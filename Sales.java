import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

class Sales {
    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private final String PRODUCT_FILE = "Data/products.txt";

    public boolean login(String username, String password) {
        try (Scanner reader = new Scanner(new File(EMPLOYEE_FILE))) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 &&
                        data[1].equals(username) &&
                        data[2].equals(password) &&
                        data[3].equals("sales")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void showSalesMenu(JFrame parentFrame) {
        JFrame salesFrame = new JFrame("Sales Menu");
        salesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        salesFrame.setSize(400, 300);
        salesFrame.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton listProductsBtn = new JButton("List All Products");
        JButton searchProductBtn = new JButton("Search Product");
        JButton placeOrderBtn = new JButton("Place Order");
        JButton confirmOrderBtn = new JButton("Confirm the Order");
        JButton cancelOrderBtn = new JButton("Cancel Order");
        JButton logoutBtn = new JButton("Logout");

        listProductsBtn.addActionListener(e -> listProducts(salesFrame));
        searchProductBtn.addActionListener(e -> searchProduct(salesFrame));
        placeOrderBtn.addActionListener(e -> placeOrder(salesFrame));
        confirmOrderBtn.addActionListener(e -> confirmOrder(salesFrame));
        cancelOrderBtn.addActionListener(e -> cancelOrder(salesFrame));
        logoutBtn.addActionListener(e -> salesFrame.dispose());

        panel.add(listProductsBtn);
        panel.add(searchProductBtn);
        panel.add(placeOrderBtn);
        panel.add(confirmOrderBtn);
        panel.add(cancelOrderBtn);
        panel.add(logoutBtn);

        salesFrame.add(panel);
        salesFrame.setVisible(true);
    }

    private void listProducts(JFrame frame) {
        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "No products available.");
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            StringBuilder productList = new StringBuilder("=== Product List ===\n");
            while (reader.hasNextLine()) {
                productList.append(reader.nextLine()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, productList.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading products: " + e.getMessage());
        }
    }

    private void searchProduct(JFrame frame) {
        String keyword = JOptionPane.showInputDialog(frame, "Enter keyword to search:");
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a search keyword.");
            return;
        }

        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "No products available.");
            return;
        }

        try (Scanner reader = new Scanner(file)) {
            StringBuilder results = new StringBuilder("=== Search Results ===\n");
            boolean found = false;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword.toLowerCase())) {
                    results.append("MATCH: ").append(line).append("\n");
                    found = true;
                }
            }
            JOptionPane.showMessageDialog(frame, found ? results.toString() : "No matching products found.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error searching product: " + e.getMessage());
        }
    }

    public void placeOrder(JFrame frame) {
        try {
            String orderId = JOptionPane.showInputDialog(frame, "Enter Order ID:");
            String productId = JOptionPane.showInputDialog(frame, "Enter Product ID:");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Quantity:"));
            String customer = JOptionPane.showInputDialog(frame, "Enter Customer Name:");

            try (PrintWriter writer = new PrintWriter(new FileWriter("Data/Order.txt"))) {
                writer.println(orderId + "," + productId + "," + quantity + "," + customer);
            }

            JOptionPane.showMessageDialog(frame, "Order saved temporarily.\nPlease confirm to finalize.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error creating order: " + e.getMessage());
        }
    }

    public void confirmOrder(JFrame frame) {
        File tempOrder = new File("Data/Order.txt");
        if (!tempOrder.exists()) {
            JOptionPane.showMessageDialog(frame, "No pending order to confirm.");
            return;
        }

        String orderId = null, productId = null, customer = null;
        int quantity = 0;

        try (Scanner reader = new Scanner(tempOrder)) {
            if (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4) {
                    orderId = data[0].trim();
                    productId = data[1].trim();
                    quantity = Integer.parseInt(data[2].trim());
                    customer = data[3].trim();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to read Order.txt: " + e.getMessage());
            return;
        }

        File productFile = new File(PRODUCT_FILE);
        File tempProductFile = new File("Data/temp_products.txt");
        boolean orderConfirmed = false;

        try (
                BufferedReader productReader = new BufferedReader(new FileReader(productFile));
                PrintWriter productWriter = new PrintWriter(new FileWriter(tempProductFile));
                PrintWriter orderWriter = new PrintWriter(new FileWriter("Data/orders.txt", true))
        ) {
            String line;
            while ((line = productReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(productId)) {
                    int availableQty = Integer.parseInt(data[2].trim());
                    if (availableQty >= quantity) {
                        productWriter.println(data[0] + "," + data[1] + "," + (availableQty - quantity) + "," + data[3]);
                        orderWriter.println(orderId + "," + productId + "," + quantity + "," + customer);
                        orderConfirmed = true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "Insufficient stock.");
                        productWriter.println(line);
                    }
                } else {
                    productWriter.println(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error updating inventory: " + e.getMessage());
            return;
        }

        boolean updated = productFile.delete() && tempProductFile.renameTo(productFile);
        if (updated && orderConfirmed) {
            tempOrder.delete();
            JOptionPane.showMessageDialog(frame, "Order confirmed and inventory updated.");
        } else if (!orderConfirmed) {
            JOptionPane.showMessageDialog(frame, "Order not confirmed due to low stock.");
        } else {
            JOptionPane.showMessageDialog(frame, "Error finalizing the order.");
        }
    }

    public void cancelOrder(JFrame frame) {
        String cancelId = JOptionPane.showInputDialog(frame, "Enter Order ID to cancel:");
        if (cancelId == null || cancelId.trim().isEmpty()) return;

        File pendingOrder = new File("Data/Order.txt");
        if (pendingOrder.exists()) {
            try (Scanner scanner = new Scanner(pendingOrder)) {
                if (scanner.hasNextLine()) {
                    String[] data = scanner.nextLine().split(",");
                    if (data.length >= 1 && data[0].trim().equals(cancelId.trim())) {
                        System.gc();
                        try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                        if (pendingOrder.delete()) {
                            JOptionPane.showMessageDialog(frame, "Pending (unconfirmed) order canceled successfully.");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error checking pending order: " + e.getMessage());
                return;
            }
        }

        File orderFile = new File("Data/orders.txt");
        File tempOrderFile = new File("Data/temp_orders.txt");
        File productFile = new File(PRODUCT_FILE);
        File tempProductFile = new File("Data/temp_products.txt");

        boolean orderFound = false;
        String canceledProductId = null;
        int canceledQuantity = 0;

        try (
                BufferedReader orderReader = new BufferedReader(new FileReader(orderFile));
                PrintWriter orderWriter = new PrintWriter(new FileWriter(tempOrderFile))
        ) {
            String line;
            while ((line = orderReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(cancelId)) {
                    canceledProductId = data[1];
                    canceledQuantity = Integer.parseInt(data[2]);
                    orderFound = true;
                } else {
                    orderWriter.println(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading orders file: " + e.getMessage());
            return;
        }

        if (!orderFound) {
            tempOrderFile.delete();
            JOptionPane.showMessageDialog(frame, "Order ID not found.");
            return;
        }

        try (
                BufferedReader productReader = new BufferedReader(new FileReader(productFile));
                PrintWriter productWriter = new PrintWriter(new FileWriter(tempProductFile))
        ) {
            String line;
            while ((line = productReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(canceledProductId)) {
                    int updatedQty = Integer.parseInt(data[2]) + canceledQuantity;
                    productWriter.println(data[0] + "," + data[1] + "," + updatedQty + "," + data[3]);
                } else {
                    productWriter.println(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error restoring inventory: " + e.getMessage());
            return;
        }

        boolean orderDeleted = orderFile.delete() && tempOrderFile.renameTo(orderFile);
        boolean productUpdated = productFile.delete() && tempProductFile.renameTo(productFile);

        if (orderDeleted && productUpdated) {
            JOptionPane.showMessageDialog(frame, "Order canceled and inventory restored.");
        } else {
            JOptionPane.showMessageDialog(frame, "Error finalizing cancellation.");
        }
    }
}

