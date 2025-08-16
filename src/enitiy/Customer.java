package enitiy;

import enitiy.enums.Gender;
import interfaces.LoanEligible;

import java.util.ArrayList;
import java.util.List;

public class Customer extends Person implements LoanEligible {
    private static int idCounter = 37566000;
    private int customerId;
    private List<Account> accounts;

    Customer(){
        super();
        this.customerId =++idCounter;
        this.accounts = new ArrayList<>();
    }

    Customer(String name, int age, Gender gender, String address, Account account){
        super(name, age, gender, address);
        this.customerId = ++idCounter;
        this.accounts.add(account);

    }

    public int getCustomerId() {
        return customerId;
    }

    public int getAccountCount(){
        return this.accounts.size();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public void removeAccount(Account account){
        this.accounts.remove(account);
    }

    public Account getAccountById(int accountId){
        for(Account account : this.accounts){
            if(account.getAccountId() == accountId){
                return account;
            }
        }
        return null;
    }

    public String showCustomerDetails(){
        return "Customer Id: "+this.customerId+" | Name: "+super.getName()+" | Age: "+super.getAge()+" | Gender: "+super.getGender()+" | Address: "+super.getAddress()+"\n";
    }

    @Override
    public boolean eligibleForLoan() {
        return false;
    }
}
