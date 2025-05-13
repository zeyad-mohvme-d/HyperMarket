//public class Admin {
//    private String username = 'admin';
//    private String password = 'admin12345';
//
//    Admin(){
//
//    }
//
//    Admin(String username, String password){
//        this.username = username;
//        this.password = password;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//    public void AddEmployee(int id, String password){
//
//    }
//}

import java.io.*;
import java.util.*;

public class Admin {

    Scanner sc = new Scanner(System.in);
    File employeeFile = new File("data/employees.txt");

    public void start() {
        System.out.println("==== Admin Panel ====");
        while (true) {
            System.out.println("\n1. Add Employee\n2. List Employees\n3. Delete Employee\n4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addEmployee(); break;
                case 2: listEmployees(); break;
                case 3: deleteEmployee(); break;
                case 4: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void addEmployee() {
        try {
            System.out.print("ID: ");
            String id = sc.nextLine();
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();
            System.out.print("Type: ");
            String type = sc.nextLine();

            BufferedWriter bw = new BufferedWriter(new FileWriter(employeeFile, true));
            bw.write(id + "," + name + "," + pass + "," + type);
            bw.newLine();
            bw.close();
            System.out.println("Employee Added!");
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }

    private void listEmployees() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(employeeFile));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    private void deleteEmployee() {
        try {
            System.out.print("Enter ID to delete: ");
            String id = sc.nextLine();

            File tempFile = new File("data/temp.txt");
            BufferedReader br = new BufferedReader(new FileReader(employeeFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(id + ",")) {
                    bw.write(line);
                    bw.newLine();
                } else {
                    found = true;
                }
            }

            br.close();
            bw.close();

            if (found) {
                employeeFile.delete();
                tempFile.renameTo(employeeFile);
                System.out.println("Employee Deleted.");
            } else {
                System.out.println("ID not found.");
            }

        } catch (IOException e) {
            System.out.println("Error updating file.");
        }
    }
}
