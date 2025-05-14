import java.util.*;
import java.io.*;

class Marketing {
    private final String EMPLOYEE_FILE = "dataemployees.txt";
    private final String OFFERS_FILE = "offers.txt";
    private final String PRODUCT_FILE = "products.txt";

    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try (Scanner reader = new Scanner(new File(EMPLOYEE_FILE))) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password) && data[3].equals("marketing")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void marketingMenu() {
        int choice;
        do {
            System.out.println("\n--- Marketing Panel ---");
            System.out.println("1. Create Product Report (Search Inventory)");
            System.out.println("2. Send Special Offer to Inventory");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a number between 1 and 3.");
                scanner.next(); // Clear invalid input
                System.out.print("Choose: ");
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> makeReport();
                case 2 -> sendOffer();
                case 3 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (choice != 3);
    }

    private void makeReport() {
        System.out.print("Enter keyword to search product: ");
        String keyword = scanner.nextLine().toLowerCase();

        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            System.out.println("No products found.");
            return;
        }

        boolean found = false;
        System.out.println("\n=== Search Results ===");
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println("MATCH: " + line);
                    found = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading product file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("No matching product found.");
        }
    }

    private void sendOffer() {
        System.out.print("Enter offer description: ");
        String offer = scanner.nextLine().trim();

        if (offer.isEmpty()) {
            System.out.println("Offer cannot be empty.");
            return;
        }

        try (FileWriter fw = new FileWriter(OFFERS_FILE, true)) {
            fw.write(offer + "\n");
            System.out.println("Offer sent to inventory.");
        } catch (IOException e) {
            System.out.println("Error writing offer: " + e.getMessage());
        }
    }
}
