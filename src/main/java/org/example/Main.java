package org.example;
import org.example.dto.PersonDTO;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DataService dataService = new DataService();
        EmployeesService employeesService = new EmployeesService();
        dataService.readFromFiles(new File("./data/"));

        employeesService.create(new Person(Type.INTERNAL, "Tadeusz", "Bartkiewicz", "+63283943273","72012218711","tadekbartk@support.com"));
        employeesService.create(new Person(Type.EXTERNAL, "Filip", "Cukierski", "+892 881 992 003","06290773871","filip.cukierski@apple.com"));
        employeesService.create(new Person(Type.INTERNAL, "Kacper", "KWTD", "+112 998 997 999","75040954752","kwtd@google.com"));

        EmployeesService.employeesMap.forEach((k,v) -> System.out.println(k + " " + v));
        System.out.println("-------------------------------------------------");
        employeesService.modify(new PersonDTO(null,"6", null, "UpdatedLastName", null,null,null));
        EmployeesService.employeesMap.forEach((k,v) -> System.out.println(k + " " + v));

        employeesService.remove("4");
        employeesService.remove("5");
        employeesService.remove("6");
    }
}
