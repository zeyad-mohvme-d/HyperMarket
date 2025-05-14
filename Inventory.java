import javax.swing.*;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Inventory {
    private final String PRODUCT_FILE = "Data/products.txt";  // Path to products file
    private final String EMPLOYEE_FILE = "Data/employees.txt"; // Path to employees file
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
        String[] options = { "Add Product", "Update Product", "Delete Product", "List Products", "Search Product", "Logout" };
        int choice = JOptionPane.showOptionDialog(null, "Choose an option", "Inventory Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> addProduct();  // Add Product
            case 1 -> updateProduct();  // Update Product
            case 2 -> deleteProduct();  // Delete Product
            case 3 -> listProducts();  // List Products
            case 4 -> searchProduct();  // Search Product
            case 5 -> JOptionPane.showMessageDialog(null, "Logging out...");  // Logout
            default -> JOptionPane.showMessageDialog(null, "Invalid option");
        }
    }

    private void addProduct() {
        try {
            String id = JOptionPane.showInputDialog("Product ID:");
            String name = JOptionPane.showInputDialog("Name:");
            String qtyStr = JOptionPane.showInputDialog("Quantity:");
            int qty = Integer.parseInt(qtyStr);
            String expiry = JOptionPane.showInputDialog("Expiry (YYYY-MM-DD):");

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Product ID and Name cannot be empty.");
                return;
            }

            FileWriter fw = new FileWriter(PRODUCT_FILE, true);
            fw.write(id + "," + name + "," + qty + "," + expiry + "\n");
            fw.close();
            JOptionPane.showMessageDialog(null, "Product added.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding product: " + e.getMessage());
        }
    }

    private void listProducts() {
        try (Scanner reader = new Scanner(new File(PRODUCT_FILE))) {
            StringBuilder productList = new StringBuilder("\n--- Product List ---\n");
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length == 4) {
                    productList.append("ID: ").append(data[0]).append(", Name: ").append(data[1])
                            .append(", Qty: ").append(data[2]).append(", Expiry: ").append(data[3]).append("\n");
                }
            }
            JOptionPane.showMessageDialog(null, productList.toString());
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No products available.");
        }
    }

    private void searchProduct() {
        String searchId = JOptionPane.showInputDialog("Enter product ID to search:");
        boolean found = false;

        try (Scanner reader = new Scanner(new File(PRODUCT_FILE))) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data[0].equals(searchId)) {
                    JOptionPane.showMessageDialog(null, "FOUND -> Name: " + data[1] + ", Qty: " + data[2] + ", Expiry: " + data[3]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Product not found.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Search error: " + e.getMessage());
        }
    }

    private void updateProduct() {
        String updateId = JOptionPane.showInputDialog("Enter Product ID to update:");
        File inputFile = new File(PRODUCT_FILE);
        File tempFile = new File("temp_inventory.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(updateId)) {
                    String name = JOptionPane.showInputDialog("New Name:");
                    String qtyStr = JOptionPane.showInputDialog("New Quantity:");
                    int qty = Integer.parseInt(qtyStr);
                    String expiry = JOptionPane.showInputDialog("New Expiry (YYYY-MM-DD):");
                    writer.println(updateId + "," + name + "," + qty + "," + expiry);
                    updated = true;
                } else {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Update error: " + e.getMessage());
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        JOptionPane.showMessageDialog(null, updated ? "Product updated." : "Product ID not found.");
    }

    private void deleteProduct() {
        String deleteId = JOptionPane.showInputDialog("Enter Product ID to delete:");
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
            JOptionPane.showMessageDialog(null, "Delete error: " + e.getMessage());
            return;
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        JOptionPane.showMessageDialog(null, deleted ? "Product deleted." : "Product ID not found.");
    }
}
