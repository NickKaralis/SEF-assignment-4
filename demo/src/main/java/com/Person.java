package com;

import java.util.*;
import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private String birthdate;
    private HashMap<Date, Integer> demeritPoints; // A variable that holds the demerit points with the offense day
    private boolean isSuspended;

    public static String[] splitByPipe(String address) {
        String[] parts = new String[5];
        int start = 0;
        int partIndex = 0;

        for (int i = 0; i < 4; i++) { // We expect 5 parts, so 4 pipes to find
            int pipeIndex = address.indexOf('|', start);
            if (pipeIndex == -1) {
                // Not enough parts
                return null; // or handle error
            }
            parts[partIndex++] = address.substring(start, pipeIndex).trim();
            start = pipeIndex + 1;
        }
        // Last part after the last pipe
        parts[partIndex] = address.substring(start).trim();

        return parts;
    }

    public boolean addPerson(String personID, String firstName, String lastName, String address, String birthdate) {
        // validate inputs
        if (validate(personID, firstName, lastName, address, birthdate) == false) {
            return false;
        }

        // make file ts
        try {
            FileOutputStream fos = new FileOutputStream("persons.txt", true);
            PrintStream ps = new PrintStream(fos);
            ps.println(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate);
            ps.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }

        System.out.println("completed added");

        return true;
    }

    // TODO: This method adds information about a person to a TXT file.
    // Condition 1: PersonID should be exactly 10 characters long;
    // the first two characters should be numbers between 2 and 9, there should be
    // at least two special characters between characters 3 and 8, //and the last
    // two characters should be upper case letters (A - Z). Example: "56s_d%&fAB"
    // Condition 2: The address of the Person should follow the following format:
    // Street Number | Street | City | State | Country.
    // The State should be only Victoria. Example: 32 | Highland Street | Melbourne
    // | Victoria | Australia.
    // Condition 3: The format of the birth date of the person should follow the
    // following format: DD-MM-YYYY. Example: 15-11-1990
    // Instruction: If the Person's information meets the above conditions and any
    // other conditions you may want to consider, //the information should be
    // inserted into a TXT file, and the addPerson function should return true.
    // Otherwise, the information should not be inserted into the TXT file, and the
    // addPerson function should return false. return true;
    public boolean updatePersonalDetails(String personID, String newID, String firstName, String lastName,
            String address,
            String birthdate) {

        // validate all inputs
        if (validate(personID, firstName, lastName, address, birthdate) == false) {
            return false;
        }

        if (validateID(newID) == false) {
            System.out.println("new ID is invalid");
            return false;
        }

        List<String> lines = new ArrayList<>();
        // need to search persons file for personID
        try {
            FileInputStream fis = new FileInputStream("persons.txt");
            Scanner scanner = new Scanner(fis);

            String userLine = null;

            // add all users except the one editting to to lines
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith(personID)) {
                    lines.add(line);
                } else {
                    userLine = line;
                }
            }

            scanner.close();
            fis.close();

            // Clear the contents of the file before writing updated data
            try (FileOutputStream fos = new FileOutputStream("persons.txt")) {
                // Opening the file in this mode clears its contents
            } catch (IOException e) {
                return false;
            }

            // create updated user line
            if (userLine != null) {
                String[] userDetails = userLine.split(",", -1);
                String updatedLine = newID + "," + firstName + "," + lastName + "," + address + "," + birthdate;

                // DEMERITE POINT UPDATE - this is where it should add the demerit poitns when
                // updated
                // Append any remaining details from the original line that are not part of the
                // updated details
                for (int i = 5; i < userDetails.length; i++) {
                    updatedLine += "," + userDetails[i];
                }

                // Add the updated line to the list of lines
                lines.add(updatedLine);

            } else {
                System.out.println("Person with ID " + personID + " not found.");
                return false; // Person not found, cannot update
            }

        } catch (IOException e) {
            return false;
        }

        // Clear the contents of the file before writing updated data
        try (FileOutputStream fos = new FileOutputStream("persons.txt")) {
            // Opening the file in this mode clears its contents
        } catch (IOException e) {
            return false;
        }

        // write back all lines to persons.txt
        try {
            FileOutputStream fos = new FileOutputStream("persons.txt", true);
            PrintStream ps = new PrintStream(fos);
            for (String line : lines) {
                ps.println(line);
            }
            ps.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }

        return true;
        // TODO: This method allows updating a given person's ID, firstName, lastName,
        // address and birthday in a TXT file.
        // Changing personal details will not affect their demerit points or the
        // suspension status.

        // All relevant conditions discussed for the addPerson function also need to be
        // considered and checked in the updatPerson function.
        // Condition 1: If a person is under 18, their address cannot be changed.
        // Condition 2: If a person's birthday is going to be changed, then no other
        // personal detail (i.e, person's ID, firstName, lastName, address) can be
        // changed. //Condition 3: If the first character/digit of a person's ID is an
        // even number, then their ID cannot be changed.
        // Instruction: If the Person's updated information meets the above conditions
        // and any other conditions you may want to consider,
        // the Person's information should be updated in the TXT file with the updated
        // information, and the updatePersonalDetails function should return true.
        // //Otherwise, the Person's updated information should not be updated in the
        // TXT file, and the updatePersonalDetails function should return false. return
        // true;
    }

    public String addDemeritPoints() {
        return ""; // remove after finishing
    }

    // TODO: This method adds demerit points for a given person in a TXT file.
    // Condition 1: The format of the date of the offense should follow the
    // following format: DD-MM-YYYY. Example: 15-11-1990
    // Condition 2: The demerit points must be a whole number between 1-6
    // Condition 3: If the person is under 21, the isSuspended variable should be
    // set to true if the total demerit points within two years exceed 6.
    // If the person is over 21, the isSuspended variable should be set to true if
    // the total demerit points within two years exceed 12.

    // Instruction: If the above condiations and any other conditions you may want
    // to consider are met, the demerit points for a person should be inserted into
    // the TXT file, //and the addDemerit Points function should return "Sucess".
    // Otherwise, the addDemeritPoints function should return "Failed". return
    // "Sucess";

    public boolean validateID(String personID) {
        if (personID == null || personID.length() != 10) {
            System.out.println(personID);
            System.out.println("ID wrong size");
            return false;
        }

        // Check first two characters are digits 2–9
        char c1 = personID.charAt(0);
        char c2 = personID.charAt(1);
        if (!Character.isDigit(c1) || !Character.isDigit(c2)) {
            System.out.println("first 2 characters not digits");
            return false;
        }
        int digit1 = Character.getNumericValue(c1);
        int digit2 = Character.getNumericValue(c2);
        if (digit1 <= 2 || digit1 >= 9 || digit2 <= 2 || digit2 >= 9) {
            System.out.println("First two characters are not digits between 2 and 9");
            return false;
        }

        // char checker ts
        int specialCount = 0;
        for (int i = 2; i < 8; i++) {
            char ch = personID.charAt(i);
            if (!Character.isLetterOrDigit(ch)) {
                specialCount++;
            }
        }
        if (specialCount < 2) {
            System.out.println("Not enough special characters");
            return false;
        }

        // format check 1
        char last1 = personID.charAt(8);
        char last2 = personID.charAt(9);
        if (!(Character.isUpperCase(last1) && Character.isUpperCase(last2))) {
            System.out.println("Last two characters are not uppercase letters");
            return false;
        }

        return true;
    }

    public boolean validateAddress(String address) {
        if (address == null) {
            System.out.println("Address is null");
            return false;
        }

        String[] addressParts = splitByPipe(address);
        if (addressParts == null || addressParts.length != 5) {
            System.out.println("Address format is invalid");
            return false; // 5 parts expected, invalid address format
            // invalid address format
        }
        if (!addressParts[2].trim().equals("Victoria")) {

            System.out.println(addressParts[2]);
            System.out.println("State is not Victoria");
            return false;
        }

        return true;
    }

    public boolean validateBirthdate(String birthdate) {
        // format cehck
        if (birthdate == null || birthdate.length() != 10) {
            System.out.println("Birthdate is null or wrong size");
            return false;
        }
        if (birthdate.charAt(2) != '-' || birthdate.charAt(5) != '-') {

            System.out.println("Birthdate format is invalid, should be DD-MM-YYYY");
            return false;
        }
        // date check // TO BE UPDATED @EVERYONE
        try {
            int day = Integer.parseInt(birthdate.substring(0, 2));
            int month = Integer.parseInt(birthdate.substring(3, 5));
            int year = Integer.parseInt(birthdate.substring(6));

            System.out.println("day: " + day + ", month: " + month + ", year: " + year);

            // not sure what the extent of the date check should be
            // this will do for now
            if (day < 1 || day > 31)
                return false;
            if (month < 1 || month > 12)
                return false;
            if (year < 1900 || year > 2100)
                return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean validate(String personID, String firstName, String lastName, String address, String birthdate) {
        // return false;
        // Check if personID is 10 characters
        System.out.println(personID);
        if (validateID(personID) == false) {
            System.out.println("Person ID is invalid");
            return false;
        }

        // format check
        if (validateAddress(address) == false) {
            System.out.println("Address is invalid");
            return false;
        }

        if (validateBirthdate(birthdate) == false) {
            System.out.println("Birthdate is invalid");
            return false;
        }

        System.out.println("completed validation");

        return true;
    }

}
