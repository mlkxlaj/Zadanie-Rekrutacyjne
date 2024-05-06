package org.example;

import org.w3c.dom.Element;

import java.io.File;

public class PersonBuilder {
    public Person build(File file, Element node) {
        Element eElement = node;
        Person person = new Person
                (
                        file.getPath().contains("External") ? Type.EXTERNAL : Type.INTERNAL,
                        eElement.getElementsByTagName("firstName").item(0).getTextContent(),
                        eElement.getElementsByTagName("lastName").item(0).getTextContent(),
                        eElement.getElementsByTagName("mobile").item(0).getTextContent(),
                        eElement.getElementsByTagName("email").item(0).getTextContent(),
                        eElement.getElementsByTagName("pesel").item(0).getTextContent()
                );
        person.setPersonId(eElement.getElementsByTagName("personId").item(0).getTextContent());
        return person;
    }
}
