package com.exercise.Controller;

import com.exercise.Model.MyCustomer;
import com.exercise.Model.MyCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserCustomerController {

    @Autowired
    private MyCustomerService myCustomerService;

    @GetMapping("/home")
    @PreAuthorize("hasRole('USER')")
    public String handleUserHome() {
        return "user-home";
    }

    @GetMapping("/customer-management")
    @PreAuthorize("hasRole('USER')")
    public String handleUserCustomerManagement(Model model, Authentication authentication) {
        List<MyCustomer> customers = myCustomerService.getAllCustomers();
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
        List<MyCustomer> customers = myCustomerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_READ')")
    public ResponseEntity<MyCustomer> getUserCustomerByCif(@PathVariable long cif) {
        Optional<MyCustomer> customer = myCustomerService.getCustomerByCif(cif);
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add-customer")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_CREATE')")
    public ResponseEntity<MyCustomer> createUserCustomer(@RequestBody MyCustomer customer) {
        MyCustomer createdCustomer = myCustomerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_UPDATE')")
    public ResponseEntity<MyCustomer> updateUserCustomer(@PathVariable long cif, @RequestBody MyCustomer customer) {
        if (cif != customer.getCif()) {
            return ResponseEntity.badRequest().build();
        }
        MyCustomer updatedCustomer = myCustomerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/customers/{cif}")
    @ResponseBody
    @PreAuthorize("hasRole('USER') and hasAuthority('PERMISSION_DELETE')")
    public ResponseEntity<Void> deleteUserCustomer(@PathVariable long cif) {
        if (cif == 0) {
            return ResponseEntity.badRequest().build();
        }
        myCustomerService.deleteCustomer(cif);
        return ResponseEntity.noContent().build();
    }
}