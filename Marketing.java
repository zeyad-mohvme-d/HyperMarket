import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;

class Marketing {

    private final String EMPLOYEE_FILE = "Data/employees.txt";
    private final String OFFERS_FILE = "Data/offers.txt";
    private final String PRODUCT_FILE = "Data/products.txt";
    private Scanner scanner;

    Marketing() {
        this.scanner = new Scanner(System.in);
    }

    // Login method
    public boolean login(String username, String password) {
        try (Scanner reader = new Scanner(new File(EMPLOYEE_FILE))) {
            String[] data;
            while (reader.hasNextLine()) {
                data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password) && data[3].equals("marketing")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    // Marketing Menu method
    public void marketingMenu() {
        int choice;
        do {
            String menu = "\n--- Marketing Panel ---\n" +
                    "1. Create Product Report (Search Inventory)\n" +
                    "2. Send Special Offer to Inventory\n" +
                    "3. Logout\n" +
                    "Choose: ";

            // Get input from the user via JOptionPane
            try {
                choice = Integer.parseInt(JOptionPane.showInputDialog(menu));
                switch (choice) {
                    case 1:
                        makeReport();
                        break;
                    case 2:
                        sendOffer();
                        break;
                    case 3:
                        JOptionPane.showMessageDialog(null, "Logging out...");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                choice = -1; // Ensure the loop continues if input is invalid.
            }
        } while (choice != 3);
    }

    // Make Product Report method
    private void makeReport() {
        String keyword = JOptionPane.showInputDialog("Enter keyword to search product:").toLowerCase();
        File file = new File(PRODUCT_FILE);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No products found.");
            return;
        }

        boolean found = false;
        StringBuilder results = new StringBuilder("\n=== Search Results ===");

        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword)) {
                    results.append("\nMATCH: ").append(line);
                    found = true;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading product file: " + e.getMessage());
            return;
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No matching product found.");
        } else {
            JOptionPane.showMessageDialog(null, results.toString());
        }
    }

    // Send Special Offer method
    private void sendOffer() {
        String offer = JOptionPane.showInputDialog("Enter offer description:").trim();
        if (offer.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Offer cannot be empty.");
        } else {
            try (FileWriter fw = new FileWriter(OFFERS_FILE, true)) {
                fw.write(offer + "\n");
                JOptionPane.showMessageDialog(null, "Offer sent to inventory.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing offer: " + e.getMessage());
            }
        }
    }
}
