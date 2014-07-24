package pl.lzola.parser;

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.xml.sax.SAXException;

import pl.lzola.model.EmployeeData;

public class StaxParser {
	
	private EmployeeData.Builder builder;
	
	/**
	 * Private constructor only for getInstance method.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private StaxParser() { 
		prepareEmployeeBuilder();
	}

	private void prepareEmployeeBuilder() {
		builder = new EmployeeData.Builder();
	}
	
	public static StaxParser getInstance() {
		return new StaxParser();
	}

	public EmployeeData parse(String staffRepository, String employerRepository) {
		loadEmployeeDateFromFile(staffRepository);
		loadEmployerDateFromFile(employerRepository);
		
		return builder.build();
	}

	private void loadEmployerDateFromFile(String employerRepository) {
		InputStream is = getInputStreamForFile(employerRepository);
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(is);
			
			while (xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()){
	                   StartElement startElement = xmlEvent.asStartElement();
	                   if(startElement.getName().getLocalPart().equals("firstname")){
	                	   xmlEvent = xmlEventReader.nextEvent();
	                	   builder.withHead(xmlEvent.asCharacters().getData());
	                   }
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private void loadEmployeeDateFromFile(String staffRepository) {
		InputStream is = getInputStreamForFile(staffRepository);
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(is);
			
			while (xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
				if (xmlEvent.isStartElement()){
	                   StartElement startElement = xmlEvent.asStartElement();
	                   if(startElement.getName().getLocalPart().equals("firstname")){
	                	   xmlEvent = xmlEventReader.nextEvent();
	                	   builder.identifiedByFirstName(xmlEvent.asCharacters().getData());
	                   } else if (startElement.getName().getLocalPart().equals("lastname")) {
	                	   xmlEvent = xmlEventReader.nextEvent();
	                	   builder.identifiedByLastName(xmlEvent.asCharacters().getData());
	                   }
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	private InputStream getInputStreamForFile(String file) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
		return classLoader.getResourceAsStream(file);
	}

}
