import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ArrayList<User> users;
    public LoginFrame(ArrayList<User> users) {
        this.users = users; // Initialize shared user list
        // (Rest of your existing code...)
    }
    public LoginFrame() {
        setTitle("Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window
        setLayout(new BorderLayout(10, 10));

        // Create a panel for the form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Add form panel to the center of the frame
        add(formPanel, BorderLayout.CENTER);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticate();
            }
        });
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(users).setVisible(true); // Pass the users list to the register frame
            }
        });
        // Button panel for alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(registerButton);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Example username and password (you can add proper validation here)
        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();  // Close the login window

            // Open the main event management window
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
