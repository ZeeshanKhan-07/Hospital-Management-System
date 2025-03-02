package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.println("Enter Patient Name: ");
        scanner.nextLine(); // Consume leftover newline
        String name = scanner.nextLine();
        
        System.out.println("Enter Patient Age: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid age:");
            scanner.next(); // Consume invalid input
        }
        int age = scanner.nextInt();
        
        System.out.println("Enter Patient Gender: ");
        scanner.nextLine(); // Consume newline before reading string
        String gender = scanner.nextLine();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, gender);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Patient Added Successfully");
                } else {
                    System.out.println("Failed to add Patient!!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultset = preparedStatement.executeQuery()) {
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+-----------+------------+");
            System.out.println("| Patient ID |        Name        |    Age    |   Gender   |");
            System.out.println("+------------+--------------------+-----------+------------+");

            while (resultset.next()) {
                int id = resultset.getInt("id");
                String name = resultset.getString("name");
                int age = resultset.getInt("age");
                String gender = resultset.getString("gender");
                System.out.printf("| %-10s | %-18s | %-9s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+-----------+------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultset = preparedStatement.executeQuery()) {
                return resultset.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
