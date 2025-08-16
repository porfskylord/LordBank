package entity.enums;

public enum TransactionType {
    OPENING_BALANCE("Account Opening"),
    CLOSING_BALANCE("Account Closing"),
    
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER_IN("Transfer Received"),
    TRANSFER_OUT("Transfer Sent"),
    
    INTEREST("Interest Credit"),
    FEE("Service Fee"),
    OVERDRAFT_FEE("Overdraft Fee"),
    
    LOAN_DISBURSEMENT("Loan Disbursement"),
    LOAN_REPAYMENT("Loan Repayment"),
    
    ADJUSTMENT("Balance Adjustment"),
    REVERSAL("Transaction Reversal");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
