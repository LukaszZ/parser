package pl.lzola.parser;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import pl.lzola.model.EmployeeData;

public class ParserTest {

	@Test
	public void shouldLoadYongMookKimWithHeadLukaszWithSaxParser() throws Exception {
		//given
		String staffRepository = "staff.xml";
		String employerRepository = "employer.xml";
		
		//when
		EmployeeData employeeData = Parser.parseSax(staffRepository, employerRepository);
		
		//then
		assertThat(employeeData.getFirstName()).isEqualTo("yong");
		assertThat(employeeData.getLastName()).isEqualTo("mook kim");
		assertThat(employeeData.getHead()).isEqualTo("Lukasz");
		
	}
	
	@Test
	public void shouldLoadYongMookKimWithHeadLukaszWithStaxParser() throws Exception {
		//given
		String staffRepository = "staff.xml";
		String employerRepository = "employer.xml";
		
		//when
		EmployeeData employeeData = Parser.parseStax(staffRepository, employerRepository);
		
		//then
		assertThat(employeeData.getFirstName()).isEqualTo("yong");
		assertThat(employeeData.getLastName()).isEqualTo("mook kim");
		assertThat(employeeData.getHead()).isEqualTo("Lukasz");
		
	}
}
