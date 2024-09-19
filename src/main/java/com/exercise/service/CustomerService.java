package com.exercise.service;

import com.exercise.entity.MyCustomer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<MyCustomer> getAllCustomers();
    MyCustomer createCustomer(MyCustomer customer);
    Optional<MyCustomer> getCustomerByCif(long cif);
    MyCustomer updateCustomer(MyCustomer customer);
    List<MyCustomer> updateSalaries(List<Long> customerIds, String updateType, double amount);
    void processCustomerFile(MultipartFile file) throws Exception;
    void deleteCustomer(long cif);
    void deleteCustomers(List<Long> customerIds);
}