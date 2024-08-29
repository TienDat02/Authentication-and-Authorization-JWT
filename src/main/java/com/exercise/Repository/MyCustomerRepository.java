package com.exercise.Repository;

import com.exercise.Entity.Gender;
import com.exercise.Entity.MyCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MyCustomerRepository extends JpaRepository<MyCustomer, Long> {
    Optional<MyCustomer> findByCif(long cif);
    List<MyCustomer> findByEmpNo(long empNo);
    List<MyCustomer> findByName(String name);
    List<MyCustomer> findByEmail(String email);
    List<MyCustomer> findByPhone(String phone);
    List<MyCustomer> findByBirthday(LocalDate birthday);
    List<MyCustomer> findByBirthPlace(String birthPlace);
    List<MyCustomer> findByGender(Gender gender);
    List<MyCustomer> findBySalary(long salary);
}
