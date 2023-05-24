package com.borinquenterrier.rewardsprogramdemo.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "PURCHASES")
public class Purchase {

    public Purchase() {

    }

    public Purchase(BigDecimal purchaseAmount,
                    LocalDate purchaseDate,
                    Customer customer) {

        this.purchaseAmount = purchaseAmount;
        this.purchaseDate = purchaseDate;
        this.customer = customer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PURCHASE_AMOUNT", precision = 10, scale = 2)
    private BigDecimal purchaseAmount;

    @Column(name = "PURCHASE_DATE", columnDefinition = "DATE")
    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }


    public String getPurchaseDateString() {
        return YearMonth.from(purchaseDate).toString();
    }

    public int rewardPoints() {
        if (purchaseAmount.floatValue() > 100f) {
            return 50 + purchaseAmount.subtract(new BigDecimal(100), MathContext.DECIMAL32).intValue() * 2;
        } else if (purchaseAmount.floatValue() > 50f) {
            return purchaseAmount.subtract(new BigDecimal(50), MathContext.DECIMAL32).intValue();
        } else {
            return 0;
        }
    }

    public Long getId() {
        return id;
    }
}
