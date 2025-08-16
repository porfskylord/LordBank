package entity;

import entity.enums.AccountType;

import static entity.Account.BankRules.OVERDRAFT_LIMIT;

public final class CurrentAccount extends Account {
    private double overdraftLimit = 10000.0;
    private static final double MIN_BALANCE = 5000.0;
    private static final double OVERDRAFT_INTEREST_RATE = 0.15;
    
    public CurrentAccount(String customerId, double initialDeposit) {
        super(customerId, initialDeposit);
    }

    @Override
    public void applyInterest() {
    }

    public void setOverdraftLimit(double overdraftLimit) {
        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }
    
    @Override
    public double calculateInterest() {
        double balance = getBalance();
        if (balance < 0) {
            return -balance * (OVERDRAFT_INTEREST_RATE / 12);
        }
        return 0;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        double availableBalance = getBalance() + OVERDRAFT_LIMIT;
        if (amount > availableBalance) {
            throw new IllegalStateException(String.format(
                "Withdrawal amount exceeds available balance and overdraft limit. Available: %.2f", 
                availableBalance));
        }
        
        super.withdraw(amount);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }

    @Override
    protected double getMinBalance() {
        return MIN_BALANCE;
    }
    
    public double getOverdraftLimit() {
        return overdraftLimit;
    }
    
    public double getAvailableBalance() {
        return getBalance() + OVERDRAFT_LIMIT;
    }

    @Override
    public String toString() {
        return String.format("CurrentAccount{%s, overdraftLimit=%.2f, availableBalance=%.2f}",
                super.toString().replace("Account", ""), 
                OVERDRAFT_LIMIT, 
                getAvailableBalance());
    }
}
