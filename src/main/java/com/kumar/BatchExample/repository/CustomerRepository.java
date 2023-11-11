package com.kumar.BatchExample.repository;


import com.kumar.BatchExample.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
}