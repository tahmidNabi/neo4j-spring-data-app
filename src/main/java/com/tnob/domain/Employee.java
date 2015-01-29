package com.tnob.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tahmid on 1/28/15.
 */


@NodeEntity
public class Employee {

    @GraphId
    public Long id;

    public String name;

    @RelatedTo(type = "Coworkers", direction = Direction.BOTH)
    public @Fetch Set<Employee> coWorkers;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void worksWith(Employee employee) {
        if (coWorkers == null) {
            coWorkers = new HashSet<Employee>();
        }
        coWorkers.add(employee);
    }

    public String toString() {
        String results = name + "'s teammates include\n";
        if (coWorkers != null) {
            for (Employee employee : coWorkers) {
                results += "\t- " + employee.name + "\n";
            }
        }
        return results;
    }
}
