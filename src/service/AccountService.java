package service;

import entity.*;
import entity.enums.AccountType;
import entity.enums.TransactionType;
import repository.AccountRepository;
import repository.CustomerRepository;
import util.ValidationUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    
    public AccountService() {
        this.accountRepository = AccountRepository.getInstance();
        this.customerRepository = CustomerRepository.getInstance();
    }
    

    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
    

    public Optional<Account> findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    
    public double getTotalDeposits() {
        return accountRepository.findAll().stream()
                .filter(Account::isActive)
                .mapToDouble(Account::getBalance)
                .filter(balance -> balance > 0)
                .sum();
    }
    
    public double getTotalLoans() {
        return -accountRepository.findAll().stream()
                .filter(Account::isActive)
                .mapToDouble(Account::getBalance)
                .filter(balance -> balance < 0)
                .sum();
    }
    
    public Map<AccountType, Long> getAccountDistribution() {
        return accountRepository.findAll().stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));
    }
    
    public int applyInterestToAllAccounts() {
        List<Account> accounts = findActiveAccounts();
                
        accounts.forEach(account -> {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).applyInterest();
            }
        });
        
        return accounts.size();
    }
    
    public List<Account> findActiveAccounts() {
        return accountRepository.findAll().stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());
    }
    
    public boolean applyInterest(String accountNumber) {
        Optional<Account> accountOpt = findAccount(accountNumber);
        if (accountOpt.isPresent() && accountOpt.get() instanceof SavingsAccount) {
            SavingsAccount account = (SavingsAccount) accountOpt.get();
            if (account.isActive()) {
                account.applyInterest();
                return true;
            }
        }
        return false;
    }
    
    public List<Transaction> getAllTransactions(int limit) {
        return accountRepository.findAll().stream()
                .flatMap(account -> account.getTransactions().stream())
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getAccountTransactions(String accountNumber, int limit) {
        return findAccount(accountNumber).map(account -> account.getTransactions().stream()
                        .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                        .limit(limit)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
    
    public Account createAccount(String customerId, AccountType accountType, double initialDeposit) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        
            Account account;
        String accountNumber = accountRepository.generateAccountNumber();
        
        switch (accountType) {
            case SAVINGS:
                if (initialDeposit < Account.BankRules.MIN_SAVINGS_BALANCE) {
                    throw new IllegalArgumentException(
                        String.format("Minimum initial deposit for savings account is %.2f", 
                        Account.BankRules.MIN_SAVINGS_BALANCE));
                }
                account = new SavingsAccount(customerId, initialDeposit);
                break;
                
            case CURRENT:
                if (initialDeposit < Account.BankRules.MIN_CURRENT_BALANCE) {
                    throw new IllegalArgumentException(
                        String.format("Minimum initial deposit for current account is %.2f", 
                        Account.BankRules.MIN_CURRENT_BALANCE));
                }
                account = new CurrentAccount(customerId, initialDeposit);
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
        
        account = accountRepository.save(account);
        
        recordTransaction(account.getAccountNumber(), 
                         TransactionType.OPENING_BALANCE, 
                         initialDeposit, 
                         "Account opened with initial deposit");
        
        return account;
    }
    
    public Account deposit(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        Account account = getAccount(accountNumber);
        
        account.deposit(amount);
        
        account = accountRepository.save(account);
        
        recordTransaction(accountNumber, TransactionType.DEPOSIT, amount, description);
        
        return account;
    }
    
    public Account withdraw(String accountNumber, double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        Account account = getAccount(accountNumber);
        
        if (account.getBalance() < amount && 
            !(account instanceof CurrentAccount && 
              ((CurrentAccount) account).getAvailableBalance() >= amount)) {
            throw new IllegalStateException("Insufficient funds");
        }
        
        account.withdraw(amount);
        
        account = accountRepository.save(account);
        
        recordTransaction(accountNumber, TransactionType.WITHDRAWAL, amount, description);
        
        return account;
    }
    
    public Account[] transfer(String fromAccountNumber, String toAccountNumber, 
                            double amount, String description) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);
        
        if (fromAccount.getBalance() < amount && 
            !(fromAccount instanceof CurrentAccount && 
              ((CurrentAccount) fromAccount).getAvailableBalance() >= amount)) {
            throw new IllegalStateException("Insufficient funds for transfer");
        }
        
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        String transferDesc = String.format("Transfer %s: %s", 
            description != null ? "(" + description + ")" : "", 
            fromAccountNumber.equals(fromAccountNumber) ? "to " + toAccountNumber : "from " + fromAccountNumber);
        
        recordTransaction(fromAccountNumber, TransactionType.TRANSFER_OUT, amount, transferDesc);
        recordTransaction(toAccountNumber, TransactionType.TRANSFER_IN, amount, transferDesc);
        
        return new Account[]{fromAccount, toAccount};
    }
    
    public Account getAccount(String accountNumber) {
        return findAccount(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
    
    public List<Account> getCustomerAccounts(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        return accountRepository.findByCustomerId(customerId);
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new IllegalArgumentException("Account not found");
        }
        return accountRepository.getTransactions(accountNumber);
    }
    
    public Account applyMonthlyInterest(String accountNumber) {
        Optional<Account> accountOpt = accountRepository.findByAccountNumber(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        
        Account account = accountOpt.get();
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalStateException("Monthly interest can only be applied to savings accounts");
        }
        
        SavingsAccount savingsAccount = (SavingsAccount) account;
        savingsAccount.applyInterest();
        
        recordTransaction(accountNumber, TransactionType.INTEREST, 
                         savingsAccount.getBalance() * savingsAccount.getInterestRate() / 12, 
                         "Monthly interest");
        
        return accountRepository.save(savingsAccount);
    }
    
    private void recordTransaction(String accountNumber, TransactionType type, 
                                 double amount, String description) {
        String transactionId = UUID.randomUUID().toString();
        Transaction transaction = new Transaction(transactionId, accountNumber, type, amount);
        accountRepository.addTransaction(transaction);
    }
    
    public double closeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);
        
        if (Math.abs(account.getBalance()) > 0.01) {
            throw new IllegalStateException("Cannot close account with non-zero balance");
        }
        
        double finalBalance = account.getBalance();
        
        recordTransaction(accountNumber, TransactionType.CLOSING_BALANCE, 
                         finalBalance, "Account closed");
        
        accountRepository.delete(accountNumber);
        
        return finalBalance;
    }
}
