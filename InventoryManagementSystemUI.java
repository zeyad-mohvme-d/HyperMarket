import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InventoryManagementSystemUI {

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

        JLabel titleLabel = new JLabel("Inventory Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);

        JButton adminBtn = new JButton("Admin Login");
        JButton inventoryBtn = new JButton("Inventory Staff Login");
        JButton marketingBtn = new JButton("Marketing Staff Login");
        JButton salesBtn = new JButton("Sales Staff Login");
        JButton userBtn = new JButton("User Login");
        JButton exitBtn = new JButton("Exit");

        // Admin login handling
        adminBtn.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(frame, "Enter Admin Username:");
            String password = JOptionPane.showInputDialog(frame, "Enter Admin Password:");
            Admin admin = new Admin();
            if (admin.login(username, password)) {
                admin.adminMenu(); // Open admin menu on successful login
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        inventoryBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Inventory Staff Login Page"));
        marketingBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Marketing Staff Login Page"));
        salesBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Sales Staff Login Page"));
        userBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "User Login Page"));
        exitBtn.addActionListener(e -> System.exit(0));

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
