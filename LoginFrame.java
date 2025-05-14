
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        panel.add(new JLabel()); // filler
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String role = authenticate(user, pass);

        if (role != null) {
            dispose(); // close login frame
            new Dashboard(role); // go to dashboard
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String authenticate(String username, String password) {
        try (Scanner reader = new Scanner(new File("employees.txt"))) {
            while (reader.hasNextLine()) {
                String[] data = reader.nextLine().split(",");
                if (data.length >= 4 && data[1].equals(username) && data[2].equals(password)) {
                    return data[3]; // return role
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
