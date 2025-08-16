package entity;

import entity.enums.AccountType;
import entity.enums.TransactionType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract sealed class Account implements Cloneable 
    permits SavingsAccount, CurrentAccount {
    
    public static final double MIN_BALANCE = 1000.0;
    public static final String BANK_CODE = "LORD";
    private static int accountCounter = 1000;
    
    private final String accountNumber;
    private double balance;
    private final String customerId;
    private final LocalDate createdDate;
    private final List<Transaction> transactions;
    private DebitCard debitCard;
    
    public static final class BankRules {
        public static final double MIN_SAVINGS_BALANCE = 1000.0;
        public static final double MIN_CURRENT_BALANCE = 5000.0;
        public static final double SAVINGS_INTEREST_RATE = 0.04;
        public static final double OVERDRAFT_LIMIT = 10000.0;
        
        private BankRules() {}
    }
    
    public class DebitCard {
        private final String cardNumber;
        private final LocalDate expiryDate;
        private final int cvv;
        
        public DebitCard() {
            this.cardNumber = String.format("%016d", (long) (Math.random() * 1_0000_0000_0000_0000L));
            this.expiryDate = LocalDate.now().plusYears(5);
            this.cvv = (int) (Math.random() * 900) + 100;
        }
        
        public String getCardNumber() {
            return cardNumber;
        }
        
        public LocalDate getExpiryDate() {
            return expiryDate;
        }
        
        public int getCvv() {
            return cvv;
        }
        
        @Override
        public String toString() {
            return String.format("Card: %s (Exp: %s, CVV: %03d)", 
                cardNumber.replaceAll("(.{4})(?=.)", "$1 ").trim(), 
                expiryDate.format(java.time.format.DateTimeFormatter.ofPattern("MM/yy")), 
                cvv);
        }
    }
    
    protected Account(String customerId, double initialDeposit) {
        if (initialDeposit < getMinBalance()) {
            throw new IllegalArgumentException("Initial deposit must be at least " + getMinBalance());
        }
        
        this.accountNumber = generateAccountNumber();
        this.balance = initialDeposit;
        this.customerId = customerId;
        this.createdDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        this.debitCard = new DebitCard();
        
        recordTransaction(TransactionType.OPENING_BALANCE, initialDeposit);
    }
    
    public abstract void applyInterest();
    public abstract double calculateInterest();
    public abstract AccountType getAccountType();
    protected abstract double getMinBalance();
    
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        recordTransaction(TransactionType.DEPOSIT, amount);
    }
    
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (balance - amount < getMinBalance()) {
            throw new IllegalStateException("Insufficient funds");
        }
        balance -= amount;
        recordTransaction(TransactionType.WITHDRAWAL, -amount);
    }
    
    public void transfer(Account toAccount, double amount) {
        if (toAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null");
        }
        if (this.equals(toAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        this.withdraw(amount);
        toAccount.deposit(amount);
        
        recordTransaction(TransactionType.TRANSFER_OUT, -amount);
        toAccount.recordTransaction(TransactionType.TRANSFER_IN, amount);
    }
    
    private String generateAccountNumber() {
        return String.format("%s%04d", BANK_CODE, ++accountCounter);
    }
    
    protected void recordTransaction(TransactionType type, double amount) {
        transactions.add(new Transaction(UUID.randomUUID().toString(), 
                                       accountNumber, type, amount));
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return balance >= 0 || 
               (this instanceof CurrentAccount && 
                balance >= -((CurrentAccount) this).getOverdraftLimit());
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public DebitCard getDebitCard() {
        return debitCard;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
    
    @Override
    public String toString() {
        return String.format("%s{accountNumber='%s', balance=%.2f, customerId='%s', createdDate=%s, type=%s}",
                getClass().getSimpleName(), accountNumber, balance, customerId, createdDate, getAccountType());
    }
    
    @Override
    public Account clone() {
        try {
            return (Account) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

}
