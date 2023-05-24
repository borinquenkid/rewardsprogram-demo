package com.borinquenterrier.rewardsprogramdemo.controller;

import com.borinquenterrier.rewardsprogramdemo.model.Customer;
import com.borinquenterrier.rewardsprogramdemo.model.Purchase;
import com.borinquenterrier.rewardsprogramdemo.repository.CustomerRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api")
public class RewardsProgramController {


    private final CustomerRepository customerRepository;

    @Autowired
    public RewardsProgramController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        intializeRepository();
    }

    //Two randomly named customers
    // One transaction per month
    // First no rewards
    // Second 1 rewards point
    // Third 52 reward points
    // Total 53 reward points

    private void intializeRepository() {
        Faker faker = new Faker();
        IntStream.rangeClosed(1,2).forEach(counter ->{
            Customer customer = new Customer(faker.name().name());

            LocalDate first = LocalDate.now();
            customer.getPurchases().add(new Purchase(new BigDecimal("1.0"), first, customer));

            LocalDate second = first.plusMonths(1);
            customer.getPurchases().add(new Purchase(new BigDecimal("51.0"), second, customer));

            LocalDate third = second.plusMonths(1);
            customer.getPurchases().add(new Purchase(new BigDecimal("101.0"), third, customer));
            customerRepository.save(customer);
        });

    }


    @GetMapping("/month/{endDate}")
    public Map<String, Map<String, Integer>> getCustomerRewardsPerMonth(@PathVariable("endDate")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                      LocalDate endDate) {
        LocalDate endDay = endDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate startDay = endDate.minusMonths(2).with(TemporalAdjusters.firstDayOfMonth());
        List<Customer> customerWithPurchasesBetween = customerRepository.findCustomerWithPurchasesBetween(startDay, endDay);
        return customerWithPurchasesBetween.stream().collect(
                Collectors.toMap(
                        Customer::getName,
                        Customer::calculateRewardPointsPerMonth
                )
        );
    }

    @GetMapping("/total")
    public Map<String, Integer> getCustomerRewards() {

        List<Customer> allCustomers = customerRepository.findAll();
        return allCustomers.stream().collect(
                 Collectors.toMap(
                         Customer::getName,
                         Customer::calculateRewardPoints
                 )
         );
    }
}

