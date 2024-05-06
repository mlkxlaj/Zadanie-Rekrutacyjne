package org.example;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class EmployeesService {
    public static Map<Person, String> employeesMap = new HashMap<>();
    private DataService dataService;
    //testy parametryzowane

    public EmployeesService() {
        this.dataService = new DataService();
    }

    public List<Person> findPerson(Type type, String personId, String firstName, String lastName, String
            mobile, String pesel) {
        return employeesMap.keySet().stream()
                .filter(person -> type == null || person.getType().equals(type))
                .filter(person -> personId == null || person.getPersonId().equals(personId))
                .filter(person -> firstName == null || person.getFirstName().equals(firstName))
                .filter(person -> lastName == null || person.getLastName().equals(lastName))
                .filter(person -> mobile == null || person.getMobile().equals(mobile))
                .filter(person -> pesel == null || person.getPesel().equals(pesel))
                .collect(Collectors.toList());
    }

    public void create(Person person) throws ParserConfigurationException, TransformerException, IOException {
        if (exists(person)) {
            throw new IllegalArgumentException("main.java.Person with the same personId, mobile, pesel, and email already exists");
        }
        person.setPersonId(generateUniquePersonId());

        employeesMap.put(person, dataService.createAndSavePersonToXML(person, person.getType()));
    }

    public boolean remove(String personId) {
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

    public void modify(Person person, String id) throws
            ParserConfigurationException, IOException, TransformerException, SAXException {

        employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Person with the given ID does not exist"));

        employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(id))
                .findFirst()
                .ifPresent(p -> {
                    p.setFirstName(person.getFirstName());
                    p.setLastName(person.getLastName());
                    p.setMobile(person.getMobile());
                    p.setPesel(person.getPesel());
                    p.setType(person.getType());
                    p.setEmail(person.getEmail());
                });

        dataService.modifyPersonInFile(id, person);
    }

    public boolean exists(Person person) {
        return employeesMap.keySet().stream()
                .anyMatch(p -> p.getMobile().equals(person.getMobile()) ||
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


