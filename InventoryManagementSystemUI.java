import java.awt.*;
import javax.swing.*;

public class InventoryManagementSystemUI {
    private static final String EMPLOYEE_FILE = "employees.txt";

    public InventoryManagementSystemUI() {}

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

        // Sales staff login action
        salesBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Sales Staff Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Sales Staff Password:");
            Sales sales = new Sales();
            if (sales.login(username, password)) {
                sales.showSalesMenu(frame);  // ✅ PERFECT
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        // User login action with GUI
        userBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter User Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter User Password:");
            User user = new User();
            if (user.login(username, password)) {
                showUserMenu(user);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        // Exit action
        exitBtn.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        panel.add(adminBtn);
        panel.add(inventoryBtn);
        panel.add(marketingBtn);
        panel.add(salesBtn);
        panel.add(userBtn);
        panel.add(exitBtn);

        frame.add(panel);
        frame.setVisible(true);
    }

    // User GUI Menu
    private static void showUserMenu(User user) {
        JFrame userFrame = new JFrame("User Panel");
        userFrame.setSize(350, 250);
        userFrame.setLocationRelativeTo(null);
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton viewProfileBtn = new JButton("View Profile");
        JButton updateInfoBtn = new JButton("Update Info");
        JButton logoutBtn = new JButton("Logout");

        viewProfileBtn.addActionListener(e -> {
            String[] profile = user.getProfile();
            if (profile != null) {
                JOptionPane.showMessageDialog(userFrame,
                        "ID: " + profile[0] + "\nUsername: " + profile[1] + "\nPassword: " + profile[2],
                        "User Profile", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(userFrame, "Failed to load profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateInfoBtn.addActionListener(e -> {
            JTextField usernameField = new JTextField();
            JTextField passwordField = new JPasswordField();
            Object[] message = {
                    "New Username:", usernameField,
                    "New Password:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(userFrame, message, "Update Info", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();
                if (user.updateInfoGUI(newUsername, newPassword)) {
                    JOptionPane.showMessageDialog(userFrame, "Info updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(userFrame, "Failed to update info.");
                }
            }
        });

        logoutBtn.addActionListener(e -> userFrame.dispose());

        panel.add(viewProfileBtn);
        panel.add(updateInfoBtn);
        panel.add(logoutBtn);

        userFrame.add(panel);
        userFrame.setVisible(true);
    }
}
