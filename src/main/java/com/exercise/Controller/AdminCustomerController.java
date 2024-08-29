package com.exercise.Controller;

import com.exercise.Model.MyCustomer;
import com.exercise.Model.MyCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminCustomerController {

    @Autowired
    private MyCustomerService myCustomerService;

    @GetMapping("/customer-management")
    public String handleAdminCustomerManagement(Model model) {
        List<MyCustomer> customers = myCustomerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin-customer-management";
    }

    @PostMapping("/add-customer")
    @ResponseBody
    public ResponseEntity<MyCustomer> createCustomer(@RequestBody MyCustomer customer) {
        MyCustomer createdCustomer = myCustomerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    @ResponseBody
    public ResponseEntity<List<MyCustomer>> getAllCustomers() {
        List<MyCustomer> customers = myCustomerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<MyCustomer> getCustomerByCif(@PathVariable long cif) {
        Optional<MyCustomer> customer = myCustomerService.getCustomerByCif(cif);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<MyCustomer> updateCustomer(@PathVariable long cif, @RequestBody MyCustomer customer) {
        if (cif != customer.getCif()) {
            return ResponseEntity.badRequest().build();
        }
        MyCustomer updatedCustomer = myCustomerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PostMapping("/mass-salary-update")
    @ResponseBody
    public ResponseEntity<List<MyCustomer>> massSalaryUpdate(@RequestBody Map<String, Object> request) {
        List<Long> customerIds = ((List<String>) request.get("customerIds")).stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        String updateType = (String) request.get("updateType");
        double amount = ((Number) request.get("amount")).doubleValue();

        List<MyCustomer> updatedCustomers = myCustomerService.updateSalaries(customerIds, updateType, amount);
        return ResponseEntity.ok(updatedCustomers);
    }

    @DeleteMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<Void> deleteCustomer(@PathVariable long cif) {
        myCustomerService.deleteCustomer(cif);
        return ResponseEntity.noContent().build();
    }
}