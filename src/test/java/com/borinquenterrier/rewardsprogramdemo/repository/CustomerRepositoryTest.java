package com.borinquenterrier.rewardsprogramdemo.repository;

import com.borinquenterrier.rewardsprogramdemo.model.Customer;
import com.borinquenterrier.rewardsprogramdemo.model.Purchase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    Faker faker = new Faker();

    @Test
    public void purchasesBetween() {
        Customer customer = new Customer(faker.name().name());

        LocalDate now = LocalDate.now();
        customer.getPurchases().add(new Purchase(new BigDecimal("1.0"), now, customer));

        LocalDate later = now.plusMonths(1);
        customer.getPurchases().add(new Purchase(new BigDecimal("2.0"), later, customer));

        customerRepository.save(customer);

        List<Customer> customerWithPurchasesBetween = customerRepository.findCustomerWithPurchasesBetween(now, later);
        assertEquals(1, customerWithPurchasesBetween.size());
        Set<Purchase> purchasesBetween =customerWithPurchasesBetween.get(0).getPurchases();
        assertEquals(2, purchasesBetween.size());



    }

}