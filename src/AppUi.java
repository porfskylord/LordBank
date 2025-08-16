import java.util.Scanner;

public class AppUi {
    private final String logo = """
            
             __                               __        _______                    __              _______              __              __       __           __   \s
            /  |                             /  |      /       \\                  /  |            /       \\            /  |            /  |     /  |         /  |  \s
            $$ |       ______   ______   ____$$ |      $$$$$$$  | ______  _______ $$ |   __       $$$$$$$  |__     __ _$$ |_           $$ |    _$$ |_    ____$$ |  \s
            $$ |      /      \\ /      \\ /    $$ |      $$ |__$$ |/      \\/       \\$$ |  /  |      $$ |__$$ /  \\   /  / $$   |          $$ |   / $$   |  /    $$ |  \s
            $$ |     /$$$$$$  /$$$$$$  /$$$$$$$ |      $$    $$< $$$$$$  $$$$$$$  $$ |_/$$/       $$    $$/$$  \\ /$$/$$$$$$/           $$ |   $$$$$$/  /$$$$$$$ |  \s
            $$ |     $$ |  $$ $$ |  $$/$$ |  $$ |      $$$$$$$  |/    $$ $$ |  $$ $$   $$<        $$$$$$$/  $$  /$$/   $$ | __         $$ |     $$ | __$$ |  $$ |  \s
            $$ |_____$$ \\__$$ $$ |     $$ \\__$$ |      $$ |__$$ /$$$$$$$ $$ |  $$ $$$$$$  \\       $$ |       $$ $$/    $$ |/  __       $$ |_____$$ |/  $$ \\__$$ |__\s
            $$       $$    $$/$$ |     $$    $$ |      $$    $$/$$    $$ $$ |  $$ $$ | $$  |      $$ |        $$$/     $$  $$/  |      $$       $$  $$/$$    $$ /  |
            $$$$$$$$/ $$$$$$/ $$/       $$$$$$$/       $$$$$$$/  $$$$$$$/$$/   $$/$$/   $$/       $$/          $/       $$$$/$$/       $$$$$$$$/ $$$$/  $$$$$$$/$$/\s
                                                                                                                                                                   \s
            """;

    private final String appInterface = """
            
            1. Create Account
            2. Deposit Money
            3. Withdraw Money
            4. Transfer Money
            5. Apply for Loan
            6. Show Transactions
            7. Exit
            """;

    private final String loginInterface = """
            === Welcome to Lord Bank ===
            
            Plz Enter User and Password
           
            """;

    public void showAppUi(){

        System.out.println(logo);
        showLoginInterface();
    }

    public void showLoginInterface(){
        System.out.println(loginInterface);
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();
        String password = in.nextLine();

        if (Auth.isAuthenticated(userName,password)){
            showAppInterface();
        }else {
            System.out.println("User Not Authenticated");
            System.exit(0);
        }

    }

    public void showAppInterface(){
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(appInterface);
            byte input = in.nextByte();

            switch (input){
                case 1 -> {
                    System.out.println("Creating Account");
                }
                case 2 -> {
                    System.out.println("Depositing Money");
                }
                case 3 -> {
                    System.out.println("Withdrawing Money");
                }
                case 4 -> {
                    System.out.println("Transferring Money");
                }
                case 5 -> {
                    System.out.println("Applying Loan");
                }
                case 6 -> {
                    System.out.println("Showing Transaction");
                }
                case 7 -> {
                    System.out.println("Exiting The App");
                    return;
                }
                default -> System.out.println("Invalid Input");
            }
        }

    }
}
