package pl.lzola.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

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

		//As we read any XML element we will push that in this stack
	    private Stack<String> elementStack = new Stack<String>();
	    
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {			
			//Push it in element stack
	        elementStack.push(qName);	        
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String currentElement = currentElement();
			if ("firstname".equals(currentElement)) {
				builder.identifiedByFirstName(new String(ch, start, length));
				elementStack.remove(currentElement);
			} else if ("lastname".equals(currentElement)) {
				builder.identifiedByLastName(new String(ch, start, length));
				elementStack.remove(currentElement);
			}
		}
		
		
		/**
	     * Utility method for getting the current element in processing
	     * */
	    private String currentElement() {
	        return elementStack.peek();
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
