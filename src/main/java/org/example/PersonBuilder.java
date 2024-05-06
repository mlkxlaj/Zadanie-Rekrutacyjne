package org.example;

import org.w3c.dom.Document;
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
                        eElement.getElementsByTagName("pesel").item(0).getTextContent(),
                        eElement.getElementsByTagName("email").item(0).getTextContent()
                );
        person.setPersonId(eElement.getElementsByTagName("personId").item(0).getTextContent());
        return person;
    }


    public void updatePersonInElement(Element rootElement, Person updatedPerson) {
        rootElement.getElementsByTagName("personId").item(0).setTextContent(updatedPerson.getPersonId());
        rootElement.getElementsByTagName("firstName").item(0).setTextContent(updatedPerson.getFirstName());
        rootElement.getElementsByTagName("lastName").item(0).setTextContent(updatedPerson.getLastName());
        rootElement.getElementsByTagName("mobile").item(0).setTextContent(updatedPerson.getMobile());
        rootElement.getElementsByTagName("email").item(0).setTextContent(updatedPerson.getEmail());
        rootElement.getElementsByTagName("pesel").item(0).setTextContent(updatedPerson.getPesel());
    }


    public void appendPersonDetailsToElement(Document doc, Element rootElement, Person person) {
        appendChildElement(doc, rootElement, "personId", person.getPersonId());
        appendChildElement(doc, rootElement, "firstName", person.getFirstName());
        appendChildElement(doc, rootElement, "lastName", person.getLastName());
        appendChildElement(doc, rootElement, "mobile", person.getMobile());
        appendChildElement(doc, rootElement, "email", person.getEmail());
        appendChildElement(doc, rootElement, "pesel", person.getPesel());
    }

    private void appendChildElement(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name);
        child.setTextContent(value);
        parent.appendChild(child);
    }
}
