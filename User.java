
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
            // Release locks
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}

            if (!inputFile.delete()) {
                System.out.println("Could not delete original file. File: " + inputFile.getAbsolutePath());
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
