import javax.swing.*;
import java.io.*;
import java.util.*;

class Admin {
    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return false;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password) && data[3].equals("admin")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void adminMenu() {
        int choice;
        do {
            String options = "\n--- Admin Panel ---\n" +
                    "1. Add Employee\n" +
                    "2. List Employees\n" +
                    "3. Search Employee\n" +
                    "4. Delete Employee\n" +
                    "5. Update Employee\n" +
                    "6. Change Username/Password\n" +
                    "7. Logout\n" +
                    "Choose: ";
            choice = Integer.parseInt(JOptionPane.showInputDialog(options));

            switch (choice) {
                case 1: addEmployee(); break;
                case 2: listEmployees(); break;
                case 3: searchEmployee(); break;
                case 4: deleteEmployee(); break;
                case 5: updateEmployee(); break;
                case 6: changeAdminCredentials(); break;
                case 7: JOptionPane.showMessageDialog(null, "Logging out..."); break;
                default: JOptionPane.showMessageDialog(null, "Invalid option");
            }
        } while (choice != 7);
    }

    private void addEmployee() {
        try {
            String id = JOptionPane.showInputDialog("Enter ID: ");
            String user = JOptionPane.showInputDialog("Enter Username: ");
            String pass = JOptionPane.showInputDialog("Enter Password: ");
            String role = JOptionPane.showInputDialog("Enter Role (admin/marketing/inventory/sales): ");

            FileWriter fw = new FileWriter(EMPLOYEE_FILE, true);
            fw.write(id + "," + user + "," + pass + "," + role + "\n");
            fw.close();
            JOptionPane.showMessageDialog(null, "Employee added successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error adding employee: " + e.getMessage());
        }
    }

    private void listEmployees() {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No employees yet.");
                return;
            }
            Scanner reader = new Scanner(file);
            StringBuilder sb = new StringBuilder("\n--- Employee List ---\n");
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4)
                    sb.append("ID: ").append(data[0]).append(", Username: ").append(data[1]).append(", Role: ").append(data[3]).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error listing employees: " + e.getMessage());
        }
    }

    private void searchEmployee() {
        String searchId = JOptionPane.showInputDialog("Enter ID to search: ");
        boolean found = false;
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(searchId)) {
                    JOptionPane.showMessageDialog(null, "Found: Username = " + data[1] + ", Role = " + data[3]);
                    found = true;
                    break;
                }
            }
            if (!found) JOptionPane.showMessageDialog(null, "Employee not found.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void updateEmployee() {
        String targetId = JOptionPane.showInputDialog("Enter the ID of the employee to update:");

        File inputFile = new File(EMPLOYEE_FILE);
        File tempFile = new File("data/temp.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(targetId)) {
                    String newId = JOptionPane.showInputDialog("Enter new ID:", data[0]);
                    String newRole = JOptionPane.showInputDialog("Enter new role (admin/marketing/inventory/sales):", data[3]);

                    writer.println(newId + "," + data[1] + "," + data[2] + "," + newRole);
                    updated = true;
                } else {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return;
        }

        if (updated) {
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}

            if (!inputFile.delete()) {
                JOptionPane.showMessageDialog(null, "Failed to delete original file.");
                return;
            }
            if (!tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(null, "Failed to rename temp file.");
                return;
            }

            JOptionPane.showMessageDialog(null, "Employee info updated.");
        } else {
            tempFile.delete();
            JOptionPane.showMessageDialog(null, "Employee ID not found.");
        }
    }

    private void deleteEmployee() {
        String deleteId = JOptionPane.showInputDialog("Enter ID to delete: ");
        File originalFile = new File("data/employees.txt");
        File tempFile = new File("data/temp.txt");
        boolean deleted = false;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(originalFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(deleteId)) {
                    writer.println(line);
                } else {
                    deleted = true;
                }
            }

            reader.close();
            writer.close();

            System.gc();
            Thread.sleep(100);

            if (originalFile.delete()) {
                if (tempFile.renameTo(originalFile)) {
                    JOptionPane.showMessageDialog(null, "Employee deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to rename temp file.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete original file: " + originalFile.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void changeAdminCredentials() {
        String currentUser = JOptionPane.showInputDialog("Enter current username:");
        String currentPass = JOptionPane.showInputDialog("Enter current password:");

        File inputFile = new File(EMPLOYEE_FILE);
        File tempFile = new File("data/temp.txt");
        boolean changed = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[1].equals(currentUser) && data[2].equals(currentPass) && data[3].equals("admin")) {
                    String newUser = JOptionPane.showInputDialog("Enter new username:", data[1]);
                    String newPass = JOptionPane.showInputDialog("Enter new password:", data[2]);
                    writer.println(data[0] + "," + newUser + "," + newPass + "," + data[3]);
                    changed = true;
                } else {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return;
        }

        if (changed) {
            System.gc();
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}

            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(null, "Credentials updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to apply changes.");
            }
        } else {
            tempFile.delete();
            JOptionPane.showMessageDialog(null, "Admin credentials not found or incorrect.");
        }
    }
}
