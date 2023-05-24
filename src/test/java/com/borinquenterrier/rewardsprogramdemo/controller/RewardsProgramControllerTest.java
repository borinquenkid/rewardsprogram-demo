package com.borinquenterrier.rewardsprogramdemo.controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardsProgramControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void month() {
        JsonPath jsonPath = get("/api/month/" + LocalDate.now().plusMonths(2).toString())
                .then()
                .contentType(JSON)
                .statusCode(200)
                .extract().jsonPath();

        Map<String, Map<String, Integer>> rewardPoints = jsonPath.get("$");
        rewardPoints.forEach((key, value) -> {
            assertTrue(value.containsValue(0));
            assertTrue(value.containsValue(1));
            assertTrue(value.containsValue(52));
        });
    }

    @Test
    void total() {
        JsonPath jsonPath = get("/api/total")
                .then()
                .contentType(JSON)
                .statusCode(200)
                .extract().jsonPath();

        Map<String, Integer> rewardPoints = jsonPath.get("$");

        rewardPoints.forEach((key, value) -> assertEquals(53, (int) value));
    }



}