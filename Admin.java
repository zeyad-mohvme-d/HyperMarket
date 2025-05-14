////public class Admin {
////    private String username = 'admin';
////    private String password = 'admin12345';
////
////    Admin(){
////
////    }
////
////    Admin(String username, String password){
////        this.username = username;
////        this.password = password;
////    }
////
////    public String getUsername() {
////        return username;
////    }
////
////    public String getPassword() {
////        return password;
////    }
////
////    public void setUsername(String username) {
////        this.username = username;
////    }
////
////    public void setPassword(String password) {
////        this.password = password;
////    }
////    public void AddEmployee(int id, String password){
////
////    }
////}
//
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
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Add Employee");
            System.out.println("2. List Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addEmployee(); break;
                case 2: listEmployees(); break;
                case 3: searchEmployee(); break;
                case 4: deleteEmployee(); break;
                case 5: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid option");
            }
        } while (choice != 5);
    }

    private void addEmployee() {
        try {
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Username: ");
            String user = scanner.nextLine();
            System.out.print("Enter Password: ");
            String pass = scanner.nextLine();
            System.out.print("Enter Role (admin/marketing/inventory/sales): ");
            String role = scanner.nextLine();

            FileWriter fw = new FileWriter(EMPLOYEE_FILE, true);
            fw.write(id + "," + user + "," + pass + "," + role + "\n");
            fw.close();
            System.out.println("Employee added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    private void listEmployees() {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) {
                System.out.println("No employees yet.");
                return;
            }
            Scanner reader = new Scanner(file);
            System.out.println("\n--- Employee List ---");
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4)
                    System.out.println("ID: " + data[0] + ", Username: " + data[1] + ", Role: " + data[3]);
            }
        } catch (Exception e) {
            System.out.println("Error listing employees: " + e.getMessage());
        }
    }

    private void searchEmployee() {
        System.out.print("Enter ID to search: ");
        String searchId = scanner.nextLine();
        boolean found = false;
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(searchId)) {
                    System.out.println("Found: Username = " + data[1] + ", Role = " + data[3]);
                    found = true;
                    break;
                }
            }
            if (!found) System.out.println("Employee not found.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        System.out.print("Enter ID to delete: ");
        String deleteId = scanner.nextLine();
        File inputFile = new File(EMPLOYEE_FILE);
        File tempFile = new File("temp_employees.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean deleted = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(deleteId)) {
                    writer.println(line);
                } else {
                    deleted = true;
                }
            }

            writer.close();
            reader.close();
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                if (deleted)
                    System.out.println("Employee deleted.");
                else
                    System.out.println("ID not found.");
            }
        } catch (IOException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}


