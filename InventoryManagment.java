import java.util.*;
import java.io.*;
import java.io.IOException;
class Inventory {

    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private final String PRODUCT_FILE = "Data/products.txt";
    private final String DAMAGED_FILE = "Data/damaged.txt";
    private final String NOTIFICATION_FILE = "notifications.txt";

    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return false;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password) && data[3].equals("inventory")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void inventoryMenu() {
        int choice;
        do {
            System.out.println("\n--- Inventory Panel ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. List Products");
            System.out.println("5. Search Product");
            System.out.println("6. Add Damaged/Returned Product");
            System.out.println("7. Check Notifications");
            System.out.println("8. Logout");
            System.out.print("Choose: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: addProduct(); break;
                case 2: updateProduct(); break;
                case 3: deleteProduct(); break;
                case 4: listProducts(); break;
                case 5: searchProduct(); break;
                case 6: addDamaged(); break;
                case 7: checkNotifications(); break;
                case 8: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid option");
            }
        } while (choice != 8);
    }

    private void addProduct() {
        try {
            System.out.print("Product ID: ");
            String id = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Quantity: ");
            int qty = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Expiry (YYYY-MM-DD): ");
            String expiry = scanner.nextLine();

            FileWriter fw = new FileWriter(PRODUCT_FILE, true);
            fw.write(id + "," + name + "," + qty + "," + expiry + "\n");
            fw.close();
            System.out.println("Product added.");
        } catch (IOException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private void listProducts() {
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                System.out.println("No products available.");
                return;
            }
            Scanner reader = new Scanner(file);
            System.out.println("Product List:");
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                System.out.println("ID: " + data[0] + ", Name: " + data[1] + ", Qty: " + data[2] + ", Expiry: " + data[3]);
            }
        } catch (Exception e) {
            System.out.println("Error listing products: " + e.getMessage());
        }
    }

    private void searchProduct() {
        System.out.print("Enter product ID to search: ");
        String searchId = scanner.nextLine();
        boolean found = false;
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) return;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(searchId)) {
                    System.out.println("FOUND -> Name: " + data[1] + ", Qty: " + data[2] + ", Expiry: " + data[3]);
                    found = true;
                }
            }
            if (!found) System.out.println("Product not found.");
        } catch (Exception e) {
            System.out.println("Search error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        System.out.print("Enter Product ID to update: ");
        String updateId = scanner.nextLine();
        File inputFile = new File(PRODUCT_FILE);
        File tempFile = new File("temp_inventory.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(updateId)) {
                    System.out.print("New Name: ");
                    String name = scanner.nextLine();
                    System.out.print("New Quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("New Expiry (YYYY-MM-DD): ");
                    String expiry = scanner.nextLine();
                    writer.println(updateId + "," + name + "," + qty + "," + expiry);
                    updated = true;
                } else {
                    writer.println(line);
                }
            }

            reader.close();
            writer.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);

            if (updated)
                System.out.println("Product updated.");
            else
                System.out.println("Product ID not found.");

        } catch (IOException e) {
            System.out.println("Update error: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String deleteId = scanner.nextLine();
        File inputFile = new File(PRODUCT_FILE);
        File tempFile = new File("temp_inventory.txt");

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

            reader.close();
            writer.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);

            if (deleted)
                System.out.println("Product deleted.");
            else
                System.out.println("Product ID not found.");

        } catch (IOException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }

    private void addDamaged() {
        try {
            System.out.print("Product ID: ");
            String id = scanner.nextLine();
            System.out.print("Reason (returned/damaged): ");
            String reason = scanner.nextLine();

            FileWriter fw = new FileWriter(DAMAGED_FILE, true);
            fw.write(id + "," + reason + "\n");
            fw.close();
            System.out.println("Logged as damaged/returned.");
        } catch (IOException e) {
            System.out.println("Error logging item: " + e.getMessage());
        }
    }

    private void checkNotifications() {
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                System.out.println("No products to check.");
                return;
            }

            PrintWriter notifier = new PrintWriter(new FileWriter(NOTIFICATION_FILE));
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                int qty = Integer.parseInt(data[2]);
                String expiry = data[3];
                if (qty < 10) {
                    notifier.println("\"Low stock alert: \" + data[1] + \" (\" + data[0] + \") has only \" + qty + \" units.\"");
                }

                if (expiry.compareTo("\"2025-01-01\"") < 0) {
                        notifier.println("\"Expiry alert: \" + data[1] + \" (\" + data[0] + \") expiring soon on \" + expiry + \".\"");
            }
        }
        notifier.close();
        System.out.println("\"Notifications written to \" "+ NOTIFICATION_FILE);
    } catch (Exception e) {
        System.out.println("\"Notification error: \" "+ e.getMessage());
    }
}
}
