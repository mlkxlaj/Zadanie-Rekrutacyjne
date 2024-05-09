package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FindPersonTest {
    private EmployeesService employeesService;

    @BeforeEach
    void setUp() {
        employeesService = new EmployeesService();
        Person person1 = new Person(Type.INTERNAL, "1", "Mikolaj", "Williams", "+2345678901", "52022114478", "sarah.williams@example.com");
        Person person2 = new Person(Type.EXTERNAL, "2", "Mikolaj", "Smith", "+3456789012", "80042448774", "alice.smith@example.com");
        EmployeesService.employeesMap.put(person1, "92731.xml");
        EmployeesService.employeesMap.put(person2, "59083.xml");
    }

    @AfterEach
    public void tearDown() {
        EmployeesService.employeesMap.clear();
    }

    @Test
    public void testFindPersonByType() {
        List<Person> result = employeesService.findPerson(Type.INTERNAL, null, null, null, null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonById() {
        List<Person> result = employeesService.findPerson(null, "2", null, null, null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonByFirstName() {
        List<Person> result = employeesService.findPerson(null, null, "Mikolaj", null, null, null, null);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindPersonByLastName() {
        List<Person> result = employeesService.findPerson(null, null, null, "Smith", null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonByMobile() {
        List<Person> result = employeesService.findPerson(null, null, null, null, "+2345678901", null, null);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonByPesel() {
        List<Person> result = employeesService.findPerson(null, null, null, null, null, "80042448774", null);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonByEmail() {
        List<Person> result = employeesService.findPerson(null, null, null, null, null, null, "sarah.williams@example.com");
        assertEquals(1, result.size());
    }

    @Test
    public void testFindPersonWhenAllNulls() {
        assertThrows(IllegalArgumentException.class, () -> employeesService.findPerson(
                null, null, null, null, null, null, null
        ));
    }

    @Test
    public void testFindPersonWhenNoParameters() {
        assertThrows(IllegalArgumentException.class, () -> employeesService.findPerson(
                null, "", "", "", "", "", ""
        ));
    }
}
