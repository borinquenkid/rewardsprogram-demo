package com.borinquenterrier.rewardsprogramdemo.model;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    public Customer() {

    }

    public Customer(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public Long getId() {
        return id;
    }

    @OneToMany(mappedBy = "customer",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Purchase> purchases = new HashSet<>();

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public int calculateRewardPoints() {
        return calculateRewardPoints(purchases);
    }

    public int calculateRewardPoints(Collection<Purchase> purchases) {
        return purchases.stream().map(Purchase::rewardPoints).reduce(0,Integer::sum);
    }

    public Map<String, Integer> calculateRewardPointsPerMonth() {
        return calculateRewardPointsPerMonth(purchases);
    }

    public Map<String, Integer> calculateRewardPointsPerMonth(Collection<Purchase> purchases) {
        Map<String, List<Purchase>> purchaseDateMap = purchases.stream()
                .collect(Collectors.groupingBy(Purchase::getPurchaseDateString));
        Map<String, Integer> rewardPointMap = purchaseDateMap
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                (entry) -> calculateRewardPoints(entry.getValue())

                        ));
        return rewardPointMap;
    }
}
