package app_ui;

import auth.Auth;

import java.util.Scanner;

public class AppUi {

    private static final String LOGO = """
            
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

    private static final String MAIN_MENU = """
            
            1. Create Account
            2. Deposit Money
            3. Withdraw Money
            4. Transfer Money
            5. Apply for Loan
            6. Show Transactions
            7. Exit
            """;

    private static final String LOGIN_PROMPT = """
            === Welcome to Lord Bank ===
            
            Plz Enter User and Password
            """;

    public void showAppUi(){
        System.out.println(LOGO);
        showLoginInterface();
    }

    private void showLoginInterface() {
        System.out.println(LOGIN_PROMPT);
        Scanner in = new Scanner(System.in);
        
        System.out.print("Username: ");
        String userName = in.nextLine().trim();
        
        System.out.print("Password: ");
        String password = in.nextLine().trim();

        if (Auth.isAuthenticated(userName, password)) {
            showAppInterface();
        } else {
            System.out.println("Error: Invalid credentials. Access denied.");
            System.exit(1);
        }
    }

    private void showAppInterface() {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println(MAIN_MENU);
            System.out.print("\nEnter your choice (1-7): ");
            
            if (!in.hasNextInt()) {
                System.out.println("\nError: Please enter a number");
                in.next();
                continue;
            }
            
            int input = in.nextInt();
            in.nextLine();
            
            switch (input) {
                case 1 -> handleCreateAccount(in);
                case 2 -> System.out.println("\nDeposit Money");
                case 3 -> System.out.println("\nWithdraw Money");
                case 4 -> System.out.println("\nTransfer Money");
                case 5 -> System.out.println("\nApply for Loan");
                case 6 -> System.out.println("\nShow Transactions");
                case 7 -> {
                    System.out.println("\nThank you for using Lord Bank. Goodbye!");
                    in.close();
                    return;
                }
                default -> System.out.println("\nError: Invalid option");
            }

        }
    }

    private void handleCreateAccount(Scanner in) {
        System.out.println("\n--- Create New Account ---");
        pressEnterToContinue(in);
    }

    private void pressEnterToContinue(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    private void handleDeposit() {
        System.out.println("\n--- Deposit Money ---");
        System.out.println("Deposit feature coming soon!\n");
    }

    private void handleWithdraw() {
        System.out.println("\n--- Withdraw Money ---");
        System.out.println("Withdrawal feature coming soon!\n");
    }

    private void handleTransfer() {
        System.out.println("\n--- Transfer Money ---");
        System.out.println("Transfer feature coming soon!\n");
    }

    private void handleLoanApplication() {
        System.out.println("\n--- Apply for Loan ---");
        System.out.println("Loan application feature coming soon!\n");
    }

    private void showTransactions() {
        System.out.println("\n--- Transaction History ---");
        System.out.println("Transaction history feature coming soon!\n");
    }
}
