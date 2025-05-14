import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

class Inventory {
    private final String EMPLOYEE_FILE = "employees.txt";
    private final String PRODUCT_FILE = "products.txt";
    private final String DAMAGED_FILE = "damaged.txt";
    private final String NOTIFICATION_FILE = "notifications.txt";

    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try (Scanner reader = new Scanner(new File(EMPLOYEE_FILE))) {
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
                case 1 -> addProduct();
                case 2 -> updateProduct();
                case 3 -> deleteProduct();
                case 4 -> listProducts();
                case 5 -> searchProduct();
                case 6 -> addDamaged();
                case 7 -> checkNotifications();
                case 8 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid option");
            }
        } while (choice != 8);
    }

    private void addProduct() {
        try {
            System.out.print("Product ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Expiry (YYYY-MM-DD): ");
            String expiry = scanner.nextLine().trim();

            if (id.isEmpty() || name.isEmpty()) {
                System.out.println("Product ID and Name cannot be empty.");
                return;
            }

            FileWriter fw = new FileWriter(PRODUCT_FILE, true);
            fw.write(id + "," + name + "," + qty + "," + expiry + "\n");
            fw.close();
            System.out.println("Product added.");
        } catch (Exception e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private void listProducts() {
        try (Scanner reader = new Scanner(new File(PRODUCT_FILE))) {
            System.out.println("\n--- Product List ---");
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length == 4) {
                    System.out.println("ID: " + data[0] + ", Name: " + data[1] + ", Qty: " + data[2] + ", Expiry: " + data[3]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No products available.");
        }
    }

    private void searchProduct() {
        System.out.print("Enter product ID to search: ");
        String searchId = scanner.nextLine();
        boolean found = false;

        try (Scanner reader = new Scanner(new File(PRODUCT_FILE))) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(searchId)) {
                    System.out.println("FOUND -> Name: " + data[1] + ", Qty: " + data[2] + ", Expiry: " + data[3]);
                    found = true;
                    break;
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
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(updateId)) {
                    System.out.print("New Name: ");
                    String name = scanner.nextLine();
                    System.out.print("New Quantity: ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    System.out.print("New Expiry (YYYY-MM-DD): ");
                    String expiry = scanner.nextLine();
                    writer.println(updateId + "," + name + "," + qty + "," + expiry);
                    updated = true;
                } else {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Update error: " + e.getMessage());
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        System.out.println(updated ? "Product updated." : "Product ID not found.");
    }

    private void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String deleteId = scanner.nextLine();
        File inputFile = new File(PRODUCT_FILE);
        File tempFile = new File("temp_inventory.txt");
        boolean deleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(deleteId)) {
                    writer.println(line);
                } else {
                    deleted = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Delete error: " + e.getMessage());
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        System.out.println(deleted ? "Product deleted." : "Product ID not found.");
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
        try (Scanner reader = new Scanner(new File(PRODUCT_FILE));
             PrintWriter notifier = new PrintWriter(new FileWriter(NOTIFICATION_FILE))) {

            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                int qty = Integer.parseInt(data[2]);
                String expiryStr = data[3];

                if (qty < 10) {
                    notifier.println("Low stock alert: " + data[1] + " (" + data[0] + ") has only " + qty + " units.");
                }

                try {
                    LocalDate expiryDate = LocalDate.parse(expiryStr);
                    if (expiryDate.isBefore(LocalDate.now().plusDays(30))) {
                        notifier.println("Expiry alert: " + data[1] + " (" + data[0] + ") expiring soon on " + expiryDate + ".");
                    }
                } catch (DateTimeParseException e) {
                    notifier.println("Invalid expiry format for product: " + data[1]);
                }
            }

            System.out.println("Notifications written to " + NOTIFICATION_FILE);
        } catch (Exception e) {
            System.out.println("Notification error: " + e.getMessage());
        }
    }
}
