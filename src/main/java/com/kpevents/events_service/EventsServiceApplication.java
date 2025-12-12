package com.kpevents.events_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        tags = {
                @Tag(name="User Module", description = "Operations with users data"),
                @Tag(name="Member Module", description = "Operations with committee members data"),
                @Tag(name="Txn Module", description = "Operations with transactions data"),
                @Tag(name="Donation Module", description = "Operations with donations data"),
                @Tag(name="Expense Module", description = "Operations with expenses data"),
                @Tag(name="Annadaan Module", description = "Operations with annadaan data"),
                @Tag(name="Temple Module", description = "Operations with temple data"),
        }
)

@SpringBootApplication
public class EventsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsServiceApplication.class, args);
	}

}
