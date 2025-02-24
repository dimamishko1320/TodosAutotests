package com.todos.integration_tests.client;

import com.todos.integration_tests.config.AppProperties;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Base client for API requests.
 * Configures request specifications, including logging and Allure reporting.
 */
@Service
public class BaseClient {
    @Autowired
    protected AppProperties appProperties;

    protected RequestSpecification getRequestSpecification() {
        return RestAssured.with()
                .contentType(ContentType.JSON)
                .filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .filter(new AllureRestAssured());
    }
}
