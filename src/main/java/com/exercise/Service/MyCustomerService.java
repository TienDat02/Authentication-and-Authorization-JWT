package com.exercise.Service;

import com.exercise.Entity.MyCustomer;
import com.exercise.Repository.MyCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyCustomerService {
    @Autowired
    private MyCustomerRepository myCustomerRepository;

    public MyCustomerService(MyCustomerRepository myCustomerRepository) {
        this.myCustomerRepository = myCustomerRepository;
    }

    // Create
    public MyCustomer createCustomer(MyCustomer customer) {

        return myCustomerRepository.save(customer);
    }

    // Read
    public List<MyCustomer> getAllCustomers() {
        return myCustomerRepository.findAll();
    }

    public Optional<MyCustomer> getCustomerByCif(long cif) {
        return myCustomerRepository.findByCif(cif);
    }

    // Update
    public MyCustomer updateCustomer(MyCustomer customer) {
        return myCustomerRepository.save(customer);
    }

    // Delete
    public void deleteCustomer(long cif) {
        myCustomerRepository.deleteById(cif);
    }

    public List<MyCustomer> updateSalaries(List<Long> customerIds, String updateType, double amount) {
        List<MyCustomer> updatedCustomers = new ArrayList<>();
        for (Long cif : customerIds) {
            Optional<MyCustomer> customerOpt = getCustomerByCif(cif);
            if (customerOpt.isPresent()) {
                MyCustomer customer = customerOpt.get();
                double newSalary;
                if ("fixed".equals(updateType)) {
                    newSalary = amount;
                } else if ("raise".equals(updateType)) {
                    newSalary = customer.getSalary() * (1 + amount/100);
                } else if ("deduction".equals(updateType)) {
                    newSalary = customer.getSalary() * (1 - amount/100);
                } else {
                    throw new IllegalArgumentException("Invalid update type");
                }
                customer.setSalary((long) newSalary); // Cast double to long
                updatedCustomers.add(updateCustomer(customer));
            }
        }
        return updatedCustomers;
    }
}
