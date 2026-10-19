package com.example.mmtbistore.util;

public class PhoneNumbers {
    private static final String PATTERN ="0\\d{9}";

    private PhoneNumbers(){}

    public static String normalize(String phone){
        String digits = phone.replaceAll("[\\s.\\-]", "");
        if (digits.startsWith("+84")){
            digits = "0" + digits.substring(3);
        }
        else if (digits.startsWith("84") && digits.length() == 11){
            digits = "0" + digits.substring(2);
        }
        return digits;
    }

    public static boolean isValid (String normalizedPhone){
        return normalizedPhone.matches(PATTERN);
    }
}
