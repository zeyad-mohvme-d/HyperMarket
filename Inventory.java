import javax.swing.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Inventory {
    private final String PRODUCT_FILE = "Data/products.txt";  // Path to products file
    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private final String Damaged_FILE = "Data/damaged.txt";// Path to employees file
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
            String options = "\n--- Inventory Panel ---\n" +
                    "1. Add Product\n" +
                    "2. Update Product\n" +
                    "3. Delete Product\n" +
                    "4. List Products\n" +
                    "5. Search Product\n" +
                    "6. Notifications\n" +
                    "7. Damaged Items\n" +   // 🆕 Add this line
                    "8. Logout\n" +
                    "Choose: ";

            choice = Integer.parseInt(JOptionPane.showInputDialog(options));

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    deleteProduct();
                    break;
                case 4:
                    listProducts();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    notifyLowStock();
                    break;
                case 7:
                    damageItems();  // 🆕 Add this line
                    break;
                case 8:
                    JOptionPane.showMessageDialog(null, "Logging out...");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option");
            }

        } while (choice != 7);
    }

    private void addProduct() {
        try {
            String id = JOptionPane.showInputDialog("Product ID:");

            // 🔍 Check if ID already exists
            File productFile = new File(PRODUCT_FILE);
            Scanner reader = new Scanner(productFile);
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 1 && data[0].equals(id)) {
                    JOptionPane.showMessageDialog(null, "❌ Product ID already exists!");
                    reader.close();
                    return;
                }
            }
            reader.close();

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
            JOptionPane.showMessageDialog(null, "✅ Product added.");
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
                    String oldName = data[1];  // Keep the old name
                    String qtyStr = JOptionPane.showInputDialog("New Quantity:", data[2]); // Show old value by default
                    int qty = Integer.parseInt(qtyStr);
                    String expiry = JOptionPane.showInputDialog("New Expiry (YYYY-MM-DD):", data[3]); // Show old expiry
                    writer.println(updateId + "," + oldName + "," + qty + "," + expiry);
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

    public void notifyLowStock() {
        File productFile = new File("Data/products.txt");
        File notifyFile = new File("Data/notifications.txt");
        File offerFile = new File("Data/offers.txt");
        File offerIndexFile = new File("Data/offers_seen_index.txt");  // 🆕 track last offer index

        boolean anyLowStock = false;
        boolean anyNewOffer = false;

        StringBuilder fullNotification = new StringBuilder();
        StringBuilder lowStockSummary = new StringBuilder("⚠ Low Stock Items:\n");

        try (
                Scanner reader = new Scanner(productFile);
                PrintWriter writer = new PrintWriter(new FileWriter(notifyFile, true))
        ) {
            // 🔹 Check product quantities
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");

                if (data.length >= 3) {
                    String productId = data[0].trim();
                    String productName = data[1].trim();
                    int quantity;

                    try {
                        quantity = Integer.parseInt(data[2].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if (quantity < 3) {
                        String msg = productName + " (ID: " + productId + ") - Qty: " + quantity;
                        writer.println("⚠ Low stock: " + msg);
                        lowStockSummary.append(msg).append("\n");
                        anyLowStock = true;
                    }
                }
            }

            if (anyLowStock) {
                fullNotification.append(lowStockSummary).append("\n");
            }

            // 🔹 Show only new offers
            if (offerFile.exists()) {
                List<String> allOffers = Files.readAllLines(offerFile.toPath());
                int lastSeenIndex = 0;

                if (offerIndexFile.exists()) {
                    try (Scanner idxReader = new Scanner(offerIndexFile)) {
                        if (idxReader.hasNextInt()) {
                            lastSeenIndex = idxReader.nextInt();
                        }
                    }
                }

                if (lastSeenIndex < allOffers.size()) {
                    fullNotification.append("🎯 New Marketing Offers:\n");
                    for (int i = lastSeenIndex; i < allOffers.size(); i++) {
                        fullNotification.append("- ").append(allOffers.get(i)).append("\n");
                    }

                    // Save the new "last seen" index
                    try (PrintWriter indexWriter = new PrintWriter(offerIndexFile)) {
                        indexWriter.println(allOffers.size());
                    }

                    anyNewOffer = true;
                }
            }

            // 🔹 Final popup
            if (anyLowStock || anyNewOffer) {
                JOptionPane.showMessageDialog(null, fullNotification.toString(), "📢 Notifications", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No notifications.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error in notifications: " + e.getMessage());
        }
    }



    private void damageItems() {
        File damagedFile = new File(Damaged_FILE);
        if (!damagedFile.exists()) {
            JOptionPane.showMessageDialog(null, "No damaged items recorded.");
            return;
        }

        StringBuilder damagedItemsText = new StringBuilder("--- Damaged Items ---\n");

        try (Scanner reader = new Scanner(damagedFile)) {
            boolean hasData = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();
                if (!line.isEmpty()) {
                    damagedItemsText.append(line).append("\n");
                    hasData = true;
                }
            }

            if (hasData) {
                JOptionPane.showMessageDialog(null, damagedItemsText.toString(), "📦 Damaged Items", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No damaged items found.");
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error reading damaged file: " + e.getMessage());
        }
    }

}
