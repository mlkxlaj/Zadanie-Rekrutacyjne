package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveTest {
    private EmployeesService employeesService;
    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        employeesService = new EmployeesService();
        person1 = new Person(Type.INTERNAL, "1", "Sarah", "Williams", "+2345678901", "52022114478", "sarah.williams@example.com");
        person2 = new Person(Type.EXTERNAL, "2", "Mikolaj", "Smith", "+3456789012", "80042448774", "alice.smith@example.com");
    }

    @AfterEach
    void tearDown() {
        EmployeesService.employeesMap.clear();
    }

    @Test
    void testRemoveExistingPerson() throws Exception {
        employeesService.create(person1);
        assertTrue(employeesService.remove(person1.getPersonId()));
        assertFalse(EmployeesService.employeesMap.containsKey(person1));
    }

    @Test
    void testRemoveNonExistingPerson() {
        assertFalse(employeesService.remove(person2.getPersonId()));
    }

    @Test
    void testRemovePersonWhenMapIsEmpty() {
        assertFalse(employeesService.remove(person1.getPersonId()));
    }

    @Test
    void testRemoveNullPersonId() {
        assertThrows(IllegalArgumentException.class, () -> employeesService.remove(null));
    }

    @Test
    void testRemoveEmptyPersonId() {
        assertThrows(IllegalArgumentException.class, () -> employeesService.remove(""));
    }
}