package entity;

import entity.enums.AccountType;
import entity.enums.TransactionType;

import java.io.Serializable;

public final class SavingsAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.04;
    private static final double MIN_BALANCE = 1000.0;

    public SavingsAccount(String customerId, double initialDeposit) {
        super(customerId, initialDeposit);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * (INTEREST_RATE / 12);
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    @Override
    protected double getMinBalance() {
        return MIN_BALANCE;
    }

    public double getInterestRate() {
        return INTEREST_RATE;
    }
    
    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            deposit(interest);
            recordTransaction(TransactionType.INTEREST, interest);
        }
    }
    
    @Deprecated
    public void applyMonthlyInterest() {
        applyInterest();
    }

    @Override
    public String toString() {
        return String.format("SavingsAccount{%s, interestRate=%.2f%%}",
                super.toString().replace("Account", ""), INTEREST_RATE * 100);
    }
}
