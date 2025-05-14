import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard(String role) {
        setTitle("Dashboard - Role: " + role);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton adminBtn = new JButton("Admin Module");
        JButton marketingBtn = new JButton("Marketing Module");
        JButton inventoryBtn = new JButton("Inventory Module");
        JButton salesBtn = new JButton("Sales Module");
        JButton userBtn = new JButton("User Module");

        if (role.equals("admin")) {
            adminBtn.addActionListener(e -> new AdminPanel());
        } else {
            adminBtn.setEnabled(false);
        }

        panel.add(adminBtn);
        panel.add(marketingBtn);
        panel.add(inventoryBtn);
        panel.add(salesBtn);
        panel.add(userBtn);

        add(panel);
        setVisible(true);
    }
}
