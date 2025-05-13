import java.util.*;
import java.io.*;
class Marketing {
    private final String EMPLOYEE_FILE = "employees.txt";
    private final String OFFERS_FILE = "offers.txt";
    private Scanner scanner = new Scanner(System.in);

    public boolean login(String username, String password) {
        try {
            File file = new File(EMPLOYEE_FILE);
            if (!file.exists()) return false;

            Scanner reader = new Scanner(file);
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
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: makeReport(); break;
                case 2: sendOffer(); break;
                case 3: System.out.println("Logging out..."); break;
                default: System.out.println("Invalid option");
            }
        } while (choice != 3);
    }

    private void makeReport() {
        System.out.print("Enter keyword to search product: ");
        String keyword = scanner.nextLine().toLowerCase();
        try {
            File file = new File("products.txt");
            if (!file.exists()) {
                System.out.println("No products found.");
                return;
            }
            Scanner reader = new Scanner(file);
            boolean found = false;
            System.out.println("=== Search Results ===");
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println("MATCH: " + line);
                    found = true;
                }
            }
            if (!found) System.out.println("No matching product found.");
        } catch (Exception e) {
            System.out.println("Error reading product file: " + e.getMessage());
        }
    }

    private void sendOffer() {
        try {
            System.out.print("Enter offer description: ");
            String offer = scanner.nextLine();

            FileWriter fw = new FileWriter(OFFERS_FILE, true);
            fw.write(offer + "\n");
            fw.close();

            System.out.println("Offer sent to inventory.");
        } catch (IOException e) {
            System.out.println("Error writing offer: " + e.getMessage());
        }
    }
}

