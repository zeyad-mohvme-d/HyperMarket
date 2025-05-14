import java.util.*;
import java.io.*;
class Sales {
    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private final String PRODUCT_FILE = "Data/products.txt";
    private final String ORDER_FILE = "Data/orders.txt";
    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return false;

            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password) && data[3].equals("sales")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void salesMenu() {
        int choice;
        do {
            System.out.println("\n--- Sales Panel ---");
            System.out.println("1. List All Products");
            System.out.println("2. Search Product");
            System.out.println("3. Place Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: listProducts(); break;
                case 2: searchProduct(); break;
                case 3: placeOrder(); break;
                case 4: cancelOrder(); break;
                case 5: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid option");
            }
        } while (choice != 5);
    }

    private void listProducts() {
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                System.out.println("No products available.");
                return;
            }
            Scanner reader = new Scanner(file);
            System.out.println("=== Product List ===");
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error reading products: " + e.getMessage());
        }
    }

    private void searchProduct() {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().toLowerCase();
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                System.out.println("No products available.");
                return;
            }
            Scanner reader = new Scanner(file);
            boolean found = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println("MATCH: " + line);
                    found = true;
                }
            }
            if (!found) System.out.println("No match found.");
        } catch (Exception e) {
            System.out.println("Error searching product: " + e.getMessage());
        }
    }

    private void placeOrder() {
        try {
            System.out.print("Enter Order ID: ");
            String orderId = scanner.nextLine();
            System.out.print("Enter Product ID: ");
            String productId = scanner.nextLine();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Customer Name: ");
            String customer = scanner.nextLine();

            FileWriter fw = new FileWriter(ORDER_FILE, true);
            fw.write(orderId + "," + productId + "," + quantity + "," + customer + "\n");
            fw.close();

            System.out.println("Order placed.");
        } catch (IOException e) {
            System.out.println("Error placing order: " + e.getMessage());
        }
    }

    private void cancelOrder() {
        System.out.print("Enter Order ID to cancel: ");
        String cancelId = scanner.nextLine();
        File inputFile = new File(ORDER_FILE);
        File tempFile = new File("temp_orders.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(cancelId)) {
                    writer.println(line);
                } else {
                    found = true;
                }
            }

            reader.close();
            writer.close();
            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                if (found) System.out.println("Order canceled.");
                else System.out.println("Order ID not found.");
            }
        } catch (IOException e) {
            System.out.println("Error canceling order: " + e.getMessage());
        }
    }
}
