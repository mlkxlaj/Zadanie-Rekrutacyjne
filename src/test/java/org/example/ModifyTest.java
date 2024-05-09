package org.example;

import org.example.dto.PersonDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ModifyTest {
    private EmployeesService employeesService;
    private DataService dataService;

    @BeforeEach
    void setUp() throws ParserConfigurationException, IOException, TransformerException {
        employeesService = new EmployeesService();
        employeesService.create(new Person(Type.INTERNAL, "Sarah", "Williams", "+2345678901", "52022114478", "sarah.williams@example.com"));
    }

    @AfterEach
    public void tearDown() {
        employeesService.remove("1");
    }

    @Test
    public void testModifyPerson() {
        PersonDTO person = new PersonDTO(null, "1", "UPDATED", "UPDATED", null, null, null);
        assertDoesNotThrow(() -> employeesService.modify(person));
    }

    @Test
    public void testModifyPersonWithInvalidData() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        PersonDTO person = new PersonDTO(null, "1", "", "", null, null, null);
        employeesService.modify(person);
        Person person1 = employeesService.findPerson(null, "1", null, null, null, null, null).get(0);
        assertEquals(person1.getFirstName(), "Sarah");
        assertEquals(person1.getLastName(), "Williams");
    }

    @Test
    public void testModifyNonExistentPerson() {
        PersonDTO person = new PersonDTO(null, "999", "UPDATED", "UPDATED", null, null, null);
        assertThrows(IllegalArgumentException.class, () -> employeesService.modify(person));
    }

    @Test
    public void testModifyPersonCheckReturn() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        PersonDTO person = new PersonDTO(null, "1", "UPDATED", "UPDATED", null, null, null);
        employeesService.modify(person);
        Person person1 = employeesService.findPerson(null, "1", null, null, null, null, null).get(0);

        assertNotEquals(person1.getFirstName(), "Sarah");
        assertNotEquals(person1.getLastName(), "Williams");
    }
}