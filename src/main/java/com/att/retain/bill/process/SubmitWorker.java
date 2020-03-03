package com.att.retain.bill.process;

import com.att.retain.bill.dto.RetainConnectionInfo;
import com.att.retain.bill.model.RequestCommunicationId;
import com.vindicia.soap.v1_1.select.BillTransactions;
import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.select.SelectStub;
import com.vindicia.soap.v1_1.selecttypes.Authentication;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.concurrent.Callable;

import static com.att.retain.bill.util.BillUtils.pause;

public class SubmitWorker implements Callable<Pair<RequestCommunicationId, BillTransactionsResponse>> {
    static Logger log = LoggerFactory.getLogger(SubmitWorker.class);

    private Transaction[] transactions;
    // setup connection:

    SelectStub select;
    Authentication auth;
    Integer retryTimes;
    Integer retryWaitingMsec;
    RequestCommunicationId reqCommId;


    public SubmitWorker(RequestCommunicationId reqCommId, Transaction[] transactions, RetainConnectionInfo retainConnectionInfo) throws AxisFault {
        this.transactions = transactions;

        select = new SelectStub(retainConnectionInfo.getEndpoint());
        select._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        select._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, new Integer(retainConnectionInfo.getTimeOutInMilliSeconds()));
        select._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(retainConnectionInfo.getTimeOutInMilliSeconds()));

        auth = new Authentication();
        auth.setLogin(retainConnectionInfo.getUsername());
        auth.setPassword(retainConnectionInfo.getPassowrd());
        auth.setVersion(retainConnectionInfo.getVersion());
        auth.setUserAgent(retainConnectionInfo.getUserAgent());

        this.retryTimes = retainConnectionInfo.getRetryTimes();
        this.retryWaitingMsec = retainConnectionInfo.getRetryWaitingMsec();

        this.reqCommId = reqCommId;
    }

    public Pair<RequestCommunicationId, BillTransactionsResponse> call() throws Exception {
        BillTransactions billTransactions = new BillTransactions();

        billTransactions.setAuth(auth);
        billTransactions.setTransactions(transactions);

        log.info(Thread.currentThread().getName() + "|" + transactions.length);

        BillTransactionsResponse billTransactionsResponse = null;
        try {
            boolean bFail = true;
            int numTimeouts = 0;
            do {
                try {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    billTransactionsResponse = select.billTransactions(billTransactions); // Call BillTransactions through SOAP
                    stopWatch.stop();

                    bFail = false; // communication is successful.
                    log.info("Elapsed time to submit transactions[" + Thread.currentThread().getName()
                            + "] - SOAP ID:[" + billTransactionsResponse.get_return().getSoapId() + "]: "
                            + stopWatch.getTotalTimeMillis() + " milliseconds");

                } catch (IOException ioe) {
                    log.error(ioe.getMessage());

                    if (numTimeouts < retryTimes) {
                        String strWaitTime = "";
                        if (retryWaitingMsec > 60000) {
                            strWaitTime = retryWaitingMsec / 60000 + " minute(s)";
                        } else {
                            strWaitTime = retryWaitingMsec / 1000 + " second(s)";
                        }
                        bFail = true; // retry again.
                        log.error("[" + ++numTimeouts + " time(s)]: Wait " + strWaitTime + " for initial query to finish: " + Thread.currentThread().getName());
                        pause(retryWaitingMsec);
                    } else {
                        bFail = false;
                    }
                }
            } while (bFail);

            pause(1000 * transactions.length + 1000); // Each Transaction submitted adds about 1 second to the response time.
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ImmutablePair<>(this.reqCommId, billTransactionsResponse);
    }
}
