package entity;

import entity.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer extends Person {
    private String customerId;
    private String email;
    private String phoneNumber;
    private LocalDate joinDate;
    private List<Account> accounts;

    public Customer() {
        this.accounts = new ArrayList<>();
        this.joinDate = LocalDate.now();
    }

    public Customer(String customerId, String name, int age, Gender gender,
                   String address, String email, String phoneNumber) {
        super(name, age, gender, address);
        this.customerId = customerId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.joinDate = LocalDate.now();
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void addAccount(Account account) {
        if (account != null) {
            this.accounts.add(account);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) &&
               Objects.equals(email, customer.email) &&
               Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId, email, phoneNumber);
    }

    @Override
    public String toString() {
        return String.format("Customer{%s, customerId='%s', email='%s', phoneNumber='%s', joinDate=%s}",
                super.toString().replace("Person", ""),
                customerId, email, phoneNumber, joinDate);
    }

    @Override
    public Customer clone() {
        return (Customer) super.clone();
    }
}
