package repository;

import entity.Customer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class CustomerRepository {
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    
    private static volatile CustomerRepository instance;
    
    private CustomerRepository() {
    }
    

    public static CustomerRepository getInstance() {
        if (instance == null) {
            synchronized (CustomerRepository.class) {
                if (instance == null) {
                    instance = new CustomerRepository();
                }
            }
        }
        return instance;
    }
    

    public Customer save(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        customers.put(customer.getCustomerId(), customer);
        return customer;
    }
    

    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
    

    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }
    

    public List<Customer> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchTerm = name.toLowerCase();
        return customers.values().stream()
            .filter(c -> c.getName().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
    }
    

    public boolean existsById(String customerId) {
        return customers.containsKey(customerId);
    }
    

    public boolean deleteById(String customerId) {
        return customers.remove(customerId) != null;
    }
    

    public long count() {
        return customers.size();
    }
    

    public void deleteAll() {
        customers.clear();
    }
    

    public String generateCustomerId() {
        return "CUST" + String.format("%04d", customers.size() + 1);
    }
}
