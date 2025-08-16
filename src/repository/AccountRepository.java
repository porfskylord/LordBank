package repository;

import entity.Account;
import entity.SavingsAccount;
import entity.CurrentAccount;
import entity.Transaction;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class AccountRepository {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Map<String, List<Transaction>> transactions = new ConcurrentHashMap<>();
    
    private static volatile AccountRepository instance;
    
    private AccountRepository() {
    }
    

    public static AccountRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new AccountRepository();
                }
            }
        }
        return instance;
    }
    

    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }
    

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }
    

    public List<Account> findByCustomerId(String customerId) {
        return accounts.values().stream()
            .filter(account -> account.getCustomerId().equals(customerId))
            .collect(Collectors.toList());
    }
    

    public void recordTransaction(String accountNumber, Transaction transaction) {
        if (accountNumber == null || transaction == null) {
            throw new IllegalArgumentException("Account number and transaction cannot be null");
        }
        transactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
    }
    

    public List<Transaction> getTransactions(String accountNumber) {
        return transactions.getOrDefault(accountNumber, Collections.emptyList());
    }
    

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
    

    public boolean deleteByAccountNumber(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }
    

    public long count() {
        return accounts.size();
    }
    

    public void deleteAll() {
        accounts.clear();
        transactions.clear();
    }

    public String generateAccountNumber() {
        return "ACCT" + String.format("%08d", accounts.size() + 1);
    }
}
