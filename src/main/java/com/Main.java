package com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Person> persons = new ArrayList<Person>();
        read(persons); // Load existing person records from file into the list
        for (int i = 0; i < persons.size(); i++) {
            System.out.println("DOCUMENT CURRENTPERSON ID: " + persons.get(i).getPersonID());
        }

        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("1. add person");
            System.out.println("2. updatePersonalDetails");
            System.out.println("3. add demerit points");
            System.out.println("4. exit");
            String option = scanner.nextLine();

            if (option.equals("add person") || option.equals("1")) {
                System.out.println("Please enter the person ID (10 characters):");
                String personID = scanner.nextLine();
                System.out.println("Please enter the first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the last name:");
                String lastName = scanner.nextLine();
                System.out
                        .println("Please enter the address (format: street number | street | city | state | country):");
                String address = scanner.nextLine();
                System.out.println("Please enter the birthdate (DD-MM-YYYY):");
                String birthdate = scanner.nextLine();

                Person person = new Person();
                boolean isAdded = person.addPerson(personID, firstName, lastName, address, birthdate);
                if (!isAdded) {
                    System.out.println("Failed to add person. Please check the input values.");
                    continue; // Ask for input again
                }

                persons.add(person); // Add the person to the list
                write(persons); // Write the updated list to the file

                // Here we need to add to method to add a person
                System.out.println("Person added with ID: " + personID);

            } else if (option.equals("updatePersonalDetails") || option.equals("2")) {
                System.out.println("Press Enter to Skip changing that particular field");

                System.out.println("Please enter the person ID:");
                String personID = scanner.nextLine();
                System.out.println("Please enter the new ID:");
                String newID = scanner.nextLine();
                System.out.println("Please enter the new first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the new last name:");
                String lastName = scanner.nextLine();
                System.out.println(
                        "Please enter the address new (format: street number | street | city | state | country):");
                String address = scanner.nextLine();
                System.out.println("Please enter new the birthdate (DD-MM-YYYY):");
                System.out.println("");
                String birthdate = scanner.nextLine();

                Person person = null;
                for (Person p : persons) {
                    if (p.personID.equals(personID)) {
                        person = p;
                        break;
                    }
                }

                if (person == null) {
                    System.out.println("Person with ID " + personID + " not found.");
                    continue; // Ask for input again
                }

                if (newID == null || newID.trim().isEmpty()) {
                    newID = person.personID;
                }
                if (firstName == null || firstName.trim().isEmpty()) {
                    firstName = person.firstName;
                }
                if (lastName == null || lastName.trim().isEmpty()) {
                    lastName = person.lastName;
                }
                if (address == null || address.trim().isEmpty()) {
                    address = person.address;
                }
                if (birthdate == null || birthdate.trim().isEmpty()) {
                    birthdate = person.birthdate;
                }

                if (person.updatePersonalDetails(personID, newID, firstName, lastName, address, birthdate)) {
                    write(persons);
                    System.out.println("Personal details updated successfully.");
                } else {
                    System.out.println("Failed to update personal details. Please check the input values.");
                    continue; // Ask for input again
                }

                // Here we need to call update personal details
                System.out.println("Personal details updated for person ID: " + personID);

                // ---------------------------------------------------------------------------------------------------------------------------

            } else if (option.equals("add demerit points") || option.equals("3")) {
                System.out.println("Please enter the person ID:");
                String personID = scanner.nextLine();
                System.out.println("Please enter the offense date (DD-MM-YYYY):");
                String offenseDate = scanner.nextLine();
                System.out.println("Please enter the demerit points:");
                int demeritPoints = Integer.parseInt(scanner.nextLine());

                // GET INDEX OF THE PERSON IN THE LIST, IF FALSE BREAK FROM THE LOOP OR IF INDEX
                // IS -1
                // IF INDEX -1000 DO NOTHING
                int personListIndex = -1000;
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getPersonID().equals(personID)) {
                        personListIndex = i; // FOUND INDEX
                    }
                }
                if (personListIndex == -1000) {
                    System.err.println("Cant find index");
                } else {

                    // Here we need to call add demerit points
                    Person p = persons.get(personListIndex);
                    p.addDemeritPoints(demeritPoints, offenseDate);
                    write(persons);
                    System.out.println("Demerit points added for person ID: " + personID);
                }

            } else if (option.equals("exit") || option.equals("4")) {
                System.out.println("Exiting the program.");
                scanner.close();
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

    }
    
    // Read all persons from file and populate the list
    public static void read(List<Person> persons) {
        try {
            FileInputStream fis = new FileInputStream("persons.txt");
            Scanner scanner = new Scanner(fis);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty())
                    continue; // Safety cehck ts

                String[] userDetails = line.split(",", -1);
                if (userDetails.length < 5) {
                    System.out.println("Invalid line (too short): " + line);
                    continue;
                }

                Person person = new Person();
                person.personID = userDetails[0];
                person.firstName = userDetails[1];
                person.lastName = userDetails[2];
                person.address = userDetails[3];
                person.birthdate = userDetails[4];
                person.isSuspended = Boolean.parseBoolean(userDetails[5]);
                person.demeritPoints = new HashMap<>();

                for (int i = 6; i < userDetails.length - 1; i += 2) {
                    if (userDetails[i].isEmpty() || userDetails[i + 1].isEmpty())
                        continue;

                    try {
                        LocalDate offenceDate = LocalDate.parse(userDetails[i], formatter);
                        int points = Integer.parseInt(userDetails[i + 1]);
                        person.demeritPoints.put(offenceDate, points);
                    } catch (Exception e) {
                        System.out.println("Skipping bad demerit entry at: " + userDetails[i]);
                    }
                }

                persons.add(person);
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    // Write all person data to the file, overwriting existing content
    public static void write(List<Person> persons) {
        // Clear the contents of the file before writing updated data
        try (FileOutputStream fos = new FileOutputStream("persons.txt")) {
            // Opening the file in this mode clears its contents
        } catch (IOException e) {
            System.out.println("Error clearing file: " + e.getMessage());
            return;
        }

        // write back all lines to persons.txt
        try {
            FileOutputStream fos = new FileOutputStream("persons.txt", true);
            PrintStream ps = new PrintStream(fos);

            for (Person p : persons) {
                StringBuilder line = new StringBuilder();
                line.append(p.personID).append(",");
                line.append(p.firstName).append(",");
                line.append(p.lastName).append(",");
                line.append(p.address).append(",");
                line.append(p.birthdate).append(",");
                line.append(p.isSuspended);

                // Write demerit points if available
                if (p.demeritPoints != null && !p.demeritPoints.isEmpty()) {
                    List<LocalDate> sortedDates = new ArrayList<>(p.demeritPoints.keySet());
                    Collections.sort(sortedDates);
                    for (LocalDate date : sortedDates) {
                        int points = p.demeritPoints.get(date);
                        line.append(",").append(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                        line.append(",").append(points);
                    }
                }

                ps.println(line.toString());
            }

            ps.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return;
        }
    }

    public static void clearPersonsFile() {
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
