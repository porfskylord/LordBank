package entity;

import entity.enums.TransactionType;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Transaction {
    private final String transactionId;
    private final String accountNumber;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(String transactionId, String accountNumber, 
                      TransactionType type, double amount, String description) {
        this.transactionId = Objects.requireNonNull(transactionId, "Transaction ID cannot be null");
        this.accountNumber = Objects.requireNonNull(accountNumber, "Account number cannot be null");
        this.type = Objects.requireNonNull(type, "Transaction type cannot be null");
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description != null ? description : "";
    }
    
    public Transaction(String transactionId, String accountNumber, 
                      TransactionType type, double amount) {
        this(transactionId, accountNumber, type, amount, "");
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    

    public double getBalanceAfter(double previousBalance) {
        switch (type) {
            case DEPOSIT:
            case TRANSFER_IN:
            case INTEREST:
                return previousBalance + amount;
            case WITHDRAWAL:
            case TRANSFER_OUT:
            case FEE:
                return previousBalance - amount;
            case OPENING_BALANCE:
                return amount;
            default:
                return previousBalance;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 &&
               transactionId.equals(that.transactionId) &&
               accountNumber.equals(that.accountNumber) &&
               type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, accountNumber, type, amount);
    }

    @Override
    public String toString() {
        return String.format("Transaction{id='%s', account='%s', type=%s, amount=%.2f, timestamp=%s}",
                transactionId, accountNumber, type, amount, 
                timestamp.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
