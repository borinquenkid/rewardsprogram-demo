package com.borinquenterrier.rewardsprogramdemo.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTest {

    @Test
    void calculateRewardPoints_noPurchases() {
        Customer customer = new Customer();
//        LocalDate now = LocalDate.now();
//        customer.getPurchases().add(new Purchase(new BigDecimal("1.0"), now, customer));
        int rewardPoints = customer.calculateRewardPoints();
        assertEquals(0, rewardPoints);
    }

    @ParameterizedTest(name = "work {0} days, expect {1} vacation days")
    @CsvSource(textBlock = """
               1,  0
              50,  0
              51,  1
              99, 49
             100, 50
             101, 52
            """
    )
    void calculateRewardPoints_purchases(int purchaseAmount, int rewardPoints) {
        Customer customer = new Customer();
        LocalDate now = LocalDate.now();
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(new Purchase(new BigDecimal(purchaseAmount), now, customer));
        assertEquals(rewardPoints, customer.calculateRewardPoints(purchases));
    }


    @Test
    void calculateRewardPoints_purchases_Permonth() {
        Customer customer = new Customer();
        LocalDate now = LocalDate.now();
        LocalDate later = now.plusMonths(1);
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(new Purchase(new BigDecimal(100), now, customer));
        purchases.add(new Purchase(new BigDecimal(50), later, customer));
        Map<String, Integer> stringIntegerMap = customer.calculateRewardPointsPerMonth(purchases);
        assertEquals(50,stringIntegerMap.get(YearMonth.from(now).toString()));
        assertEquals(0,stringIntegerMap.get(YearMonth.from(later).toString()));
    }

}