package com.tnob;

import com.tnob.domain.Employee;
import com.tnob.repository.EmployeeRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

/**
 * Created by tahmid on 1/28/15.
 */

@Configuration
@EnableNeo4jRepositories(basePackages = "com.tnob.repository")
public class Neo4jSpringDataAppMain extends Neo4jConfiguration implements CommandLineRunner{

    public static final String REST_URI = "http://localhost:7474/db/data";

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase(REST_URI);
    }

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    GraphDatabase graphDatabase;

    public Neo4jSpringDataAppMain() {
        setBasePackage("com.tnob");
    }

    public static void main(String[] args) {
        SpringApplication.run(Neo4jSpringDataAppMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Employee greg = new Employee("Greg");
        Employee roy = new Employee("Roy");
        Employee craig = new Employee("Craig");

        System.out.println("Before linking up with Neo4j...");
        for (Employee person : new Employee[]{greg, roy, craig}) {
            System.out.println(person);
        }

        Transaction tx = graphDatabase.beginTx();
        try {
            employeeRepository.save(greg);
            employeeRepository.save(roy);
            employeeRepository.save(craig);

            greg = employeeRepository.findByName(greg.name);
            greg.worksWith(roy);
            greg.worksWith(craig);
            employeeRepository.save(greg);

            roy = employeeRepository.findByName(roy.name);
            roy.worksWith(craig);
            // We already know that roy works with greg
            employeeRepository.save(roy);

            // We already know craig works with roy and greg

            System.out.println("Lookup each person by name...");
            for (String name: new String[]{greg.name, roy.name, craig.name}) {
                System.out.println(employeeRepository.findByName(name));
            }

            System.out.println("Looking up who works with Greg...");
            for (Employee employee : employeeRepository.findByCoWorkersName("Greg")) {
                System.out.println(employee.name + " works with Greg.");
            }

            tx.success();
        } finally {
            tx.close();
        }


    }


}
