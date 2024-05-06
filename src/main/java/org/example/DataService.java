package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class DataService {

    PersonBuilder personBuilder = new PersonBuilder();

    public DataService() {
        this.personBuilder = new PersonBuilder();
    }

    public void readFromFiles(File dir) throws ParserConfigurationException, IOException, SAXException {
        File[] files = dir.listFiles();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                readFromFiles(file);
            } else if (file.getName().toLowerCase().endsWith(".xml")) {
                processFile(file, builder);
            }
        }
    }

    private void processFile(File file, DocumentBuilder builder) throws SAXException, IOException {
        Document document = builder.parse(file.getAbsolutePath());
        NodeList nodeList = document.getElementsByTagName("person");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Person person = personBuilder.build(file, (Element) node);

                EmployeesService.employeesMap.put(person, file.getName());
            }
        }
    }

    public String createAndSavePersonToXML(Person person, Type type) throws ParserConfigurationException, TransformerException, IOException {
        String directory = "./data/" + type.toString().toLowerCase() + "/";
        File file = createFile(directory);

        if (file == null) {
            throw new IOException("Failed to create directory: " + directory);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element rootElement = doc.createElement("person");
        doc.appendChild(rootElement);

        personBuilder.appendPersonDetailsToElement(doc, rootElement, person);

        savePersonToXML(doc, file);

        return file.getName();
    }

    private File createFile(String directory) throws IOException {
        File dir = new File(directory);
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) {
                throw new IOException("Failed to create directory: " + directory);
            }
        }

        Random rand = new Random();
        String fileName;
        File file;
        do {
            fileName = String.format("%05d.xml", rand.nextInt(100000));
            file = new File(directory + fileName);
        } while (file.exists());
        return file;
    }

    public boolean deleteFile(String fileName, Type type) {
        String directory = "./data/" + type.toString().toLowerCase() + "/";
        File file = new File(directory + fileName);
        return file.exists() && file.delete();
    }

    public void modifyPersonInFile(String id, Person updatedPerson) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Person person = EmployeesService.employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Person with the given ID does not exist"));
        updatedPerson.setPersonId(id);

        String fileName = EmployeesService.employeesMap.get(person);
        String directory = "./data/" + (person.getType().toString().toLowerCase() + "/");
        File file = new File(directory + fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        Element rootElement = doc.getDocumentElement();

        personBuilder.updatePersonInElement(rootElement, updatedPerson);

        savePersonToXML(doc, file);
    }


    private void appendChildElement(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name);
        child.setTextContent(value);
        parent.appendChild(child);
    }

    public void savePersonToXML(Document doc, File file) throws TransformerException {
        if (file == null || doc == null) {
            throw new IllegalArgumentException("Document or file is null");
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}