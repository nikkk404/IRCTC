/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.example.services.UserBookingService;

import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println("Running...");
        Scanner sc = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService = null;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("Error initializing UserBookingService: " + ex.getMessage());
            return;
        }

        while (option != 7) {
            System.out.println("Choose option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Booking");
            System.out.println("4. Search Train");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my booking");
            System.out.println("7. Exit");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Signing up...");
                    System.out.println("Enter the username to sign up:");
                    String nameToSignUp = scanner.next();

                    System.out.println("Enter the password to sign up:");
                    String passwordToSignUp = scanner.next();

                    // Hash the password securely
                    String hashedPassword = UserServiceUtil.hashPassword(passwordToSignUp);

                    // Generate unique user ID
                    String userId = UUID.randomUUID().toString();

                    // Create User object
                    User userToSignUp = new User();
                    userToSignUp.setName(nameToSignUp);
                    userToSignUp.setPassword(passwordToSignUp);  // Storing raw password (Not recommended)
                    userToSignUp.setHashedPassword(hashedPassword);
                    userToSignUp.setUserId(userId);
                    userToSignUp.setTicketsBooked(new ArrayList<>());

                    // Call signup service
                    userBookingService.signUp(userToSignUp);

                    System.out.println("Signup successful!");
                    break;
                case 2:
                    System.out.println("Logging in...");
                    System.out.println("Enter the username to login:");
                    String nameToLogin = scanner.next();

                    System.out.println("Enter the password to login:");
                    String passwordToLogin = scanner.next();

                    // Hash the entered password for comparison
                    String hashedPassword = UserServiceUtil.hashPassword(passwordToLogin);

                    try {
                        // Authenticate user
                        boolean isAuthenticated = userBookingService.login(nameToLogin, hashedPassword);

                        if (isAuthenticated) {
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                    } catch (IOException ex) {
                        System.out.println("Error during login: " + ex.getMessage());
                        return;
                    }

                    break;
                case 3:
                    System.out.println("Fetching Booking...");
                   userBookingService.fetchBooking();
                    break;
                case 4:
                    System.out.println("Searching for Trains...");
                    System.out.println("Type your source station:");
                    String source = scanner.next();

                    System.out.println("Type your destination station:");
                    String dest = scanner.next();

                    // Fetch trains between the given source and destination
                    List<Train> trains = userBookingService.getTrains(source, dest);

                    // Check if no trains are found
                    if (trains == null || trains.isEmpty()) {
                        System.out.println("No trains available between " + source + " and " + dest);
                        break;
                    }

                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train ID: " + t.getTrainId());

                        // Ensure station time data exists before iterating
                        if (t.getStationTimes() != null) {
                            for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                                System.out.println("   Station: " + entry.getKey() + " | Time: " + entry.getValue());
                            }
                        }
                        index++;
                    }

                    System.out.println("Select a train by typing its index (1,2,3...):");
                    int selectedIndex = scanner.nextInt();

                    // Validate train selection
                    if (selectedIndex < 1 || selectedIndex > trains.size()) {
                        System.out.println("Invalid selection. Please try again.");
                        break;
                    }

                    Train selectedTrain = trains.get(selectedIndex - 1);
                    System.out.println("You selected Train ID: " + selectedTrain.getTrainId());

                    break;
                case 5:
                    System.out.println("Booking a Seat...");
                    if (trainSelectedForBooking == null) {
                        System.out.println("No train selected. Please search and select a train first.");
                        break;
                    }

                    // Display available seats
                    System.out.println("Available Seats:");
                    for (List<Integer> row : seats) {
                        for (Integer val : row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }

                    // Ask for seat selection
                    System.out.println("Select the seat by typing the row and column:");
                    System.out.print("Enter the row: ");
                    int row = scanner.nextInt();

                    System.out.print("Enter the column: ");
                    int col = scanner.nextInt();

                    // Validate row and column input (assuming seats is a List<List<Integer>>)
                    if (row < 0 || row >= seats.size() || col < 0 || col >= seats.get(row).size()) {
                        System.out.println("Invalid seat selection. Please enter a valid row and column.");
                        break;
                    }

                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);

                    if (Boolean.TRUE.equals(booked)) {
                        System.out.println("Booked! Enjoy your journey.");
                    } else {
                        System.out.println("Can't book this seat. It may already be taken.");
                    }

                    break;
                case 6:
                    System.out.println("Cancelling Booking...");
                    // Implement booking cancellation logic here
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option! Please choose a valid number.");
            }
        }

        sc.close();
    }
}
