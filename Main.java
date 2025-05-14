import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main(String role, String username) {
        setTitle("Hyper Market - Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        JLabel welcomeLabel = new JLabel("Welcome, " + username + " (" + role + ")", SwingConstants.CENTER);
        add(welcomeLabel);

        if (role.equalsIgnoreCase("admin")) {
            add(createButton("Admin Module", () -> new AdminPanel()));
        }
        if (role.equalsIgnoreCase("marketing")) {
            add(createButton("Marketing Module", () -> JOptionPane.showMessageDialog(this, "Marketing Module Placeholder")));
        }
        if (role.equalsIgnoreCase("inventory")) {
            add(createButton("Inventory Module", () -> JOptionPane.showMessageDialog(this, "Inventory Module Placeholder")));
        }
        if (role.equalsIgnoreCase("sales")) {
            add(createButton("Sales Module", () -> JOptionPane.showMessageDialog(this, "Sales Module Placeholder")));
        }

        // All users can access this
        add(createButton("User Module", () -> JOptionPane.showMessageDialog(this, "User Module Placeholder")));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        add(logoutBtn);

        setVisible(true);
    }

    private JButton createButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        return button;
    }
}
