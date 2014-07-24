package pl.lzola.model;

public class EmployeeData {

	private String firstName;
	private String lastName;
	private String head;

	/**
	 * For builder only
	 */
	private EmployeeData() {
	}
	
	/**
	 * For builder only
	 * @param employeeData
	 */
	public EmployeeData(EmployeeData employeeData) {
		this.firstName = employeeData.firstName;
		this.lastName = employeeData.lastName;
		this.head = employeeData.head;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getHead() {
		return head;
	}
	
	public static class Builder {
		private EmployeeData employeeData = new EmployeeData();
		
		public Builder identifiedByFirstName(String fisrtName) {
			employeeData.firstName = fisrtName;
			return this;
		}
		
		public Builder identifiedByLastName(String lastName) {
			employeeData.lastName = lastName;
			return this;
		}
		
		public Builder withHead(String firstName) {
			employeeData.head = firstName;
			return this;
		}
		
		public EmployeeData build() {
			return new EmployeeData(employeeData);
		}
	}
}
