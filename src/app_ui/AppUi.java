package app_ui;

import auth.Auth;
import entity.*;
import entity.enums.AccountType;
import entity.enums.Gender;
import entity.enums.TransactionType;
import service.BankingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AppUi {
    private final BankingService bankingService;
    private final Scanner scanner;
    private String currentUser;

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
            
            === Main Menu ===
            1. Customer Management
            2. Account Management
            3. Transaction Processing
            4. Reports
            5. System Administration
            0. Exit
            """;
            
    private static final String CUSTOMER_MENU = """
            
            === Customer Management ===
            1. Register New Customer
            2. View Customer Details
            3. Update Customer Information
            4. List All Customers
            0. Back to Main Menu
            """;
            
    private static final String ACCOUNT_MENU = """
            
            === Account Management ===
            1. Open New Account
            2. Close Account
            3. View Account Details
            4. List All Accounts
            5. Apply Monthly Interest
            0. Back to Main Menu
            """;
            
    private static final String TRANSACTION_MENU = """
            
            === Transaction Processing ===
            1. Deposit
            2. Withdraw
            3. Transfer
            4. View Transaction History
            0. Back to Main Menu
            """;
            
    private static final String REPORTS_MENU = """
            
            === Reports ===
            1. Bank Summary
            2. Customer Statement
            3. Transaction History
            4. View Account Transactions
            0. Back to Main Menu
            """;
            
    private static final String ADMIN_MENU = """
            
            === System Administration ===
            1. Apply Monthly Interest to All
            2. System Information
            3. Apply Interest to Account
            0. Back to Main Menu
            """;

    private static final String LOGIN_PROMPT = """
            === Welcome to Lord Bank ===
            
            Plz Enter User and Password
            """;

    public AppUi() {
        this.bankingService = new BankingService();
        this.scanner = new Scanner(System.in);
    }
    
    public void showAppUi() {
        System.out.println(LOGO);
        showLoginInterface();
    }

    private void showLoginInterface() {
        System.out.println(LOGIN_PROMPT);
        
        System.out.print("Username: ");
        String userName = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (Auth.isAuthenticated(userName, password)) {
            this.currentUser = userName;
            System.out.println("\nLogin successful! Welcome, " + userName + "!");
            pressEnterToContinue();
            showMainMenu();
        } else {
            System.out.println("\nError: Invalid credentials. Access denied.");
            System.exit(1);
        }
    }

    private void showMainMenu() {
        while (true) {
            clearScreen();
            System.out.println(MAIN_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-5): ", 0, 5);
            
            switch (choice) {
                case 1 -> showCustomerMenu();
                case 2 -> showAccountMenu();
                case 3 -> showTransactionMenu();
                case 4 -> showReportsMenu();
                case 5 -> showAdminMenu();
                case 0 -> {
                    System.out.println("\nThank you for using Lord Bank. Goodbye!");
                    scanner.close();
                    return;
                }
            }
        }
    }
    
    private void showCustomerMenu() {
        while (true) {
            clearScreen();
            System.out.println(CUSTOMER_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-4): ", 0, 4);
            
            switch (choice) {
                case 1 -> registerNewCustomer();
                case 2 -> viewCustomerDetails();
                case 3 -> updateCustomerInfo();
                case 4 -> listAllCustomers();
                case 0 -> { return; }
            }
            
            pressEnterToContinue();
        }
    }
    
    private void showAccountMenu() {
        while (true) {
            clearScreen();
            System.out.println(ACCOUNT_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-5): ", 0, 5);
            
            switch (choice) {
                case 1 -> openNewAccount();
                case 2 -> closeAccount();
                case 3 -> viewAccountDetails();
                case 4 -> listAllAccounts();
                case 5 -> applyMonthlyInterest();
                case 0 -> { return; }
            }
            
            pressEnterToContinue();
        }
    }
    
    private void showTransactionMenu() {
        while (true) {
            clearScreen();
            System.out.println(TRANSACTION_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-4): ", 0, 4);
            
            switch (choice) {
                case 1 -> handleDeposit();
                case 2 -> handleWithdraw();
                case 3 -> handleTransfer();
                case 4 -> viewTransactionHistory();
                case 0 -> { return; }
            }
            
            pressEnterToContinue();
        }
    }
    
    private void showReportsMenu() {
        while (true) {
            clearScreen();
            System.out.println(REPORTS_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-4): ", 0, 4);
            
            switch (choice) {
                case 1 -> showBankSummary();
                case 2 -> generateCustomerStatement();
                case 3 -> viewTransactionHistory();
                case 4 -> viewAccountTransactions("");
                case 0 -> { return; }
            }
            
            pressEnterToContinue();
        }
    }
    
    private void showAdminMenu() {
        while (true) {
            clearScreen();
            System.out.println(ADMIN_MENU);
            
            int choice = getIntInput("\nEnter your choice (0-3): ", 0, 3);
            
            switch (choice) {
                case 1 -> applyInterestToAllAccounts();
                case 2 -> showSystemInfo();
                case 3 -> applyMonthlyInterest();
                case 0 -> { return; }
            }
            
            pressEnterToContinue();
        }
    }

    private void registerNewCustomer() {
        clearScreen();
        System.out.println("\n=== Register New Customer ===");
        
        String name = getStringInput("Full Name: ", true);
        int age = getIntInput("Age: ", 18, 120);
        
        System.out.println("\nAvailable Genders: ");
        for (int i = 0; i < Gender.values().length; i++) {
            System.out.printf("%d. %s%n", i + 1, Gender.values()[i]);
        }
        
        Gender gender = Gender.values()[getIntInput("\nSelect Gender (1-" + Gender.values().length + "): ", 
            1, Gender.values().length) - 1];
        
        String address = getStringInput("Address: ", true);
        String email = getStringInput("Email: ", true);
        String phone = getStringInput("Phone Number: ", true);
        
        try {
            Customer customer = bankingService.registerCustomer(name, age, gender, address, email, phone);
            System.out.println("\nCustomer registered successfully!");
            System.out.println("Customer ID: " + customer.getCustomerId());
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void viewCustomerDetails() {
        clearScreen();
        System.out.println("\n=== View Customer Details ===");
        
        String customerId = getStringInput("Enter Customer ID: ", true);
        
        try {
            Optional<Customer> customerOpt = bankingService.findCustomer(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                
                System.out.println("\nCustomer Details:");
                System.out.println("-".repeat(40));
                System.out.printf("%-15s: %s%n", "ID", customer.getCustomerId());
                System.out.printf("%-15s: %s%n", "Name", customer.getName());
                System.out.printf("%-15s: %d%n", "Age", customer.getAge());
                System.out.printf("%-15s: %s%n", "Gender", customer.getGender());
                System.out.printf("%-15s: %s%n", "Email", customer.getEmail());
                System.out.printf("%-15s: %s%n", "Phone", customer.getPhoneNumber());
                System.out.printf("%-15s: %s%n", "Address", customer.getAddress());
                System.out.printf("%-15s: %s%n", "Join Date", customer.getJoinDate());
                
                List<Account> accounts = bankingService.getCustomerAccounts(customerId);
                if (accounts.isEmpty()) {
                    System.out.println("\nNo accounts found for this customer.");
                } else {
                    System.out.println("\nAccounts:");
                    System.out.println("-".repeat(40));
                    System.out.printf("%-12s %-15s %-10s %12s%n", 
                        "Account #", "Type", "Status", "Balance");
                    System.out.println("-".repeat(50));
                    
                    for (Account account : accounts) {
                        System.out.printf("%-12s %-15s %-10s $%11.2f%n",
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getBalance() >= 0 ? "Active" : "Overdrawn",
                            Math.abs(account.getBalance()));
                    }
                }
            } else {
                System.out.println("\n Customer not found.");
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void updateCustomerInfo() {
        clearScreen();
        System.out.println("\n=== Update Customer Information ===");
        
        String customerId = getStringInput("Enter Customer ID: ", true);
        
        try {
            Optional<Customer> customerOpt = bankingService.findCustomer(customerId);
            if (customerOpt.isPresent()) {
                Customer customer = customerOpt.get();
                
                System.out.println("\nCurrent Information:");
                System.out.println("-".repeat(40));
                System.out.printf("1. %-20s: %s%n", "Name", customer.getName());
                System.out.printf("2. %-20s: %s%n", "Email", customer.getEmail());
                System.out.printf("3. %-20s: %s%n", "Phone", customer.getPhoneNumber());
                System.out.printf("4. %-20s: %s%n", "Address", customer.getAddress());
                
                System.out.println("\nEnter new values (press Enter to keep current):");
                
                String name = getStringInput("Name [" + customer.getName() + "]: ", false);
                if (name.isEmpty()) name = customer.getName();
                
                String email = getStringInput("Email [" + customer.getEmail() + "]: ", false);
                if (email.isEmpty()) email = customer.getEmail();
                
                String phone = getStringInput("Phone [" + customer.getPhoneNumber() + "]: ", false);
                if (phone.isEmpty()) phone = customer.getPhoneNumber();
                
                String address = getStringInput("Address [" + customer.getAddress() + "]: ", false);
                if (address.isEmpty()) address = customer.getAddress();
                
                if (!name.equals(customer.getName()) || !email.equals(customer.getEmail()) ||
                    !phone.equals(customer.getPhoneNumber()) || !address.equals(customer.getAddress())) {
                    
                    Customer updatedCustomer = bankingService.updateCustomer(
                        customerId, name, email, phone, address);
                    
                    System.out.println("\nCustomer information updated successfully!");
                } else {
                    System.out.println("\nNo changes were made.");
                }
            } else {
                System.out.println("\n Customer not found.");
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void listAllCustomers() {
        clearScreen();
        System.out.println("\n=== All Customers ===");
        
        try {
            List<Customer> customers = bankingService.getAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("\nNo customers found.");
            } else {
                System.out.printf("\n%-10s %-25s %-15s %-30s %-12s%n", 
                    "ID", "Name", "Phone", "Email", "Join Date");
                System.out.println("-".repeat(90));
                
                for (Customer customer : customers) {
                    System.out.printf("%-10s %-25s %-15s %-30s %-12s%n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPhoneNumber(),
                        customer.getEmail(),
                        customer.getJoinDate());
                }
                
                System.out.println("\nTotal customers: " + customers.size());
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }

    private void openNewAccount() {
        clearScreen();
        System.out.println("\n=== Open New Account ===");
        
        String customerId = getStringInput("Customer ID: ", true);
        
        try {
            Optional<Customer> customerOpt = bankingService.findCustomer(customerId);
            if (customerOpt.isEmpty()) {
                System.out.println("\n Customer not found.");
                return;
            }
            
            AccountType[] accountTypes = {AccountType.SAVINGS, AccountType.CURRENT};
            for (int i = 0; i < accountTypes.length; i++) {
                System.out.printf("%d. %s%n", i + 1, accountTypes[i]);
            }
            
            int typeChoice = getIntInput("\nSelect Account Type (1-" + accountTypes.length + "): ", 
                1, accountTypes.length);
            AccountType accountType = accountTypes[typeChoice - 1];
            
            double initialDeposit = getDoubleInput("Initial deposit amount: ", 0, Double.MAX_VALUE);
            
            double interestRate = 0;
            if (accountType == AccountType.SAVINGS) {
                interestRate = getDoubleInput("Annual interest rate (%): ", 0, 100);
            }
            
            double overdraftLimit = 0;
            if (accountType == AccountType.CURRENT) {
                overdraftLimit = getDoubleInput("Overdraft limit: ", 0, Double.MAX_VALUE);
            }
            
            Account account = bankingService.openAccount(customerId, accountType, initialDeposit);
            
            if (accountType == AccountType.CURRENT && overdraftLimit > 0) {
                CurrentAccount currentAccount = (CurrentAccount) account;
                currentAccount.setOverdraftLimit(overdraftLimit);
            }
            
            System.out.println("\nAccount created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.printf("Balance: $%.2f%n", account.getBalance());
            if (accountType == AccountType.CURRENT) {
                System.out.printf("Overdraft Limit: $%.2f%n", overdraftLimit);
            }
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void closeAccount() {
        clearScreen();
        System.out.println("\n=== Close Account ===");
        
        String accountNumber = getStringInput("Enter Account Number: ", true);
        
        try {
            if (confirm("Are you sure you want to close this account?")) {
                double balance = bankingService.closeAccount(accountNumber);
                if (balance == 0) {
                    System.out.println("\nAccount closed successfully!");
                } else if (balance > 0) {
                    System.out.println("\nCannot close account. Please withdraw the remaining balance of $" + 
                        String.format("%.2f", balance) + " first.");
                } else {
                    System.out.println("\nAccount not found or could not be closed.");
                }
            } else {
                System.out.println("\nAccount closure cancelled.");
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void listAllAccounts() {
        clearScreen();
        System.out.println("\n=== All Accounts ===");
        
        try {
            List<Account> accounts = bankingService.getAllAccounts();
            if (accounts.isEmpty()) {
                System.out.println("\nNo accounts found.");
                return;
            }
            
            System.out.printf("%-15s %-12s %-15s %-10s %15s%n", 
                "Account Number", "Type", "Customer ID", "Status", "Balance");
            System.out.println("-".repeat(75));
            
            for (Account account : accounts) {
                System.out.printf("%-15s %-12s %-15s %-10s $%14.2f%n",
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getCustomerId(),
                    account.isActive() ? "Active" : "Inactive",
                    account.getBalance());
            }
            
            long activeAccounts = accounts.stream().filter(Account::isActive).count();
            double totalBalance = accounts.stream().mapToDouble(Account::getBalance).sum();
            
            System.out.println("\nSummary:");
            System.out.println("-".repeat(50));
            System.out.printf("Total Accounts:    %d%n", accounts.size());
            System.out.printf("Active Accounts:   %d%n", activeAccounts);
            System.out.printf("Inactive Accounts: %d%n", accounts.size() - activeAccounts);
            System.out.printf("Total Balance:     $%.2f%n", totalBalance);
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void viewAccountDetails() {
        clearScreen();
        System.out.println("\n=== Account Details ===");
        
        String accountNumber = getStringInput("Enter Account Number: ", true);
        
        try {
            Optional<Account> accountOpt = bankingService.findAccount(accountNumber);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                
                System.out.println("\nAccount Details:");
                System.out.println("-".repeat(50));
                System.out.printf("%-20s: %s%n", "Account Number", account.getAccountNumber());
                System.out.printf("%-20s: %s%n", "Type", account.getAccountType());
                System.out.printf("%-20s: %s%n", "Status", account.isActive() ? "Active" : "Closed");
                System.out.printf("%-20s: $%.2f%n", "Balance", account.getBalance());
                System.out.printf("%-20s: %s%n", "Date Opened", account.getCreatedDate());
                
                if (account instanceof SavingsAccount) {
                    SavingsAccount savings = (SavingsAccount) account;
                    System.out.printf("%-20s: %.2f%%%n", "Interest Rate", savings.getInterestRate());
                } else if (account instanceof CurrentAccount) {
                    CurrentAccount current = (CurrentAccount) account;
                    System.out.printf("%-20s: $%.2f%n", "Overdraft Limit", current.getOverdraftLimit());
                    System.out.printf("%-20s: $%.2f%n", "Available Balance", 
                        current.getBalance() + current.getOverdraftLimit());
                }
                
                List<Transaction> transactions = bankingService.getAccountTransactions(accountNumber, 5);
                if (!transactions.isEmpty()) {
                    System.out.println("\nRecent Transactions:");
                    System.out.println("-".repeat(100));
                    System.out.printf("%-20s %-15s %-30s %12s %12s%n", 
                        "Date", "Type", "Description", "Amount", "Balance");
                    System.out.println("-".repeat(100));
                    
                    for (Transaction txn : transactions) {
                        String desc = txn.getDescription();
                        if (desc.length() > 28) {
                            desc = desc.substring(0, 25) + "...";
                        }
                        
                        System.out.printf("%-20s %-15s %-30s $%11.2f $%11.2f%n",
                            txn.getTimestamp().toLocalDate(),
                            txn.getType(),
                            desc,
                            txn.getAmount(),
                            bankingService.findAccount(txn.getAccountNumber()).map(Account::getBalance).orElse(0.0));
                    }
                }
            } else {
                System.out.println("\n Account not found.");
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void applyMonthlyInterest() {
        clearScreen();
        System.out.println("\n=== Apply Monthly Interest ===");
        
        String accountNumber = getStringInput("Enter Account Number (or leave blank for all savings accounts): ", false);
        
        try {
            if (accountNumber.isEmpty()) {
                int count = bankingService.applyInterestToAllAccounts();
                System.out.printf("\nInterest applied to %d savings accounts.%n", count);
            } else {
                boolean applied = bankingService.applyInterest(accountNumber);
                if (applied) {
                    System.out.println("\nInterest applied successfully!");
                } else {
                    System.out.println("\nFailed to apply interest. Account may not be a savings account or does not exist.");
                }
            }
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void handleDeposit() {
        clearScreen();
        System.out.println("\n=== Deposit Money ===");
        
        String accountNumber = getStringInput("Enter Account Number: ", true);
        double amount = getDoubleInput("Enter amount to deposit: ", 0.01, 1000000);
        String description = getStringInput("Description (optional): ", false);
        
        if (description.isEmpty()) {
            description = "Deposit";
        }
        
        try {
            Account account = bankingService.deposit(accountNumber, amount, description);
            System.out.println("\nDeposit successful!");
            System.out.printf("New balance: $%.2f%n", account.getBalance());
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void handleWithdraw() {
        clearScreen();
        System.out.println("\n=== Withdraw Money ===");
        
        String accountNumber = getStringInput("Enter Account Number: ", true);
        double amount = getDoubleInput("Enter amount to withdraw: ", 0.01, 1000000);
        String description = getStringInput("Description (optional): ", false);
        
        if (description.isEmpty()) {
            description = "Withdrawal";
        }
        
        try {
            Account account = bankingService.withdraw(accountNumber, amount, description);
            System.out.println("\nWithdrawal successful!");
            System.out.printf("New balance: $%.2f%n", account.getBalance());
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void handleTransfer() {
        clearScreen();
        System.out.println("\n=== Transfer Money ===");
        
        String fromAccount = getStringInput("Enter Source Account Number: ", true);
        String toAccount = getStringInput("Enter Destination Account Number: ", true);
        
        if (fromAccount.equals(toAccount)) {
            System.out.println("\nSource and destination accounts cannot be the same.");
            return;
        }
        
        double amount = getDoubleInput("Enter amount to transfer: ", 0.01, 1000000);
        String description = getStringInput("Description (optional): ", false);
        
        if (description.isEmpty()) {
            description = "Transfer to " + toAccount;
        }
        
        try {
            Optional<Account> fromOpt = bankingService.findAccount(fromAccount);
            Optional<Account> toOpt = bankingService.findAccount(toAccount);
            
            if (fromOpt.isEmpty() || toOpt.isEmpty()) {
                System.out.println("\nOne or both accounts not found.");
                return;
            }
            
            System.out.println("\nTransfer Details:");
            System.out.println("-".repeat(50));
            System.out.printf("From Account: %s%n", fromAccount);
            
            Account from = fromOpt.get();
            Account to = toOpt.get();
            
            System.out.printf("To Account:   %s%n", toAccount);
            System.out.printf("Amount:       $%.2f%n", amount);
            System.out.printf("Description:  %s%n", description);
            
            if (confirm("\nConfirm this transfer?")) {
                Account[] updatedAccounts = bankingService.transfer(fromAccount, toAccount, amount, description);
                System.out.println("\nTransfer completed successfully!");
                System.out.printf("New balance: $%.2f%n", updatedAccounts[0].getBalance());
            } else {
                System.out.println("\nTransfer cancelled.");
            }
        } catch (Exception e) {
            System.out.println("\nError during transfer: " + e.getMessage());
        }
    }
    
    private void viewTransactionHistory() {
        clearScreen();
        System.out.println("\n=== Transaction History ===");
        
        String accountNumber = getStringInput("Enter Account Number (or leave blank for all accounts): ", false);
        int limit = getIntInput("Number of transactions to show (1-100): ", 1, 100);
        
        try {
            List<Transaction> transactions;
            String header;
            
            if (accountNumber.isEmpty()) {
                transactions = bankingService.getAllTransactions(limit);
                header = "All Accounts - Last " + limit + " Transactions";
            } else {
                transactions = bankingService.getAccountTransactions(accountNumber, limit);
                header = "Account " + accountNumber + " - Last " + limit + " Transactions";
            }
            
            if (transactions.isEmpty()) {
                System.out.println("\nNo transactions found.");
                return;
            }
            
            System.out.println("\n" + header);
            System.out.println("-".repeat(120));
            System.out.printf("%-20s %-12s %-15s %-40s %15s %15s%n", 
                "Date", "Type", "Account", "Description", "Amount", "Balance");
            System.out.println("-".repeat(120));
            
            for (Transaction txn : transactions) {
                String desc = txn.getDescription();
                if (desc.length() > 35) {
                    desc = desc.substring(0, 32) + "...";
                }
                
                System.out.printf("%-20s %-12s %-15s %-40s $%14.2f $%14.2f%n",
                    txn.getTimestamp().toLocalDate(),
                    txn.getType(),
                    txn.getAccountNumber(),
                    desc,
                    txn.getAmount(),
                    bankingService.findAccount(txn.getAccountNumber()).map(Account::getBalance).orElse(0.0));
            }
            
            double totalDeposits = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER_IN)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER_OUT)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            System.out.println("-".repeat(120));
            System.out.printf("%-89s $%14.2f $%14.2f%n", 
                "TOTALS:", totalDeposits, totalWithdrawals);
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void showBankSummary() {
        clearScreen();
        System.out.println("\n=== Bank Summary ===");
        
        try {
            long totalCustomers = bankingService.getCustomerCount();
            long totalAccounts = bankingService.getAccountCount();
            double totalDeposits = bankingService.getTotalDeposits();
            double totalLoans = bankingService.getTotalLoans();
            
            List<Transaction> recentTransactions = bankingService.getAllTransactions(5);
            
            System.out.println("\nAccount Statistics:");
            System.out.println("-".repeat(50));
            System.out.printf("%-30s: %,d%n", "Total Customers", totalCustomers);
            System.out.printf("%-30s: %,d%n", "Total Accounts", totalAccounts);
            System.out.printf("%-30s: $%,.2f%n", "Total Deposits", totalDeposits);
            System.out.printf("%-30s: $%,.2f%n", "Total Loans", totalLoans);
            
            if (!recentTransactions.isEmpty()) {
                System.out.println("\nRecent Transactions:");
                System.out.println("-".repeat(80));
                System.out.printf("%-12s %-10s %-15s %-25s %12s%n", 
                    "Date", "Type", "Account", "Description", "Amount");
                System.out.println("-".repeat(80));
                
                for (Transaction txn : recentTransactions) {
                    String desc = txn.getDescription();
                    if (desc.length() > 22) {
                        desc = desc.substring(0, 19) + "...";
                    }
                    
                    System.out.printf("%-12s %-10s %-15s %-25s $%11.2f%n",
                        txn.getTimestamp().toLocalDate(),
                        txn.getType(),
                        txn.getAccountNumber(),
                        desc,
                        txn.getAmount());
                }
            }
            
            Map<AccountType, Long> accountDistribution = bankingService.getAccountDistribution();
            if (!accountDistribution.isEmpty()) {
                System.out.println("\nAccount Distribution:");
                System.out.println("-".repeat(50));
                
                long total = accountDistribution.values().stream().mapToLong(Long::longValue).sum();
                for (Map.Entry<AccountType, Long> entry : accountDistribution.entrySet()) {
                    double percentage = total > 0 ? (entry.getValue() * 100.0) / total : 0;
                    System.out.printf("%-15s: %3d (%.1f%%)%n", 
                        entry.getKey(), entry.getValue(), percentage);
                }
            }
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void generateCustomerStatement() {
        clearScreen();
        System.out.println("\n=== Customer Statement ===");
        
        String customerId = getStringInput("Enter Customer ID: ", true);
        
        try {
            Optional<Customer> customerOpt = bankingService.findCustomer(customerId);
            if (customerOpt.isEmpty()) {
                System.out.println("\n Customer not found.");
                return;
            }
            
            Customer customer = customerOpt.get();
            List<Account> accounts = bankingService.getCustomerAccounts(customerId);
            
            if (accounts.isEmpty()) {
                System.out.println("\nNo accounts found for this customer.");
                return;
            }
            
            System.out.println("\nCustomer Information:");
            System.out.println("-".repeat(50));
            System.out.printf("%-15s: %s%n", "Customer ID", customer.getCustomerId());
            System.out.printf("%-15s: %s%n", "Name", customer.getName());
            System.out.printf("%-15s: %s%n", "Join Date", customer.getJoinDate());
            
            System.out.println("\nAccount Summary:");
            System.out.println("-".repeat(80));
            System.out.printf("%-12s %-15s %-12s %15s %15s%n", 
                "Account #", "Type", "Status", "Balance", "Available");
            System.out.println("-".repeat(80));
            
            double totalBalance = 0;
            double totalAvailable = 0;
            
            for (Account account : accounts) {
                double available = account.getBalance();
                if (account instanceof CurrentAccount) {
                    available += ((CurrentAccount) account).getOverdraftLimit();
                }
                
                System.out.printf("%-12s %-15s %-12s $%14.2f $%14.2f%n",
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.isActive() ? "Active" : "Inactive",
                    account.getBalance(),
                    available);
                
                if (account.isActive()) {
                    totalBalance += account.getBalance();
                    totalAvailable += available;
                }
            }
            
            System.out.println("-".repeat(80));
            System.out.printf("%-39s $%14.2f $%14.2f%n", 
                "TOTALS:", totalBalance, totalAvailable);
            
            if (confirm("\nView detailed transaction history for an account?")) {
                String accountNumber = getStringInput("Enter Account Number: ", true);
                Optional<Account> accountOpt = accounts.stream()
                    .filter(a -> a.getAccountNumber().equals(accountNumber))
                    .findFirst();
                
                if (accountOpt.isPresent()) {
                    viewAccountTransactions(accountNumber);
                } else {
                    System.out.println("\nInvalid account number.");
                }
            }
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void viewAccountTransactions(String accountNumber) {
        clearScreen();
        System.out.println("\n=== Account Transaction History ===");
        
        try {
            Optional<Account> accountOpt = bankingService.findAccount(accountNumber);
            if (accountOpt.isEmpty()) {
                System.out.println("\n Account not found.");
                return;
            }
            
            Account account = accountOpt.get();
            System.out.printf("\nAccount: %s (%s)%n", accountNumber, account.getAccountType());
            System.out.printf("Balance: $%,.2f%n", account.getBalance());
            
            if (account instanceof CurrentAccount) {
                CurrentAccount current = (CurrentAccount) account;
                System.out.printf("Available: $%,.2f (Overdraft: $%,.2f)%n", 
                    account.getBalance() + current.getOverdraftLimit(),
                    current.getOverdraftLimit());
            }
            
            int limit = getIntInput("\nNumber of transactions to show (1-100): ", 1, 100);
            
            List<Transaction> transactions = bankingService.getAccountTransactions(accountNumber, limit);
            if (transactions.isEmpty()) {
                System.out.println("\nNo transactions found for this account.");
                return;
            }
            
            System.out.println("\n" + "-".repeat(120));
            System.out.printf("%-20s %-15s %-50s %15s %15s%n", 
                "Date", "Type", "Description", "Amount", "Balance");
            System.out.println("-".repeat(120));
            
            for (Transaction txn : transactions) {
                String desc = txn.getDescription();
                if (desc.length() > 45) {
                    desc = desc.substring(0, 42) + "...";
                }
                
                System.out.printf("%-20s %-15s %-50s $%14.2f $%14.2f%n",
                    txn.getTimestamp().toLocalDate(),
                    txn.getType(),
                    desc,
                    txn.getAmount(),
                    bankingService.findAccount(txn.getAccountNumber()).map(Account::getBalance).orElse(0.0));
            }
            
            double totalDeposits = transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER_IN)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER_OUT)
                .mapToDouble(Transaction::getAmount)
                .sum();
                
            System.out.println("-".repeat(120));
            System.out.printf("%-86s $%14.2f $%14.2f%n", 
                "TOTALS:", totalDeposits, totalWithdrawals);
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void applyInterestToAllAccounts() {
        clearScreen();
        System.out.println("\n=== Apply Interest to All Accounts ===");
        
        if (!confirm("This will apply monthly interest to all eligible accounts. Continue?")) {
            System.out.println("\nOperation cancelled.");
            return;
        }
        
        try {
            int count = bankingService.applyInterestToAllAccounts();
            System.out.printf("\nSuccessfully applied interest to %d accounts.%n", count);
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }
    
    private void showSystemInfo() {
        clearScreen();
        System.out.println("\n=== System Information ===");
        
        try {
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            
            System.out.println("\nSystem Resources:");
            System.out.println("-".repeat(50));
            System.out.printf("%-20s: %s%n", "OS", System.getProperty("os.name"));
            System.out.printf("%-20s: %s%n", "Java Version", System.getProperty("java.version"));
            System.out.printf("%-20s: %d MB%n", "Total Memory", totalMemory / (1024 * 1024));
            System.out.printf("%-20s: %d MB%n", "Used Memory", usedMemory / (1024 * 1024));
            System.out.printf("%-20s: %d MB%n", "Free Memory", freeMemory / (1024 * 1024));
            
            System.out.println("\nDatabase Statistics:");
            System.out.println("-".repeat(50));
            System.out.printf("%-20s: %,d%n", "Customers", bankingService.getCustomerCount());
            System.out.printf("%-20s: %,d%n", "Accounts", bankingService.getAccountCount());
            
            Map<AccountType, Long> accountDist = bankingService.getAccountDistribution();
            for (Map.Entry<AccountType, Long> entry : accountDist.entrySet()) {
                System.out.printf("  - %-17s: %,d%n", entry.getKey(), entry.getValue());
            }
            
            System.out.printf("%-20s: $%,.2f%n", "Total Deposits", bankingService.getTotalDeposits());
            System.out.printf("%-20s: $%,.2f%n", "Total Loans", bankingService.getTotalLoans());
            
            System.out.println("\nLast Activity:");
            System.out.println("-".repeat(50));
            System.out.printf("%-20s: %s%n", "Current User", currentUser);
            System.out.printf("%-20s: %s%n", "Login Time", LocalDateTime.now());
            
        } catch (Exception e) {
            System.out.println("\n Error: " + e.getMessage());
        }
    }

    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }
    
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Please enter a number between %d and %d.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Please enter a number between %.2f and %.2f.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    private String getStringInput(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!required || !input.isEmpty()) {
                return input;
            }
            System.out.println("This field is required. Please enter a value.");
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt + " (yyyy-MM-dd): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }
    
    private boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' for yes or 'n' for no.");
        }
    }
}
