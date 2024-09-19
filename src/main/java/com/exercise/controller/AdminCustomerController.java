package com.exercise.controller;

import com.exercise.dto.response.ApiResponse;
import com.exercise.entity.MyCustomer;
import com.exercise.exception.ErrorCode;
import com.exercise.exception.SalaryException;
import com.exercise.service.CustomerService;
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

import static com.exercise.exception.ErrorCode.INVALID_DATA_FORMAT;
import static com.exercise.exception.ErrorCode.UNEXPECTED_ERROR;

@Controller
@RequestMapping("/admin")
public class AdminCustomerController {

    private final CustomerService customerService;

    @Autowired
    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer-management")
    public String handleAdminCustomerManagement(Model model) {
        List<MyCustomer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "admin-customer-management";
    }

    @PostMapping("/add-customer")
    @ResponseBody
    public ResponseEntity<MyCustomer> createCustomer(@RequestBody MyCustomer customer) {
        MyCustomer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    @ResponseBody
    public ResponseEntity<List<MyCustomer>> getAllCustomers() {
        List<MyCustomer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<MyCustomer> getCustomerByCif(@PathVariable long cif) {
        Optional<MyCustomer> customer = customerService.getCustomerByCif(cif);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<MyCustomer> updateCustomer(@PathVariable long cif, @RequestBody MyCustomer customer) {
        if (cif != customer.getCif()) {
            return ResponseEntity.badRequest().build();
        }
        MyCustomer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PostMapping("/mass-salary-update")
    @ResponseBody
    public ResponseEntity<ApiResponse> massSalaryUpdate(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> customerIds = ((List<String>) request.get("customerIds")).stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            String updateType = (String) request.get("updateType");
            Object amountObj = request.get("amount");
            double amount;
            // Kiểm tra và chuyển đổi kiểu dữ liệu của amountObj
            if (amountObj instanceof Number) {
                amount = ((Number) amountObj).doubleValue();
            } else {
                throw new SalaryException(ErrorCode.INVALID_DATA_FORMAT);
            }

            List<MyCustomer> updatedCustomers = customerService.updateSalaries(customerIds, updateType, amount);
            return ResponseEntity.ok(new ApiResponse(1000, "success", updatedCustomers));

        } catch (SalaryException e) {
            return ResponseEntity.status(e.getErrorCode().getStatusCode())
                    .body(new ApiResponse(e.getErrorCode().getCode(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                    .body(new ApiResponse(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode(), e.getMessage(), null));
        }
    }

    @DeleteMapping("/customers/{cif}")
    @ResponseBody
    public ResponseEntity<Void> deleteCustomer(@PathVariable long cif) {
        customerService.deleteCustomer(cif);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/customers/filter")
    @ResponseBody
    public ResponseEntity<List<MyCustomer>> filterCustomersBySalary(@RequestParam double minSalary, @RequestParam double maxSalary) {
        List<MyCustomer> customers = customerService.getAllCustomers().stream()
                .filter(customer -> customer.getSalary() >= minSalary && customer.getSalary() <= maxSalary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }
    @DeleteMapping("/customers")
    @ResponseBody
    public ResponseEntity<Void> deleteCustomers(@RequestBody Map<String, List<Long>> request) {
        List<Long> customerIds = request.get("customerIds");
        customerService.deleteCustomers(customerIds);
        return ResponseEntity.noContent().build();
    }
}