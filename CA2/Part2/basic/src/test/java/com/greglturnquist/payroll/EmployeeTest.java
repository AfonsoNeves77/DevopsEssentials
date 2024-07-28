package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private void assertInvalidEmployeeParameters_Exception(String firstName, String lastName, String description, int jobYears,String email) {
        // Arrange
        String expected = "Invalid Parameters";

        // Act
        Exception exception = assertThrows(InstantiationException.class, () ->
                new Employee(firstName, lastName, description, jobYears,email));

        String result = exception.getMessage();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void NullNameEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception(null, "Smith", "Janitor", 20,"sample@hotmail.com");
    }

    @Test
    void EmptyNameEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("", "Smith", "Janitor", 20,"sample@hotmail.com");
    }


    @Test
    void NullLastNameEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", null, "Janitor", 20,"sample@hotmail.com");
    }

    @Test
    void EmptyLastNameEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "", "Janitor", 20,"sample@hotmail.com");
    }


    @Test
    void NullDescriptionEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", null, 20,"sample@hotmail.com");
    }

    @Test
    void EmptyDescriptionEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", "", 20,"sample@hotmail.com");
    }

    @Test
    void NegativeJobYearsEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", "Janitor", -1,"sample@hotmail.com");
    }

    @Test
    void NullEmailEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", "Sailor", 20,null);
    }

    @Test
    void EmptyEmailEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", "Sailor", 20,"");
    }

    @Test
    void invalidEmailEmployee_InstantiationExceptionShouldBeThrown() {
        assertInvalidEmployeeParameters_Exception("John", "Smith", "Sailor", 20,"potter..hotmail.com");
    }

    @Test
    void getFirstName() throws InstantiationException {
        String firstName = "John";
        String lastName = "Smith";
        String description = "Janitor";
        int jobYears = 7;
        String email = "sample@hotmail.com";
        // Arrange
        Employee employee = new Employee(firstName,lastName,description,jobYears,email);
        // Act
        String result = employee.getFirstName();
        // Assert
        assertEquals(firstName, result);
    }
    @Test
    void setAndGetFirstName()  {
        String firstName = "John";
        // Arrange
        Employee employee = new Employee();
        // Act
       employee.setFirstName(firstName);
       String result = employee.getFirstName();
        // Assert
        assertEquals(firstName, result);
    }

    @Test
    void setFirstName_InvalidName()  {
        String firstName = "";
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setFirstName(firstName);
        String result = employee.getFirstName();
        // Assert
        assertNull(result);
    }


    @Test
    void getAndSetLastName() {
        // Arrange
        Employee employee = new Employee();

        // Act
        employee.setLastName("Smith");
        String result = employee.getLastName();

        // Assert
        assertEquals("Smith", result);
    }

    @Test
    void setLastName_InvalidLastName()  {
        String lastName = "";
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setLastName(lastName);
        String result = employee.getLastName();
        // Assert
        assertNull(result);
    }

    @Test
    void getAndSetDescription() {
        // Arrange
        Employee employee = new Employee();

        // Act
        employee.setDescription("Janitor");
        String result = employee.getDescription();

        // Assert
        assertEquals("Janitor", result);
    }

    @Test
    void setDescription_InvalidDescription()  {
        String description = "";
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setDescription(description);
        String result = employee.getDescription();
        // Assert
        assertNull(result);
    }

    @Test
    void getAndSetJobYears() {
        // Arrange
        Employee employee = new Employee();

        // Act
        employee.setJobYears(5);
        int result = employee.getJobYears();

        // Assert
        assertEquals(5, result);
    }

    @Test
    void setJobYears_InvalidYear()  {
        int jobYear = -9;
        int expected = 0;
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setJobYears(jobYear);
        int result = employee.getJobYears();
        // Assert
        assertEquals(expected,result);
    }


    @Test
    void getAndSetEmail() {
        // Arrange
        Employee employee = new Employee();
        String email = "sample@hotmail.com";

        // Act
        employee.setEmail(email);
        String result = employee.getEmail();

        // Assert
        assertEquals(email, result);
    }

    @Test
    void setEmail_EmptyEmail()  {
        String email = "";
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setEmail(email);
        String result = employee.getEmail();
        // Assert
        assertNull(result);
    }

    @Test
    void setEmail_InvalidEmail_EmailShouldNotBeUpdated()  {
        String email = "potter..hotmail.com";
        // Arrange
        Employee employee = new Employee();
        // Act
        employee.setEmail(email);
        String result = employee.getEmail();
        // Assert
        assertNull(result);
    }
}