/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.payroll;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Entity // <1>
public class Employee {

	private @Id @GeneratedValue Long id; // <2>
	private String firstName;
	private String lastName;
	private String description;

	private int jobYears;

	private String email;


	public Employee(String firstName, String lastName, String description,int jobYears,String email) throws InstantiationException {
		if(!areParametersValid(firstName,lastName,description,jobYears,email)){
			throw new InstantiationException("Invalid Parameters");
		}
		if(!emailIsValid(email)){
			throw new InstantiationException("Invalid Parameters");
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
		this.jobYears = jobYears;
		this.email = email;
	}

	public Employee() {

	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Employee employee = (Employee) o;
		return Objects.equals(id, employee.id) &&
			Objects.equals(firstName, employee.firstName) &&
			Objects.equals(lastName, employee.lastName) &&
			Objects.equals(description, employee.description) &&
			Objects.equals(jobYears, employee.jobYears);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, firstName, lastName, description);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(parameterIsValid(firstName)){
			this.firstName = firstName;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if(parameterIsValid(lastName)){
			this.lastName = lastName;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(parameterIsValid(description)){
			this.description = description;
		}
	}

	public int getJobYears() {
		return jobYears;
	}

	public void setJobYears(int jobYears) {
		if(jobYearsAreValid(jobYears)){
			this.jobYears = jobYears;
		}

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(parameterIsValid(email) && emailIsValid(email)){
			this.email = email;
		}
	}

	private boolean parameterIsValid(String firstName){
		return firstName != null && !firstName.trim().isEmpty();
	}

	private boolean jobYearsAreValid(int jobYears){
		return jobYears >= 0;
	}

	private boolean areParametersValid(String firstName, String lastName, String description,int jobYears,String email){
		return parameterIsValid(firstName) && parameterIsValid(lastName) && parameterIsValid(description) &&
				jobYearsAreValid(jobYears) && parameterIsValid(email);
	}

	private boolean emailIsValid(String email){
		return email.contains("@");
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", description='" + description + '\'' +
				", jobYears=" + jobYears +
				", email='" + email + '\'' +
				'}';
	}
}
// end::code[]
