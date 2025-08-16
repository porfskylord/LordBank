package enitiy;

public class Account {
    private int accountId;
    private String accountNumber;
    private String accountType;
    private double balance;

    public Account(int accountId, String accountNumber, String accountType, double balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
