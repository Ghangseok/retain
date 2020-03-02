package com.att.retain.bill;

import com.att.retain.bill.dto.RetainConnectionInfo;
import com.att.retain.bill.process.ReadWorker;
import com.att.retain.bill.process.SubmitWorker;
import com.att.retain.bill.service.IResponseWriter;
import com.att.retain.bill.service.ITransactionsReader;
import com.att.retain.bill.service.ResponseFileWriter;
import com.att.retain.bill.service.TransactionDatabaseReader;
import com.att.retain.bill.service.TransactionFileReader;
import com.att.retain.bill.util.BillUtils;
import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
public class SEL001BillSelect implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SEL001BillSelect.class);

    @Value("${submit.billTransactions.soap_url}")
    String endpoint;
    @Value("${submit.billTransactions.soap_username}")
    String username;
    @Value("${submit.billTransactions.soap_password}")
    String password;

    @Value("${submit.billTransactions.version}")
    String version;
    @Value("${submit.billTransactions.user_agent}")
    String userAgent;
    @Value("${submit.billTransactions.timeout_in_milliseconds}")
    int timeOutInMilliSeconds;

    RetainConnectionInfo retainConnectionInfo;

    @Value("${submit.billTransactions.request_header}")
    String request_header;
    @Value("${submit.billTransactions.request_path}")
    String request_path;
    @Value("${submit.billTransactions.request_file}")
    String request_file;
    @Value("${submit.billTransactions.response_path}")
    String response_path;

    @Value("${submit.billTransactions.pageSize}")
    Integer pageSize;
    @Value("${submit.billTransactions.maxThreadWorkers}")
    Integer maxThreadWorkers;
    @Value("${submit.billTransactions.retryWaitingMsec}")
    Integer retryWaitingMsec;
    @Value("${submit.billTransactions.retryTimes}")
    Integer retryTimes;
    @Value("${submit.billTransactions.txSource}")
    String txSource;

    private ITransactionsReader transactionsReader;
    private IResponseWriter responseWriter;

    private ObjectProvider<ReadWorker> readWorkerObjectProvider;

    @Autowired
    public SEL001BillSelect(ObjectProvider<ReadWorker> readWorkerObjectProvider) {
        this.readWorkerObjectProvider = readWorkerObjectProvider;
    }

    public static void main(String[] args) {
        SpringApplication.run(SEL001BillSelect.class, args);
    }

    public void init(ApplicationArguments args) {

        Set<String> argNames = args.getOptionNames();
        argNames.stream().forEach(name-> {
            if (args.getOptionValues(name) != null) {
                String argValue = args.getOptionValues(name).stream().findFirst().get();
                switch (name) {
                    case "endpoint":
                        this.endpoint = argValue;
                        break;
                    case "username":
                        this.username = argValue;
                        break;
                    case "password":
                        this.password = argValue;
                        break;
                    case "responsePath":
                        this.response_path = argValue;
                        break;
                    case "maxThreadWorkers":
                        this.maxThreadWorkers = Integer.parseInt(argValue);
                        break;
                    case "pageSize":
                        this.pageSize = Integer.parseInt(argValue);
                        break;
                }
            }
        });

        retainConnectionInfo = new RetainConnectionInfo(endpoint, username, password, version, userAgent,
                                                                  timeOutInMilliSeconds, retryTimes, retryWaitingMsec);

        log.info("\n\tendpoint=" + endpoint + "\n\tlogin=" + username +
                "\n\n\tversion=" + version + "\n\tuserAgent=" + userAgent +
                "\n\ttimeOutInMilliSeconds=" + timeOutInMilliSeconds + "\n");

        if (txSource != null && txSource.equals("FILE")) {
            transactionsReader = new TransactionFileReader(request_header, request_path, request_file);
            responseWriter = new ResponseFileWriter(response_path);
        } else {
            transactionsReader = new TransactionDatabaseReader();
            responseWriter = new ResponseFileWriter(response_path);
            //responseWriter = new ResponseDatabaseWriter();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        init(args);

        String startTimeOfProcess = BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

        int pageNum = 0;
        int nSubmittedTransactions = 0, nSubmissionFailures = 0;
        boolean bHasTransaction = true;

        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            ExecutorService readWorkerThreadPool = Executors.newFixedThreadPool(maxThreadWorkers);
            ExecutorService submitWorkerThreadPool = Executors.newFixedThreadPool(maxThreadWorkers);

            while (bHasTransaction) { // Until all transactions are read from the source.

                List<Callable<Pair<Integer, List<Transaction>>>> callableReader = new LinkedList<Callable<Pair<Integer, List<Transaction>>>>();
                List<Future<Pair<Integer, List<Transaction>>>> listTransactionData = new LinkedList<Future<Pair<Integer, List<Transaction>>>>();

                List<Callable<Pair<Integer, BillTransactionsResponse>>> listCallableBillTransaction = new LinkedList<Callable<Pair<Integer, BillTransactionsResponse>>>();
                List<Future<Pair<Integer, BillTransactionsResponse>>> listBillTransactionsResponse = new LinkedList<Future<Pair<Integer, BillTransactionsResponse>>>();

                int bulkReadSize = maxThreadWorkers;

                /***********************************************************************************************************
                 * Read the transactions concurrently
                 */
                while (bulkReadSize-- > 0) {
                    pageNum++;

                    final Callable<Pair<Integer, List<Transaction>>> readWorker =
                            readWorkerObjectProvider.getObject(transactionsReader, pageNum, pageSize);
                    callableReader.add(readWorker); // add the reader callable object into thread pool.
                }
                // Executes the given readers, returning a list of Futures holding their status and results when all complete.
                listTransactionData = readWorkerThreadPool.invokeAll(callableReader);

                /***********************************************************************************************************
                 * Submit the transactions through SOAP concurrently
                 */
                Map<Integer, List<Transaction>> mapTransactionsToPage = new HashMap<>();
                for (Future<Pair<Integer, List<Transaction>>> future : listTransactionData) { // future has page # and list of transactions.
                    Pair<Integer, List<Transaction>> txnsWithPageNum = future.get();
                    mapTransactionsToPage.put(txnsWithPageNum.getLeft(), txnsWithPageNum.getRight());

                    Transaction[] transactions = txnsWithPageNum.getRight().stream().toArray(Transaction[]::new);
//                    Transaction[] transactions = txnsWithPageNum.getRight().toArray(new Transaction[0]);

                    if (transactions.length < pageSize) { // If there is at least one page which has transactions less than pageSize,
                                                          // it means the reader already read all transactions.
                        bHasTransaction = false;          // Abort while loop.
                    }

                    if (transactions.length > 0) { // submit the only page which has transactions.
                        Callable<Pair<Integer, BillTransactionsResponse>> submitWorker =
                                new SubmitWorker(txnsWithPageNum.getLeft(), transactions, retainConnectionInfo);
                        listCallableBillTransaction.add(submitWorker); // add the submit transactions into thread pool.
                    }
                }
                 //Executes the given submits, returning a list of Futures holding their status and results when all complete.
                listBillTransactionsResponse = submitWorkerThreadPool.invokeAll(listCallableBillTransaction);

                /***********************************************************************************************************
                 * Writing Result sequentially
                 */
                for (Future<Pair<Integer, BillTransactionsResponse>> futureResponse : listBillTransactionsResponse) { // future has page # and response of submit.
                    Pair<Integer, BillTransactionsResponse> responseWithPageNum = futureResponse.get();

                    Pair<Integer, Integer> resultCounts
                            = responseWriter.writeSubmitResult(responseWithPageNum.getLeft(),
                            mapTransactionsToPage.get(responseWithPageNum.getLeft()), responseWithPageNum.getRight());
                    nSubmittedTransactions += resultCounts.getLeft();
                    nSubmissionFailures += resultCounts.getRight();
                }
            }

            readWorkerThreadPool.shutdown();
            submitWorkerThreadPool.shutdown();

            stopWatch.stop();
            log.debug("Elapsed time is [" + stopWatch.getTotalTimeSeconds() + "] sec.");

            // Get the Java runtime
            Runtime runtime = Runtime.getRuntime();
            // Run the garbage collector
            runtime.gc();
            // Calculate the used memory
            long memory = runtime.totalMemory() - runtime.freeMemory();
            log.debug("Used memory is bytes: " + memory);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        String endTimeOfProcess = BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        /***************************************************************************************
         * Statistics of Submit
         */
        log.info("****************************************************************************");
        log.info("*** pageSize              : [{}]", pageSize);
        log.info("*** nThreads              : [{}]", maxThreadWorkers);
        log.info("*** nRetries              : [{}]", retryTimes);
        log.info("*** retryWaitSeconds      : [{}]", retryWaitingMsec / 1000);
        log.info("*** submit transactions   : [{}]", nSubmittedTransactions);
        log.info("*** submission failures   : [{}]", nSubmissionFailures);
        log.info("*** start time of process : [{}]", startTimeOfProcess);
        log.info("*** end time of process   : [{}]", endTimeOfProcess);
        log.info("****************************************************************************");
    }
}