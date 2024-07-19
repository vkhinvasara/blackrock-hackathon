package com.voldy.blackrock.common;

import com.voldy.blackrock.model.UserRegistration;

public class UserRegistrationValidator {

    public static boolean isValid(UserRegistration user) {

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }


        if (user.getMobileNumber() == null || user.getMobileNumber().trim().isEmpty()) {
            return false;
        }


        if (user.getSalary() < 0) {
            return false;
        }

        if (user.getFoodExpense() < 0 || user.getHealthExpense() < 0 || user.getLivingExpense() < 0 ||
                user.getTransportExpense() < 0 || user.getPayables() < 0 || user.getMiscellaneous() < 0) {
            return false;
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !isValidEmail(user.getEmail())) {
            return false;
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty() || user.getPassword().length() < 6) {
            return false;
        }

        return true;
    }

    // Helper method to validate email pattern
    private static boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}
