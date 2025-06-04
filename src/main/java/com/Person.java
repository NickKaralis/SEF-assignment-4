package com;

import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.FileOutputStream;
import java.io.FileInputStream;



public class Person {
    public String personID;
    public String firstName;
    public String lastName;
    public String address;
    public String birthdate;
    public HashMap<LocalDate, Integer> demeritPoints = new HashMap<LocalDate, Integer>(); // A variable that holds the
    // demerit
    // points with the offense day
    public boolean isSuspended;

    // FORMATTING
    // id,firstName,lastName,address,birthdate

//      |----------------------------------------------------------------------------------------------------|
//      |----------------------------------------------------------------------------------------------------|
//      |----------------------------------------------------------------------------------------------------|

    public boolean addPerson(String personID, String firstName, String lastName, String address, String birthdate) {
        // validate inputs
        if (!validate(personID, firstName, lastName, address, birthdate)) {
            return false;
        }
        if (isPersonIDExists(personID)) {
            System.out.println("Person with ID " + personID + " already exists.");
            return false;
        }

        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.isSuspended = false; // default value
        this.demeritPoints = new HashMap<LocalDate, Integer>(); // Initialize demerit points

        System.out.println("completed added");

        return true;
    }

    // This method adds information about a person to a TXT file.
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
//  |----------------------------------------------------------------------------------------------------|
//  |----------------------------------------------------------------------------------------------------|
//  |----------------------------------------------------------------------------------------------------|

    public boolean updatePersonalDetails(String personID, String newID, String firstName, String lastName,
            String address, String birthdate) {

       

        // NEED TO CHECK UPDATE CONDITIONS
        // Condition 1: If a person is under 18, their address cannot be changed.
        // Condition 2: If a person's birthday is going to be changed, then no other
        // personal detail can be changed
        // (i.e, person's ID, firstName, lastName, address) cannot be changed.
        // Condition 3: If the first character/digit of a person's ID is an even number,
        // then their ID cannot be changed.

        // create list of lines in the persons.txt file
        List<String> lines = new ArrayList<>();
        Boolean isEven=false;

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

            // System.out.println("HERE 1");

            scanner.close();
            fis.close();
            // create updated user line
            if (userLine != null) {
                String[] userDetails = userLine.split(",", -1);
    
                if (firstName == null || firstName.isBlank()) {
                    firstName = userDetails[1];
                }
                if (lastName == null || lastName.isBlank()) {
                    lastName = userDetails[2];
                }
                if (address == null || address.isBlank()) {
                    address = userDetails[3];
                }
                if (birthdate == null || birthdate.isBlank()) {
                    birthdate = userDetails[4];
                }
                if (newID == null || newID.isBlank()) {
                    newID = userDetails[0];
                }
    
                if (!validate(personID, firstName, lastName, address, birthdate)) {
                    return false;
                }
                if (!validateID(newID)) {
                    System.out.println("new ID is invalid");
                    return false;
                }
    
                int firstDigit = Character.getNumericValue(personID.charAt(0));
                if (firstDigit % 2 == 0 && !newID.equals(personID)) {
                    System.out.println("Your ID will not be changed as the first character is an even number");
                    isEven = true;
                }
    
                String existingBirthdate = userDetails[4];
                String updatedLine = null;
    
                if (!existingBirthdate.strip().equals(birthdate.strip())) {
                    // Condition 2: birthday changes → no other change allowed
                    if (!newID.equals(userDetails[0]) || !firstName.equals(userDetails[1]) ||
                        !lastName.equals(userDetails[2]) || !address.equals(userDetails[3])) {
                        System.out.println("Cannot change other details if birthday is changed");
                        return false;
                    }
                    System.out.println("birthday differs from existing birthday, updating only birthday");
                    updatedLine = userDetails[0] + "," + userDetails[1] + "," + userDetails[2] + "," + userDetails[3] + "," + birthdate;
                } else {
                    // Condition 1: under 18 can't change address
                    boolean isUnder18 = false;
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        LocalDate birthDateCheckDate = LocalDate.parse(existingBirthdate, formatter);
                        LocalDate today = LocalDate.now();
                        if (today.minusYears(18).isBefore(birthDateCheckDate)) {
                            System.out.println("Your address will not be changed as you are under 18");
                            isUnder18 = true;
                            return false;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid birthdate format");
                    }
                    if (isUnder18) {
                        address = userDetails[3];
                    }
    
                    if (isEven) {
                        updatedLine = userDetails[0] + "," + firstName + "," + lastName + "," + address + "," + birthdate;
                    } else {
                        updatedLine = newID + "," + firstName + "," + lastName + "," + address + "," + birthdate;
                    }
                }
    
                // Preserve demerit points
                for (int i = 5; i < userDetails.length; i++) {
                    updatedLine += "," + userDetails[i];
                }
    
                lines.add(updatedLine);
    
                // Update in memory
                this.personID = isEven ? this.personID : newID;
                this.firstName = firstName;
                this.lastName = lastName;
                this.address = address;
                this.birthdate = birthdate;
    
            } else {
                System.out.println("Person with ID " + personID + " not found.");
                return false;
            }
    
        } catch (IOException e) {
            return false;
        }
        this.personID = isEven ? this.personID : newID;  
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;

        return true;
        // This method allows updating a given person's ID, firstName, lastName,
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
//|----------------------------------------------------------------------------------------------------|
//|----------------------------------------------------------------------------------------------------|
//|----------------------------------------------------------------------------------------------------|
public String addDemeritPoints(int demeritsToAdd, String offenceDate) {
    if (demeritsToAdd < 1 || demeritsToAdd > 6) {
        System.out.println("Demerit points must be between 1 and 6.");
        return "Failed";
    }
    if (!isValidDate(offenceDate)) {
        return "Failed";
    }
    if (this.birthdate == null || !validateBirthdate(this.birthdate)) {
        System.out.println("Birthdate is null or invalid.");
        return "Failed";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate offenceLocalDate = LocalDate.parse(offenceDate, formatter);

    // Age at offence
    LocalDate birthLocalDate = LocalDate.parse(this.birthdate, formatter);
    int age = offenceLocalDate.getYear() - birthLocalDate.getYear();
    if (birthLocalDate.plusYears(age).isAfter(offenceLocalDate)) {
        age--;
    }

    // Calculate total within 2 years
    int totalPoints = 0;
    LocalDate twoYearsAgo = offenceLocalDate.minusYears(2);
    for (Map.Entry<LocalDate, Integer> entry : demeritPoints.entrySet()) {
        LocalDate date = entry.getKey();
        int points = entry.getValue();
        if ((date.isEqual(twoYearsAgo) || date.isAfter(twoYearsAgo)) &&
            (date.isBefore(offenceLocalDate) || date.isEqual(offenceLocalDate))) {
            totalPoints += points;
        }
    }

    // Add new points
    totalPoints += demeritsToAdd;
    this.demeritPoints.put(offenceLocalDate, demeritsToAdd);
    System.out.println("TOTAL POINTS " + totalPoints);
    System.out.println("AGE " + age);

    // Update suspension
    if (age < 21) {
        if (totalPoints > 6) {
            this.isSuspended = true;
        } else {
            this.isSuspended = false;
        }
    } else {
        if (totalPoints > 12) {
            this.isSuspended = true;
        } else {
            this.isSuspended = false;
        }
    }
    return "Success";
}

    // This method adds demerit points for a given person in a TXT file.
    // Condition 1: The format of the date of the offense should follow the
    // following format: DD-MM-YYYY. Example: 15-11-1990
    // Condition 2: The demerit points must be a whole number between 1-6
    // Condition 3: If the person is under 21, the isSuspended variable should be
    // set to true if the total demerit points within two years exceed 6.
    // Condition 4: If the person is over 21, the isSuspended variable should be set to true if
    // the total demerit points within two years exceed 12.

    // Instruction: If the above condiations and any other conditions you may want
    // to consider are met, the demerit points for a person should be inserted into
    // the TXT file, //and the addDemerit Points function should return "Sucess".
    // Otherwise, the addDemeritPoints function should return "Failed". return
    // "Sucess";
    


    //|----------------------------------------------------------------------------------------------------|
//HELPER FUNCTIONS
    public boolean isPersonIDExists(String personID) {
        try (Scanner scanner = new Scanner(new FileInputStream("persons.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(personID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading persons.txt: " + e.getMessage());
        }
        return false;
    }
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
        if (digit1 < 2 || digit1 > 9 || digit2 < 2 || digit2 > 9) {
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
            System.out.println("");

            return false;
        }
        String[] addressParts = splitByPipe(address);
        if (addressParts == null || addressParts.length != 5) {
            System.out.println("Address must have 5 parts separated by '|'.");
            return false;
        }
    
        // Validate Street Number
        try {
            Integer.parseInt(addressParts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Street number must be a valid number.");
            return false;
        }
    
        // Validate Street Name
        if (addressParts[1].isEmpty()) {
            System.out.println("Street name is blank. Street name cannot be empty.");
            return false;
        }
    
        // Validate City
        if (addressParts[2].isEmpty()) {
            System.out.println("City is blank. City cannot be blank");
            return false;
        }
    
        // Validate State
        if (!addressParts[3].equals("Victoria")) {
            System.out.println("State must be 'Victoria'.");
            return false;
        }
    
        // Validate Country
        if (addressParts[4].isEmpty()) {
            System.out.println("Country is blank. Country cannot be empty.");
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
    public String getPersonID(){ return this.personID;}
    public boolean isValidDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (Exception e) {
            System.out.println("Date format invalid: " + dateStr);
            return false;
        }
    }

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
public static void clearPersonsFile2() {
    try {
        FileWriter fw = new FileWriter("persons.txt", false);  
        fw.write("");   
        fw.close();
        System.out.println("persons.txt has been cleared.");
    } catch (IOException e) {
        System.out.println("Failed to clear persons.txt: " + e.getMessage());
    }
}
}
