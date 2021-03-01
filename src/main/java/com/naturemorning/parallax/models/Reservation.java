/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturemorning.parallax.models;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)

    private long id;
    private String customerFirstName;
    private String customerLastName;
    private String email;
    private String guestNo;
    private LocalDate dob;
    private LocalDate rdob;
    private String time;
    private String phoneNo;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getcustomerFirstName() {
        return customerFirstName;
    }

    public void setcustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getcustomerLastName() {
        return customerLastName;
    }

    public void setcustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuestNo() {
        return guestNo;
    }

    public void setGuestNo(String guestNo) {
        this.guestNo = guestNo;
    }
    
     public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getRDob() {
        return rdob;
    }

    public void setRDob(LocalDate rdob) {
        this.rdob = rdob;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", customerFirstName=" + customerFirstName + ", customerLastName="
                + customerLastName + ", email=" + email + ", guestNo=" + guestNo + ", phoneNo=" + phoneNo + ", dob=" 
                + dob + ", rdob=" + rdob + ", time=" + time + "]";
    }
}
