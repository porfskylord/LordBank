import java.util.Scanner;

public class Auth {
    public static boolean isAuthenticated(String userName, String password){
        String rootUser = "admin", rootPassword = "admin";
        return (rootUser.equals(userName) && rootPassword.equals(password));
    }

}
