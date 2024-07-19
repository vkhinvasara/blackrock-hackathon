package com.voldy.blackrock.sms;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsParser {

    public static SmsParserResponse parseSms(String messageBody) {

        int amount = 0;
        String transactionType = "";
        String merchantName = "";

        try {
            amount = Integer.parseInt(extractAmount(messageBody));
            transactionType = extractTransactionType(messageBody);
            merchantName = extractMerchantName(messageBody);
        }catch (Exception e){
            Log.d("LOGGING", "parseSms: " + e.getMessage());
        }

        return new SmsParserResponse(amount, transactionType, merchantName);
    }

    private static String extractAmount(String messageBody) {
        Pattern pattern = Pattern.compile("Rs\\.?(\\d+(\\.\\d{2})?|\\d{1,3}(,\\d{3})*(\\.\\d{2})?)");
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            String amount = matcher.group(1);
            amount = amount.replace(",", "");
            return amount;
        }
        return null;
    }

    private static String extractTransactionType(String messageBody) {
        Pattern pattern = Pattern.compile("(credited|debited)");
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String extractMerchantName(String messageBody) {
        Pattern pattern = Pattern.compile("transfer to (.+?) Ref|by ([^\\s]+) \\(Ref");
        Matcher matcher = pattern.matcher(messageBody);
        if (matcher.find()) {
            return matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        }
        return null;
    }

}
