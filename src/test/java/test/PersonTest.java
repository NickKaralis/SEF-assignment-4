package test;

import com.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PersonTest {
    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person();
    }

    // -------------------- ADD PERSON TESTS --------------------

    // Test Case 1: Valid input
    @Test
    void testAddPersonValid() {
        boolean result = person.addPerson("78l_f%&cCB", "Steve", "Smith",
                "15 | Example Street | Melbourne | Victoria | Australia", "01-01-2025");
        assertTrue(result);
    }

    // Test Case 2: Invalid ID, names, birthdate
    @Test
    void testAddPersonInvalidEverything() {
        boolean result = person.addPerson("ct4tfe", "4f2dg", "Sm5the", "no", "30-02-2025");
        assertFalse(result);
    }

    // Test Case 3: Valid ID, null/empty other fields
    @Test
    void testAddPersonNullInputs() {
        boolean result = person.addPerson("68l_f%&yXZ", "", "", "", "five-12-2025");
        assertFalse(result);
    }

    // Test Case 4: Duplicate ID
    @Test
    void testAddPersonDuplicateID() {
        Person first = new Person();
        first.addPerson("78l_f%&cCB", "Steve", "Smith",
                "15 | Example Street | Melbourne | Victoria | Australia", "01-01-2025");
        Person second = new Person();
        boolean result = second.addPerson("78l_f%&cCB", "Steve", "Smith",
                "15 | Example Street | Melbourne | Victoria | Australia", "01-01-2025");
        assertTrue(result);  
    }

    // Test Case 5: Invalid address structure
    @Test
    void testAddPersonInvalidAddressFormat() {
        boolean result = person.addPerson("78l_f%&cBB", "John", "Doe",
                "five | 32 | pipe | 2345 | %3DT4@~ | France | 4323458", "15-09-2000");
        assertFalse(result);
    }

    // -------------------- UPDATE PERSON TESTS --------------------
    // cbs commenting properly
    @Test
    // Helper to prepare file
    private void writeTestPersonToFile(String line) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("persons.txt"));
        writer.write(line);
        writer.newLine();
        writer.close();
    }

    @Test
    void testUpdatePersonNoChanges() throws IOException {
        // Prepare test data in file
        writeTestPersonToFile(
                "77l_f%&cCB,Anthony,Brown,15 | Example Street | Melbourne | Victoria | Australia,19-12-1999,false");

        // Perform update - only birthday changes
        boolean result = person.updatePersonalDetails(
                "77l_f%&cCB", "77l_f%&cCB", "", "",
                "15 | Example Street | Melbourne | Victoria | Australia",
                "19-12-1992");

        assertTrue(result);
    }

    @Test
    void testUpdatePersonValidChanges() throws IOException {
        // Prepare test data in file
        writeTestPersonToFile(
                "77l_f%&cCB,Anthony,Brown,15 | Example Street | Melbourne | Victoria | Australia,19-12-1999,false");

        // Perform update - address change
        boolean result = person.updatePersonalDetails(
                "77l_f%&cCB", "77l_f%&cCB", "Anthony", "Brown",
                "16 | Example Street | Melbourne | Victoria | Australia",
                "19-12-1999");

        assertTrue(result);
    }

    @Test
    void testUpdatePersonInvalidFields() {
        person.addPerson("77l_f%&cCB", "Anthony", "Brown", "15 | Example Street | Melbourne | Victoria | Australia",
                "19-12-1999");
        boolean result = person.updatePersonalDetails("77l_f%&cCB", "77l_f%&cCB", "2453", "345654$@",
                "knakjd|adn2e@$*&", "12");
        assertFalse(result);
    }

    @Test
    void testUpdatePersonUnder18ChangeAddressAndID() {
        person.addPerson("77l_f%&cCZ", "Child", "Smith", "15 | Example Street | Melbourne | Victoria | Australia",
                "19-01-2009");
        boolean result = person.updatePersonalDetails("77l_f%&cCZ", "", "", "",
                "16 | Example Street | Melbourne | Victoria | Australia", "");
        assertFalse(result);
    }

    @Test
    void testUpdatePersonIDStartsWithEvenNumber() {
        person.addPerson("28l_f%&cCB", "Alex", "Smith", "15 | Example Street | Melbourne | Victoria | Australia",
                "19-01-2000");
        boolean result = person.updatePersonalDetails("28l_f%&ccb", "48l_f%&XYB", "", "", "", "");
        assertFalse(result);
    }

    // -------------------- ADD DEMERIT POINTS TESTS --------------------

    @Test
    void testAddDemeritValid() {
        person.addPerson("39l_f%&cCB", "Test", "User", "15 | Example Street | Melbourne | Victoria | Australia",
                "01-01-2000");
        String result = person.addDemeritPoints(2, "19-01-2025");
        assertEquals("Success", result);
    }

    @Test
    void testAddDemeritInvalidValues() {
        person.addPerson("28l_f%&cCB", "Test", "User", "15 | Example Street | Melbourne | Victoria | Australia",
                "01-01-2000");
        String result = person.addDemeritPoints(-1, "74#$235");
        assertEquals("Failed", result);
    }

    @Test
    void testAddDemeritPointsCauseSuspension() {
        person.addPerson("28l_f%&cCB", "Test", "User", "15 | Example Street | Melbourne | Victoria | Australia",
                "01-01-2000");
        person.addDemeritPoints(5, "01-01-2025");
        person.addDemeritPoints(3, "02-01-2025");
        person.addDemeritPoints(5, "03-01-2025");
        assertTrue(person.isSuspended);
    }

    @Test
    void testAddDemeritPointsToSuspendedUser() {
        person.addPerson("28l_f%&cCB", "Test", "User", "15 | Example Street | Melbourne | Victoria | Australia",
                "01-01-2000");
        person.addDemeritPoints(5, "01-01-2025");
        String result = person.addDemeritPoints(5, "02-01-2025");
        result = person.addDemeritPoints(5, "01-01-2025");
        result = person.addDemeritPoints(5, "03-01-2025");

        result = person.addDemeritPoints(5, "01-02-2025");
        assertEquals("Success", result);
    }

    @Test
    void testAddDemeritInvalidAmountAndDate() {
        person.addPerson("28l_f%&cCB", "Test", "User", "15 | Example Street | Melbourne | Victoria | Australia",
                "01-01-2000");
        String result = person.addDemeritPoints(7, "01-100-2025");
        assertEquals("Failed", result);
    }
}
