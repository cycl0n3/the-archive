package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.records.CustomerRecord;
import jooq.generated.tables.records.EmployeeRecord;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.OrderdetailRecord;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClassicModelsService classicModelsService;

    public MainApplication(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch employees from a certain office (4) with customers, orders and orders details:");
            List<Tuple2<EmployeeRecord, List<Tuple2<CustomerRecord, 
            List<Tuple2<OrderRecord, List<Tuple2<OrderdetailRecord, ProductRecord>>>>>>>> result 
                    = classicModelsService.fetchEmployeeWithCustomersOrdersByOfficeCode("4");
            System.out.println(result);
        };
    }
}
