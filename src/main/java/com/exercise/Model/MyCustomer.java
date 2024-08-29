package com.exercise.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;

@Entity
public class MyCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cif;

    private long empNo;
    private String name;
    private String email;
    private String permAddress;
    private String tempAddress;
    private String phone;
    private LocalDate birthday;
    private String birthPlace;
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;
    private long salary;

    public MyCustomer() {
    }
    public MyCustomer(long cif, long empNo, String name, String email, String permAddress, String tempAddress, String phone, LocalDate birthday, String birthPlace, Gender gender, long salary) {
        this.cif = cif;
        this.empNo = empNo;
        this.name = name;
        this.email = email;
        this.permAddress = permAddress;
        this.tempAddress = tempAddress;
        this.phone = phone;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.gender = gender;
        this.salary = salary;
    }

    public long getCif() {
        return cif;
    }

    public void setCif(long cif) {
        this.cif = cif;
    }

    public long getEmpNo() {
        return empNo;
    }

    public void setEmpNo(long empNo) {
        this.empNo = empNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermAddress() {
        return permAddress;
    }

    public void setPermAddress(String permAddress) {
        this.permAddress = permAddress;
    }

    public String getTempAddress() {
        return tempAddress;
    }

    public void setTempAddress(String tempAddress) {
        this.tempAddress = tempAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}