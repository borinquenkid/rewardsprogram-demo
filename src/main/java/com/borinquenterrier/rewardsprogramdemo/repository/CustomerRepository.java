package com.borinquenterrier.rewardsprogramdemo.repository;

import com.borinquenterrier.rewardsprogramdemo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    @Query("select distinct c from Customer c inner join fetch c.purchases p where p.purchaseDate between :startDate and :endDate")
    List<Customer> findCustomerWithPurchasesBetween(LocalDate startDate, LocalDate endDate );
}
