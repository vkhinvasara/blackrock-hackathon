package com.voldy.blackrock.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSParse {

    public static SmsResponse parseSMS(String smsMessage) {
        Pattern pattern = Pattern.compile("(debited|credited) by ([0-9.]+)");
        Matcher matcher = pattern.matcher(smsMessage);

        SmsResponse smsResponse = new SmsResponse();

        if (matcher.find()) {
            String transactionType = matcher.group(1);
            String amount = matcher.group(2);

            smsResponse.setAmount(amount);
            smsResponse.setTransactionType(transactionType);
        }

        return smsResponse;
    }
}