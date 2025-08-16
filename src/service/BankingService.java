package service;

import entity.*;
import entity.enums.AccountType;
import entity.enums.Gender;
import entity.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BankingService {
    private final CustomerService customerService;
    private final AccountService accountService;
    
    public BankingService() {
        this.customerService = new CustomerService();
        this.accountService = new AccountService();
    }
    
    public Customer registerCustomer(String name, int age, Gender gender, 
                                   String address, String email, String phoneNumber) {
        return customerService.createCustomer(name, age, gender, address, email, phoneNumber);
    }
    
    public Optional<Customer> findCustomer(String customerId) {
        return customerService.findCustomerById(customerId);
    }
    
    public Customer updateCustomer(String customerId, String name, String email, 
                                 String phoneNumber, String address) {
        return customerService.updateCustomer(customerId, name, email, phoneNumber, address);
    }
    

    public Optional<Account> findAccount(String accountNumber) {
        return accountService.findAccountByNumber(accountNumber);
    }
    
    public List<Account> getAllAccounts() {
        return accountService.findAllAccounts();
    }
    
    public double getTotalDeposits() {
        return accountService.getTotalDeposits();
    }
    
    public double getTotalLoans() {
        return accountService.getTotalLoans();
    }
    
    public Map<AccountType, Long> getAccountDistribution() {
        return accountService.getAccountDistribution();
    }
    
    public int applyInterestToAllAccounts() {
        return accountService.applyInterestToAllAccounts();
    }
    
    public boolean applyInterest(String accountNumber) {
        return accountService.applyInterest(accountNumber);
    }
    
        public List<Transaction> getAllTransactions(int limit) {
        return accountService.getAllTransactions(limit);
    }
    
    public List<Transaction> getAccountTransactions(String accountNumber, int limit) {
        return accountService.getAccountTransactions(accountNumber, limit);
    }
    
        public Account openAccount(String customerId, AccountType accountType, double initialDeposit) {
        if (!customerService.customerExists(customerId)) {
            throw new IllegalArgumentException("Customer not found");
        }
        
        return accountService.createAccount(customerId, accountType, initialDeposit);
    }
    
    public Account deposit(String accountNumber, double amount, String description) {
        return accountService.deposit(accountNumber, amount, description);
    }
    
    public Account withdraw(String accountNumber, double amount, String description) {
        return accountService.withdraw(accountNumber, amount, description);
    }
    
    public Account[] transfer(String fromAccountNumber, String toAccountNumber, 
                            double amount, String description) {
        return accountService.transfer(fromAccountNumber, toAccountNumber, amount, description);
    }
    
    public Account getAccount(String accountNumber) {
        return accountService.getAccount(accountNumber);
    }
    
    public List<Account> getCustomerAccounts(String customerId) {
        return accountService.getCustomerAccounts(customerId);
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return accountService.getTransactionHistory(accountNumber);
    }
    
    public Account applyMonthlyInterest(String accountNumber) {
        return accountService.applyMonthlyInterest(accountNumber);
    }
    
    public double closeAccount(String accountNumber) {
        return accountService.closeAccount(accountNumber);
    }
    
    public List<Customer> searchCustomersByName(String name) {
        return customerService.findCustomersByName(name);
    }
    
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    public long getCustomerCount() {
        return customerService.getAllCustomers().size();
    }
    
    public long getAccountCount() {
        return accountService.getCustomerAccounts("").size();
    }
    
    public double getTotalBankBalance() {
        return accountService.getCustomerAccounts("").stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
    
    public void applyMonthlyInterestToAll() {
        accountService.getCustomerAccounts("").stream()
                .filter(account -> account instanceof SavingsAccount)
                .forEach(account -> accountService.applyMonthlyInterest(account.getAccountNumber()));
    }
}
