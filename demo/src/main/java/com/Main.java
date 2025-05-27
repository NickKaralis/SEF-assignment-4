package com;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Person> persons = new ArrayList<Person>();
        read(persons);

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
                System.out.println("Please enter the address (format: street|city|state|zip|country):");
                String address = scanner.nextLine();
                System.out.println("Please enter the birthdate (YYYY-MM-DD):");
                String birthdate = scanner.nextLine();

                Person person = new Person();
                boolean isAdded = person.addPerson(personID, firstName, lastName, address, birthdate);
                if (!isAdded) {
                    System.out.println("Failed to add person. Please check the input values.");
                    continue; // Ask for input again
                }

                persons.add(person); // Add the person to the list
                write(persons); // Write the updated list to the file)

                // Here we need to add to method to add a person
                System.out.println("Person added with ID: " + personID);

            } else if (option.equals("updatePersonalDetails") || option.equals("2")) {
                System.out.println("Please enter the person ID:");
                String personID = scanner.nextLine();
                System.out.println("Please enter the new ID:");
                String newID = scanner.nextLine();
                System.out.println("Please enter the new first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the new last name:");
                String lastName = scanner.nextLine();
                System.out.println("Please enter the new address (format: street|city|state|zip|country):");
                String address = scanner.nextLine();
                System.out.println("Please enter the new birthdate (YYYY-MM-DD):");
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

                if (person.updatePersonalDetails(personID, newID, firstName, lastName, address, birthdate)) {
                    write(persons);
                    System.out.println("Personal details updated successfully.");
                } else {
                    System.out.println("Failed to update personal details. Please check the input values.");
                    continue; // Ask for input again
                }

                // Here we need to call update personal details
                System.out.println("Personal details updated for person ID: " + personID);

            } else if (option.equals("add demerit points") || option.equals("3")) {
                System.out.println("Please enter the person ID:");
                String personID = scanner.nextLine();
                System.out.println("Please enter the offense date (YYYY-MM-DD):");
                String offenseDate = scanner.nextLine();
                System.out.println("Please enter the demerit points:");
                int demeritPoints = Integer.parseInt(scanner.nextLine());

                // Here we need to call add demerit points
                System.out.println("Demerit points added for person ID: " + personID);

            } else if (option.equals("exit") || option.equals("4")) {
                System.out.println("Exiting the program.");
                scanner.close();
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

    }

    public static void read(List<Person> persons) {
        try {
            FileInputStream fis = new FileInputStream("persons.txt");
            Scanner scanner = new Scanner(fis);

            String userLine = null;

            // add all users except the one editting to to lines
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] userDetails = line.split(",", -1);
                Person person = new Person();

                person.personID = userDetails[0];
                person.firstName = userDetails[1];
                person.lastName = userDetails[2];
                person.address = userDetails[3];
                person.birthdate = userDetails[4];

                for (int i = 5; i < userDetails.length; i += 2) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate birthDateCheckDate = LocalDate.parse(userDetails[i], formatter);

                    int amount = Integer.parseInt(userDetails[i + 1]);
                    person.demeritPoints.put(birthDateCheckDate, amount);
                }

            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

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
                for (LocalDate date : p.demeritPoints.keySet()) {
                    line.append(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
                    line.append(p.demeritPoints.get(date)).append(",");
                }

                ps.println(line);
            }

            ps.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
            return;
        }
    }

}