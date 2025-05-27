package com;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

                // Here we need to add to method to add a person
                System.out.println("Person added with ID: " + personID);

            } else if (option.equals("updatePersonalDetails") || option.equals("2")) {
                System.out.println("Please enter the person ID:");
                String personID = scanner.nextLine();
                System.out.println("Please enter the new first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter the new last name:");
                String lastName = scanner.nextLine();
                System.out.println("Please enter the new address (format: street|city|state|zip|country):");
                String address = scanner.nextLine();
                System.out.println("Please enter the new birthdate (YYYY-MM-DD):");
                String birthdate = scanner.nextLine();

                Person person = new Person();
                boolean isAdded = person.updatePersonalDetails(personID, firstName, lastName, address, birthdate);
                if (!isAdded) {
                    System.out.println("Failed to cahnge person. Please check the input values.");
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

}
