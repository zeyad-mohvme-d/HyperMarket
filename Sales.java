import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

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

    // Show Sales Menu with GUI buttons
    public void showSalesMenu(JFrame parentFrame) {
        JFrame salesFrame = new JFrame("Sales Menu");
        salesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        salesFrame.setSize(400, 300);
        salesFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton listProductsBtn = new JButton("List All Products");
        JButton searchProductBtn = new JButton("Search Product");
        JButton placeOrderBtn = new JButton("Place Order");
        JButton cancelOrderBtn = new JButton("Cancel Order");
        JButton logoutBtn = new JButton("Logout");

        // ✅ List products
        listProductsBtn.addActionListener(e -> listProducts(salesFrame));

        // ✅ Search product
        searchProductBtn.addActionListener(e -> searchProduct(salesFrame));

        // ✅ Place order (updates inventory and saves to orders.txt)
        placeOrderBtn.addActionListener(e -> placeOrder(salesFrame));

        // ✅ Cancel order (optional, pass frame if needed)
        cancelOrderBtn.addActionListener(e -> cancelOrder(salesFrame));

        // ✅ Logout
        logoutBtn.addActionListener(e -> salesFrame.dispose());

        // Add all buttons to panel
        panel.add(listProductsBtn);
        panel.add(searchProductBtn);
        panel.add(placeOrderBtn);
        panel.add(cancelOrderBtn);
        panel.add(logoutBtn);

        salesFrame.add(panel);
        salesFrame.setVisible(true);
    }


    // List all products (functionality)
    private void listProducts(JFrame frame) {
        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, "No products available.");
                return;
            }
            Scanner reader = new Scanner(file);
            StringBuilder productList = new StringBuilder("=== Product List ===\n");
            while (reader.hasNextLine()) {
                productList.append(reader.nextLine()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, productList.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error reading products: " + e.getMessage());
        }
    }

    // Search for products (functionality)
    private void searchProduct(JFrame frame) {
        String keyword = JOptionPane.showInputDialog(frame, "Enter keyword to search:");
        if (keyword == null || keyword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a search keyword.");
            return;
        }

        try {
            File file = new File(PRODUCT_FILE);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(frame, "No products available.");
                return;
            }
            Scanner reader = new Scanner(file);
            StringBuilder searchResults = new StringBuilder("=== Search Results ===\n");
            boolean found = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    searchResults.append("MATCH: ").append(line).append("\n");
                    found = true;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(frame, "No matching products found.");
            } else {
                JOptionPane.showMessageDialog(frame, searchResults.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error searching product: " + e.getMessage());
        }
    }

    // Place order (functionality)
    public void placeOrder(JFrame frame) {
        String orderId = JOptionPane.showInputDialog(frame, "Enter Order ID:");
        String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
        int quantity;
        try {
            quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Quantity:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid quantity.");
            return;
        }
        String customer = JOptionPane.showInputDialog(frame, "Enter Customer Name:");

        File productFile = new File("Data/products.txt"); // adjust to "data/products.txt" if needed
        File tempProductFile = new File("Data/temp_products.txt");
        boolean orderPlaced = false;

        try (
                BufferedReader reader = new BufferedReader(new FileReader(productFile));
                PrintWriter tempWriter = new PrintWriter(new FileWriter(tempProductFile));
                PrintWriter orderWriter = new PrintWriter(new FileWriter("orders.txt", true))  // append mode
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 4) {
                    tempWriter.println(line); // skip broken lines safely
                    continue;
                }

                String id = data[0].trim();
                String name = data[1].trim();
                int availableQty = Integer.parseInt(data[2].trim());
                String expiry = data[3].trim();

                if (id.equals(productId)) {
                    if (availableQty >= quantity) {
                        int newQty = availableQty - quantity;
                        tempWriter.println(id + "," + name + "," + newQty + "," + expiry);
                        orderWriter.println(orderId + "," + id + "," + quantity + "," + customer);
                        orderPlaced = true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Only " + availableQty + " in stock. Cannot place order.");
                        tempWriter.println(line); // write original line unchanged
                    }
                } else {
                    tempWriter.println(line); // keep other products unchanged
                }
            }

        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Error placing order: " + e.getMessage());
            return;
        }

        // GC + slight delay to release locks
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        // Debug logs
        boolean deleted = productFile.delete();
        boolean renamed = tempProductFile.renameTo(productFile);
        System.out.println("Deleted products.txt: " + deleted);
        System.out.println("Renamed temp_products.txt: " + renamed);
        System.out.println("Path: " + productFile.getAbsolutePath());

        // Final result
        if (deleted && renamed) {
            if (orderPlaced) {
                JOptionPane.showMessageDialog(frame, "Order placed successfully and inventory updated.");
            } else {
                JOptionPane.showMessageDialog(frame, "Order failed. Product not found or insufficient stock.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Error updating inventory file.");
        }
    }




    // Cancel order (functionality)
    private void cancelOrder(JFrame frame) {
        String orderIdToCancel = JOptionPane.showInputDialog(frame, "Enter Order ID to cancel:");
        if (orderIdToCancel == null || orderIdToCancel.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an Order ID.");
            return;
        }

        File inputFile = new File(ORDER_FILE);
        File tempFile = new File("temp_orders.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(orderIdToCancel)) {
                    writer.println(line);
                } else {
                    found = true;
                }
            }

            reader.close();
            writer.close();

            if (inputFile.delete()) {
                tempFile.renameTo(inputFile);
                if (found) {
                    JOptionPane.showMessageDialog(frame, "Order canceled.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Order ID not found.");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error canceling order: " + e.getMessage());
        }
    }
}
