package com.exercise.controller;

import com.exercise.entity.MyCustomer;
import com.exercise.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserCustomerController {

    private final CustomerService customerService;

    public UserCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public String handleUserHome() {
        return "user-home";
    }

    @GetMapping("/customer-management")
    @PreAuthorize("hasRole('USER')")
    public String handleUserCustomerManagement(Model model, Authentication authentication) {
        List<MyCustomer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);

        // Add user permissions to the model
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            model.addAttribute("userPermissions", authorities);
        } else {
            model.addAttribute("userPermissions", Collections.emptyList());
        }

        return "user-customer-management";
    }

    @GetMapping("/customers")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_READ')")
    public ResponseEntity<List<MyCustomer>> getUserCustomers() {
        List<MyCustomer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_READ')")
    public ResponseEntity<MyCustomer> getUserCustomerByCif(@PathVariable long cif) {
        Optional<MyCustomer> customer = customerService.getCustomerByCif(cif);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add-customer")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<MyCustomer> createUserCustomer(@RequestBody MyCustomer customer) {
        MyCustomer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<MyCustomer> updateUserCustomer(@PathVariable long cif, @RequestBody MyCustomer customer) {
        if (cif != customer.getCif()) {
            return ResponseEntity.badRequest().build();
        }
        MyCustomer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Void> deleteUserCustomer(@PathVariable long cif) {
        if (cif == 0) {
            return ResponseEntity.badRequest().build();
        }
        customerService.deleteCustomer(cif);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/customers")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Void> deleteCustomers(@RequestBody Map<String, List<Long>> request) {
        List<Long> customerIds = request.get("customerIds");
        customerService.deleteCustomers(customerIds);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/mass-salary-update")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<List<MyCustomer>> massSalaryUpdate(@RequestBody Map<String, Object> request) {
        List<Long> customerIds = ((List<String>) request.get("customerIds")).stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        String updateType = (String) request.get("updateType");
        double amount = ((Number) request.get("amount")).doubleValue();

        List<MyCustomer> updatedCustomers = customerService.updateSalaries(customerIds, updateType, amount);
        return ResponseEntity.ok(updatedCustomers);
    }
}