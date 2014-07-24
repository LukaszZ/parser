package pl.lzola.parser;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import pl.lzola.model.EmployeeData;

public class Parser {

	public static EmployeeData parseSax(String staffRepository, String employerRepository) throws ParserConfigurationException, SAXException {
		return SaxParser.getInstance().parse(staffRepository, employerRepository);
	}
	
	public static EmployeeData parseStax(String staffRepository, String employerRepository) {
		return StaxParser.getInstance().parse(staffRepository, employerRepository);
	}
	
}
