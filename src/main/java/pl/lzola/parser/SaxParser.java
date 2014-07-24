package pl.lzola.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.lzola.model.EmployeeData;

class SaxParser {

	private SAXParser saxParser;
	private EmployeeData.Builder builder;
	
	/**
	 * Private constructor only for getInstance method.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private SaxParser() throws ParserConfigurationException, SAXException {
		prepareSaxPareser();
		prepareEmployeeBuilder();
	}

	public EmployeeData parse(String staffRepository, String employerRepository) {
		
		try {
			loadEmployeeDateFromFile(staffRepository);
			loadEmployerDateFromFile(employerRepository);
		}catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return builder.build();
	}

	private void loadEmployeeDateFromFile(String staffRepository) throws SAXException, IOException {
		InputStream is = getInputStreamForFile(staffRepository);
		saxParser.parse(is, new StaffHandler());
		is.close();
	}
	

	private void loadEmployerDateFromFile(String employerRepository) throws SAXException, IOException {
		InputStream is = getInputStreamForFile(employerRepository);
		saxParser.parse(is, new EmployerHandler());
		is.close();
	}

	private InputStream getInputStreamForFile(String file) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); 
		return classLoader.getResourceAsStream(file);
	}
	
	/**
	 * Returns new instance of SaxParser ready to parse.
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static SaxParser getInstance() throws ParserConfigurationException, SAXException {
		return new SaxParser();
	}
	
	private void prepareEmployeeBuilder() {
		builder = new EmployeeData.Builder();
	}
	
	private void prepareSaxPareser() throws ParserConfigurationException, SAXException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		saxParser = factory.newSAXParser();
	}
	
	private class StaffHandler extends org.xml.sax.helpers.DefaultHandler {
		private boolean firstname;
		private boolean lastename;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("firstname")) {
				firstname = true;
			} else if (qName.equals("lastname")) {
				lastename = true;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (firstname) {
				builder.identifiedByFirstName(new String(ch, start, length));
				firstname = false;
			} else if (lastename) {
				builder.identifiedByLastName(new String(ch, start, length));
				lastename = false;
			}
		}
		
	}
	
	private class EmployerHandler extends org.xml.sax.helpers.DefaultHandler {

		private boolean firstname;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("firstname")) {
				firstname = true;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (firstname) {
				builder.withHead(new String(ch, start, length));
				firstname = false;
			}
		}
		
	}
}
