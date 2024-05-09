package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class DataService {

    private static final String DATA_DIRECTORY = "./data/";
    private static final String XML_EXTENSION = ".xml";

    private final PersonBuilder personBuilder;

    public DataService() {
        this.personBuilder = new PersonBuilder();
    }

    public void readFromFiles(File dir) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = createDocumentBuilder();

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                readFromFiles(file);
            } else if (file.getName().toLowerCase().endsWith(XML_EXTENSION)) {
                processFile(file, builder);
            }
        }
    }

    public String createAndSavePersonToXML(Person person, Type type) throws ParserConfigurationException, TransformerException, IOException {
        String directory = DATA_DIRECTORY + type.toString().toLowerCase() + "/";
        File file = createFile(directory);

        Document doc = createNewDocument();
        Element rootElement = doc.createElement("person");
        doc.appendChild(rootElement);

        personBuilder.appendPersonDetailsToElement(doc, rootElement, person);
        savePersonToXML(doc, file);

        return file.getName();
    }

    public boolean deleteFile(String fileName, Type type) {
        String directory = DATA_DIRECTORY + type.toString().toLowerCase() + "/";
        File file = new File(directory + fileName);
        return file.exists() && file.delete();
    }

    public void modifyPersonInFile(String id, Person updatedPerson) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Person person = findPersonById(id);
        updatedPerson.setPersonId(id);

        String fileName = EmployeesService.employeesMap.get(person);
        String directory = DATA_DIRECTORY + person.getType().toString().toLowerCase() + "/";
        File file = new File(directory + fileName);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        Document doc = parseDocumentFromFile(file);
        Element rootElement = doc.getDocumentElement();

        personBuilder.updatePersonInElement(rootElement, updatedPerson);
        savePersonToXML(doc, file);
    }

    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    private Document createNewDocument() throws ParserConfigurationException {
        return createDocumentBuilder().newDocument();
    }

    private Document parseDocumentFromFile(File file) throws ParserConfigurationException, IOException, SAXException {
        return createDocumentBuilder().parse(file);
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

    private File createFile(String directory) throws IOException {
        File dir = new File(directory);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + directory);
        }

        return createUniqueFileInDirectory(directory);
    }

    private File createUniqueFileInDirectory(String directory) {
        Random rand = new Random();
        String fileName;
        File file;
        do {
            fileName = String.format("%05d.xml", rand.nextInt(100000));
            file = new File(directory + fileName);
        } while (file.exists());
        return file;
    }

    private Person findPersonById(String id) {
        return EmployeesService.employeesMap.keySet().stream()
                .filter(p -> p.getPersonId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Person with the given ID does not exist"));
    }

    private void savePersonToXML(Document doc, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
}