package com.att.retain.bill.service;

import com.att.retain.bill.util.BillUtils;
import com.vindicia.soap.v1_1.select.BillTransactionsResponse;
import com.vindicia.soap.v1_1.selecttypes.Return;
import com.vindicia.soap.v1_1.selecttypes.Transaction;
import com.vindicia.soap.v1_1.selecttypes.TransactionValidationResponse;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseFileWriter implements IResponseWriter {
    static Logger log = LoggerFactory.getLogger(ResponseFileWriter.class);

    private Path resultPath;

    private static final String FILE_NAME_PREFIX = "SOAP_submit_";
    private static final String SEPARATOR = ",";

    public ResponseFileWriter(String filePath) {
        Path resultDirectory = Paths.get(filePath);
        resultPath = Paths.get(filePath + System.getProperty("file.separator")
                + FILE_NAME_PREFIX + BillUtils.getNow("yyyyMMdd_HHmmss") + ".csv");
        String resultFileHeader = "timestamp,SOAP_ID,merchantTransactionId,TVR_CODE,TVR_DESCRIPTION,RETURN_CODE,RETURN_STRING";
        try {
            if (!Files.exists(resultDirectory))
                Files.createDirectories(resultDirectory);
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
        writeContent(resultPath, resultFileHeader, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Pair<Integer, Integer> writeSubmitResult(int pageNum, List<Transaction> transactions, BillTransactionsResponse billTransactionsResponse) {

        int nSubmittedTransactions = 0, nSubmissionFailures = 0;
        StringBuffer strResultContent = new StringBuffer();

        if (billTransactionsResponse != null) {
            Return btxsReturn = billTransactionsResponse.get_return();  // The result of SOAP request
            String soapId = btxsReturn.getSoapId();
            int returnCode = btxsReturn.getReturnCode().getValue();
            String returnMsg = btxsReturn.getReturnString();

            if (returnCode == 200) {
                TransactionValidationResponse[] response = billTransactionsResponse.getResponse(); // the result of each transactions.
                if (response != null) {
                    for (TransactionValidationResponse txValidResponse : response) {
                        strResultContent = new StringBuffer();
                        if (txValidResponse != null) {
                            strResultContent.append(BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss Z")).append(SEPARATOR);
                            strResultContent.append(soapId).append(SEPARATOR);
                            strResultContent.append(txValidResponse.getMerchantTransactionId()).append(SEPARATOR);
                            strResultContent.append(txValidResponse.getCode()).append(SEPARATOR);
                            strResultContent.append(txValidResponse.getDescription()).append(SEPARATOR);
                            strResultContent.append(returnCode).append(SEPARATOR);
                            strResultContent.append(returnMsg);
                            writeContent(resultPath, strResultContent.toString(), StandardOpenOption.APPEND);
                        }
                    }
                } else { // this soap call was successful.
                    for (Transaction transaction : transactions) {
                        strResultContent = new StringBuffer();
                        strResultContent.append(BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss Z")).append(SEPARATOR);
                        strResultContent.append(soapId).append(SEPARATOR);
                        strResultContent.append(transaction.getMerchantTransactionId());
                        strResultContent.append(SEPARATOR);
                        strResultContent.append(SEPARATOR);
                        strResultContent.append(returnCode).append(SEPARATOR);
                        strResultContent.append(returnMsg);
                        writeContent(resultPath, strResultContent.toString(), StandardOpenOption.APPEND);
                        nSubmittedTransactions++;
                    }
                }
            } else {
                strResultContent.append(BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss Z")).append(SEPARATOR);
                strResultContent.append(soapId).append(SEPARATOR);
                strResultContent.append(SEPARATOR);
                strResultContent.append(SEPARATOR);
                strResultContent.append(SEPARATOR);
                strResultContent.append(returnCode).append(SEPARATOR);
                strResultContent.append(returnMsg);
                writeContent(resultPath, strResultContent.toString(), StandardOpenOption.APPEND);
            }

        } else {
            nSubmissionFailures++;
            log.error("\n\tPage " + pageNum + " Transactions : [503] Communication Failed (Soap Server)!");
            strResultContent.append(BillUtils.getNow("yyyy-MM-dd'T'HH:mm:ss Z")).append(SEPARATOR);
            strResultContent.append(SEPARATOR).append(SEPARATOR).append(SEPARATOR).append(SEPARATOR);
            strResultContent.append("500").append(SEPARATOR);
            strResultContent.append("Page ").append(pageNum).append(": Process Failed (Soap Server)");

            writeContent(resultPath, strResultContent.toString(), StandardOpenOption.APPEND);
        }
        return new ImmutablePair<>(nSubmittedTransactions, nSubmissionFailures);
    }

    private void writeContent(Path path, String content, StandardOpenOption standardOpenOption) {
        try  {
            byte[] bytes = (content + System.lineSeparator()).getBytes(UTF_8);
            Files.write(path, bytes, StandardOpenOption.CREATE, standardOpenOption);
        } catch(IOException ioe) {
            log.error(ioe.getMessage());
        }
    }
}
