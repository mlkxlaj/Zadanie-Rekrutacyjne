package org.example;

import org.example.dto.PersonDTO;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeesService {
    public static Map<Person, String> employeesMap = new HashMap<>();
    private final DataService dataService;

    public EmployeesService() {
        this.dataService = new DataService();
    }

    public List<Person> findPerson(Type type, String personId, String firstName, String lastName, String mobile, String pesel, String email) {
        if (Stream.of(type, personId, firstName, lastName, mobile, pesel, email)
                .allMatch(s -> s == null || s.toString().trim().isEmpty())) {
            throw new IllegalArgumentException("At least one parameter must be non-null and not an empty string");
        }

        return employeesMap.keySet().stream()
                .filter(person -> type == null || person.getType().equals(type))
                .filter(person -> personId == null || person.getPersonId().equals(personId))
                .filter(person -> firstName == null || person.getFirstName().equals(firstName))
                .filter(person -> lastName == null || person.getLastName().equals(lastName))
                .filter(person -> mobile == null || person.getMobile().equals(mobile))
                .filter(person -> pesel == null || person.getPesel().equals(pesel))
                .filter(person -> email == null || person.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    public boolean create(Person person) throws ParserConfigurationException, TransformerException, IOException {
        if (exists(person)) {
            throw new IllegalArgumentException("main.java.Person with the same personId, mobile, pesel, and email already exists");
        }
        person.setPersonId(generateUniquePersonId());

        employeesMap.put(person, dataService.createAndSavePersonToXML(person, person.getType()));
        return true;
    }

    public boolean remove(String personId) {
        if (personId == null || personId.trim().isEmpty()) {
            throw new IllegalArgumentException("PersonId cannot be null or empty");
        }
        Person person = employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(personId))
                .findFirst()
                .orElse(null);

        if (person != null) {
            dataService.deleteFile(employeesMap.get(person), person.getType());
            employeesMap.remove(person);
            return true;
        }

        return false;
    }

    public void modify(PersonDTO personDTO) throws
            ParserConfigurationException, IOException, TransformerException, SAXException {
        employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(personDTO.personId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Person with the given ID does not exist"));

        employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(personDTO.personId()))
                .findFirst()
                .ifPresent(p -> {
                    if (personDTO.firstName() != null && !personDTO.firstName().trim().isEmpty()) {
                        p.setFirstName(personDTO.firstName());
                    }
                    if (personDTO.lastName() != null && !personDTO.lastName().trim().isEmpty()) {
                        p.setLastName(personDTO.lastName());
                    }
                    if (personDTO.mobile() != null && !personDTO.mobile().trim().isEmpty()) {
                        p.setMobile(personDTO.mobile());
                    }
                    if (personDTO.pesel() != null && !personDTO.pesel().trim().isEmpty()) {
                        p.setPesel(personDTO.pesel());
                    }
                    if (personDTO.email() != null && !personDTO.email().trim().isEmpty()) {
                        p.setEmail(personDTO.email());
                    }
                });

        Person person = employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(personDTO.personId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Person with the given ID does not exist"));

        dataService.modifyPersonInFile(person.getPersonId(), person);
    }

    public boolean exists(Person person) {
        return employeesMap.keySet().stream()
                .anyMatch(p -> p.getPersonId().equals(person.getPersonId()) ||
                        p.getMobile().equals(person.getMobile()) ||
                        p.getPesel().equals(person.getPesel()) ||
                        p.getEmail().equals(person.getEmail()));
    }

    public static String generateUniquePersonId() {
        OptionalInt maxId = employeesMap.keySet().stream()
                .map(Person::getPersonId)
                .mapToInt(Integer::parseInt)
                .max();

        int newId = maxId.isPresent() ? maxId.getAsInt() + 1 : 1;

        return Integer.toString(newId);
    }
}


