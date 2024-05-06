package org.example;

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
        //System.out.println(employeesService.findPerson(null, "2", null, null, null, null));
        //employeesService.create(new Person(Type.INTERNAL, "Emma", "Johnson", "+988653221", "02261905898", "emmaj32321son@example.com"));
        //employeesService.employeesMap.forEach((person, file) -> System.out.println(person + " " + file));
        //employeesService.remove("4");
        //employeesService.employeesMap.forEach((person, file) -> System.out.println(person + " " + file));
        employeesService.modify(new Person(Type.EXTERNAL, "Sarah", "adwawddwad", "+2345678901", "23456789012", "sarah.williams@example.com"),"3");
        //employeesService.employeesMap.forEach((person, file) -> System.out.println(person + " " + file));
    }
}
