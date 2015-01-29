package com.tnob.repository;

import com.tnob.domain.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tahmid on 1/28/15.
 */


public interface EmployeeRepository extends CrudRepository<Employee, String> {

    Employee findByName(String name);

    Iterable<Employee> findByCoWorkersName(String name);
}
