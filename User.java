//import java.util.*;
//import java.io.*;
//class User {
//    private final String USER_FILE = "Data/users.txt";
//    private Scanner scanner = new Scanner(System.in);
//    private String loggedUserId = null;
//
//    public boolean login(String username, String password) {
//        try {
//            File file = new File(USER_FILE);
//            if (!file.exists()) return false;
//            Scanner reader = new Scanner(file);
//            while (reader.hasNextLine()) {
//                String[] data = reader.nextLine().split(",");
//                if (data.length >= 3 && data[1].equals(username) && data[2].equals(password)) {
//                    loggedUserId = data[0];
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Login error: " + e.getMessage());
//        }
//        return false;
//    }
//
//    public void userMenu() {
//        int choice;
//        do {
//            System.out.println("\n--- User Panel ---");
//            System.out.println("1. View Profile");
//            System.out.println("2. Update Info (Except ID)");
//            System.out.println("3. Logout");
//            System.out.print("Choose: ");
//            choice = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (choice) {
//                case 1: viewProfile(); break;
//                case 2: updateInfo(); break;
//                case 3: System.out.println("Logging out..."); break;
//                default: System.out.println("Invalid option");
//            }
//        } while (choice != 3);
//    }
//
//    private void viewProfile() {
//        try {
//            File file = new File(USER_FILE);
//            Scanner reader = new Scanner(file);
//            while (reader.hasNextLine()) {
//                String[] data = reader.nextLine().split(",");
//                if (data[0].equals(loggedUserId)) {
//                    System.out.println("ID: " + data[0]);
//                    System.out.println("Username: " + data[1]);
//                    System.out.println("Password: " + data[2]);
//                    return;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    private void updateInfo() {
//        File inputFile = new File(USER_FILE);
//        File tempFile = new File("temp_users.txt");
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
//            String line;
//            boolean updated = false;
//
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data[0].equals(loggedUserId)) {
//                    System.out.print("Enter new username: ");
//                    String newUsername = scanner.nextLine();
//                    System.out.print("Enter new password: ");
//                    String newPassword = scanner.nextLine();
//                    writer.println(data[0] + "," + newUsername + "," + newPassword);
//                    updated = true;
//                } else {
//                    writer.println(line);
//                }
//            }
//
//            writer.close();
//            reader.close();
//            inputFile.delete();
//            tempFile.renameTo(inputFile);
//
//            if (updated)
//                System.out.println("Info updated.");
//            else
//                System.out.println("User not found.");
//        } catch (IOException e) {
//            System.out.println("Update error: " + e.getMessage());
//        }
//    }
//}


import java.io.*;
import java.util.*;

public class User {
    private final String USER_FILE = "Data/users.txt";
    private String loggedUserId = null;

    public boolean login(String username, String password) {
        try {
            File file = new File(USER_FILE);
            if (!file.exists()) return false;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 3 && data[1].equals(username) && data[2].equals(password)) {
                    loggedUserId = data[0];
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public String[] getProfile() {
        try {
            File file = new File(USER_FILE);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(loggedUserId)) {
                    return data;
                }
            }
        } catch (Exception e) {
            System.out.println("Profile error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateInfoGUI(String newUsername, String newPassword) {
        File inputFile = new File(USER_FILE);
        File tempFile = new File("temp_users.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(loggedUserId)) {
                    writer.println(data[0] + "," + newUsername + "," + newPassword);
                    updated = true;
                } else {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Update GUI error: " + e.getMessage());
            return false;
        }

        if (updated) {
            if (!inputFile.delete()) {
                System.out.println("Could not delete original file. Maybe it's open in another program?");
                System.out.println("File absolute path: " + inputFile.getAbsolutePath());
                return false;
            }
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file.");
                return false;
            }
        } else {
            tempFile.delete(); // Clean up
        }

        return updated;
    }

}
