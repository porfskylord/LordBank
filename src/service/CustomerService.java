package service;

import entity.Customer;
import entity.enums.Gender;
import repository.CustomerRepository;
import util.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class CustomerService {
    private final CustomerRepository customerRepository;
    
    public CustomerService() {
        this.customerRepository = CustomerRepository.getInstance();
    }
    

    public Customer createCustomer(String name, int age, Gender gender, 
                                 String address, String email, String phoneNumber) {

        if (!ValidationUtils.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name");
        }
        if (age < 18 || age > 120) {
            throw new IllegalArgumentException("Age must be between 18 and 120");
        }
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtils.isValidPhone(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        
        String customerId = customerRepository.generateCustomerId();
        
        Customer customer = new Customer(customerId, name, age, gender, address,
                                       email, phoneNumber);
        
        return customerRepository.save(customer);
    }
    

    public Optional<Customer> findCustomerById(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        return customerRepository.findById(customerId);
    }
    

    public List<Customer> findCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return customerRepository.findByName(name);
    }
    

    public Customer updateCustomer(String customerId, String name, String email, 
                                 String phoneNumber, String address) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        if (name != null && !name.isBlank()) {
            if (!ValidationUtils.isValidName(name)) {
                throw new IllegalArgumentException("Invalid name");
            }
            customer.setName(name);
        }
        
        if (email != null && !email.isBlank()) {
            if (!ValidationUtils.isValidEmail(email)) {
                throw new IllegalArgumentException("Invalid email format");
            }
            customer.setEmail(email);
        }
        
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            if (!ValidationUtils.isValidPhone(phoneNumber)) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
            customer.setPhoneNumber(phoneNumber);
        }
        
        if (address != null && !address.isBlank()) {
            customer.setAddress(address);
        }
        
        return customerRepository.save(customer);
    }
    

    public boolean deleteCustomer(String customerId) {
        return customerRepository.delete(customerId);
    }
    

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    

    public boolean customerExists(String customerId) {
        return customerRepository.existsById(customerId);
    }
}
