package com.exercise.Service;

import com.exercise.Entity.Gender;
import com.exercise.Entity.MyCustomer;
import com.exercise.Repository.MyCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
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

    public void deleteCustomers(List<Long> customerIds) {
        customerIds.forEach(this::deleteCustomer);
    }

    public void processCustomerFile(MultipartFile file) throws Exception {
        List<MyCustomer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                MyCustomer customer = new MyCustomer();
                customer.setEmpNo(Long.parseLong(fields[0]));
                customer.setName(fields[1]);
                customer.setEmail(fields[2]);
                customer.setPermAddress(fields[3]);
                customer.setTempAddress(fields[4]);
                customer.setPhone(fields[5]);
                customer.setBirthday(LocalDate.parse(fields[6]));
                customer.setBirthPlace(fields[7]);
                customer.setGender(Gender.valueOf(fields[8]));
                customer.setSalary(Long.parseLong(fields[9]));
                customers.add(customer);
            }
        }

        // Save all customers
        myCustomerRepository.saveAll(customers);
    }
}
