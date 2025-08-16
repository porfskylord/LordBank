package repository;

import entity.Account;
import entity.SavingsAccount;
import entity.CurrentAccount;
import entity.Transaction;
import util.FileUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AccountRepository {
    private static final String ACCOUNTS_FILE = "accounts.dat";
    private static final String TRANSACTIONS_FILE = "transactions.dat";
    
    private Map<String, Account> accounts;
    private Map<String, List<Transaction>> transactions;
    
    private static volatile AccountRepository instance;
    
    private AccountRepository() {
        loadData();
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        Map<String, Account> loadedAccounts = FileUtil.loadFromFile(ACCOUNTS_FILE, new ConcurrentHashMap<String, Account>());
        this.accounts = new ConcurrentHashMap<>(loadedAccounts);
        
        Map<String, List<Transaction>> loadedTransactions =
            FileUtil.loadFromFile(TRANSACTIONS_FILE, new ConcurrentHashMap<String, List<Transaction>>());
        this.transactions = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : loadedTransactions.entrySet()) {
            this.transactions.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }
    
    private void saveAccounts() {
        FileUtil.saveToFile(ACCOUNTS_FILE, new HashMap<>(accounts));
    }
    
    private void saveTransactions() {
        Map<String, List<Transaction>> transactionsToSave = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : transactions.entrySet()) {
            transactionsToSave.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        FileUtil.saveToFile(TRANSACTIONS_FILE, transactionsToSave);
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
        saveAccounts();
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
    

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        String accountNumber = transaction.getAccountNumber();
        transactions.computeIfAbsent(accountNumber, k -> new ArrayList<>()).add(transaction);
        saveTransactions();
    }
    

    public List<Transaction> getTransactions(String accountNumber) {
        return new ArrayList<>(transactions.getOrDefault(accountNumber, Collections.emptyList()));
    }
    

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }
    

    public boolean delete(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        boolean removed = accounts.remove(accountNumber) != null;
        if (removed) {
            saveAccounts();
            if (transactions.remove(accountNumber) != null) {
                saveTransactions();
            }
        }
        return removed;
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
