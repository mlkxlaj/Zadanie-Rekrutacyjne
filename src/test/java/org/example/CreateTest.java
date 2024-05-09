package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static org.example.EmployeesService.employeesMap;
import static org.junit.jupiter.api.Assertions.*;

class CreateTest {
    private EmployeesService employeesService;
    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        employeesService = new EmployeesService();
        person1 = new Person(Type.INTERNAL,"1","Sarah", "Williams", "+2345678901", "52022114478", "sarah.williams@example.com");
        person2 = new Person(Type.EXTERNAL, "2","Mikolaj", "Smith", "+3456789012", "80042448774", "alice.smith@example.com");
    }

    @AfterEach
    public void tearDown() {
        Optional<String> highestId = employeesMap.keySet().stream()
                .map(Person::getPersonId)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(Integer::parseInt));
        highestId.ifPresent(s -> employeesService.remove(s));
        employeesMap.clear();
    }

    @Test
    public void testCreatePerson() throws ParserConfigurationException, IOException, TransformerException {
        EmployeesService.employeesMap.put(person1, "92731.xml");
        EmployeesService.employeesMap.put(person2, "59083.xml");
        Person person = new Person(Type.INTERNAL,"3", "Mikolaj", "Kowaszewicz", "+123 821382183", "97031003029", "jetbrains@support.com");
        assertTrue(employeesService.create(person));
        assertThrows(IllegalArgumentException.class, () -> employeesService.create(person));
    }

    @Test
    public void testCreatePersonWithExistingPersonId() {
        EmployeesService.employeesMap.put(person1, "92731.xml");
        Person personWithExistingId = new Person(Type.INTERNAL, "1", "John", "Doe", "+1234567890", "82101334767", "john.doe@example.com");
        assertThrows(IllegalArgumentException.class, () -> employeesService.create(personWithExistingId));
    }
}
