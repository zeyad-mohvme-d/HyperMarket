import java.awt.*;
import javax.swing.*;
import java.io.File;

public class InventoryManagementSystemUI {
    private static final String EMPLOYEE_FILE = "employees.txt";  // Path to employee file

    public InventoryManagementSystemUI() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagementSystemUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Inventory Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        JButton adminBtn = new JButton("Admin Login");
        JButton inventoryBtn = new JButton("Inventory Staff Login");
        JButton marketingBtn = new JButton("Marketing Staff Login");
        JButton salesBtn = new JButton("Sales Staff Login");
        JButton userBtn = new JButton("User Login");
        JButton exitBtn = new JButton("Exit");

        // Admin login action
        adminBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Admin Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Admin Password:");
            Admin admin = new Admin();
            if (admin.login(username, password)) {
                admin.adminMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        // Inventory staff login action
        inventoryBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Inventory Staff Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Inventory Staff Password:");
            Inventory inventory = new Inventory();
            if (inventory.login(username, password)) {
                inventory.inventoryMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        // Marketing staff login action
        marketingBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Marketing Staff Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Marketing Staff Password:");
            Marketing marketing = new Marketing();
            if (marketing.login(username, password)) {
                marketing.marketingMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });
        
        salesBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Sales Staff Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Sales Staff Password:");
            Sales sales = new Sales();
            if (sales.login(username, password)) {
                sales.showSalesMenu(frame);  // Show Sales menu UI
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        // User login action
        userBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "User Login Page"));

        // Exit action
        exitBtn.addActionListener(e -> System.exit(0));

        // Adding buttons to the panel
        panel.add(adminBtn);
        panel.add(inventoryBtn);
        panel.add(marketingBtn);
        panel.add(salesBtn);
        panel.add(userBtn);
        panel.add(exitBtn);

        frame.add(panel);
        frame.setVisible(true);
    }
}
