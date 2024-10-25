import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ArrayList<User> users; // List to store registered users

    public RegisterFrame(ArrayList<User> users) {
        this.users = users; // Receive user list from the main application
        setTitle("Register");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
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

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        // Button panel for alignment
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate input
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Add the new user to the list
        users.add(new User(username, password));
        JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        this.dispose(); // Close the registration window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegisterFrame(new ArrayList<User>()).setVisible(true);  // For testing only
            }
        });
    }
}
