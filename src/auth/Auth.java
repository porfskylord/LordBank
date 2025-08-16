package auth;

public class Auth {
    private static final String ROOT_USER = "admin";
    private static final String ROOT_PASSWORD = "admin";

    public static boolean isAuthenticated(String userName, String password) {
        return (ROOT_USER.equals(userName) && ROOT_PASSWORD.equals(password));
    }
}
