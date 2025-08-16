package util;

import java.util.regex.Pattern;


public final class ValidationUtils {
    private ValidationUtils() {}
    
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s'-]+");
    

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && 
               NAME_PATTERN.matcher(name).matches();
    }
    

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && 
               accountNumber.matches("^[A-Z]{4}\\d{4,8}$");
    }
    

    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && 
               customerId.matches("^CUST\\d{4,8}$");
    }
    

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                String specialChars = "!@#$%^&*()_+-=[]{};':\",./<>?|`~";
                if (specialChars.indexOf(c) >= 0) {
                    hasSpecial = true;
                }
            }
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
