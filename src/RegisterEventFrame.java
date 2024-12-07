import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterEventFrame extends JFrame {
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;

    // Database credentials
    private final String jdbcURL = "jdbc:mysql://localhost:3306/ems_db";
    private final String dbUser = "root";
    private final String dbPassword = "Prateek@497";

    public RegisterEventFrame() {
        setTitle("Register for Event");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel titleLabel = new JLabel("Event Registration", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 70, 123));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        formPanel.add(new JLabel("Name:", JLabel.RIGHT));
        nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:", JLabel.RIGHT));
        emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:", JLabel.RIGHT));
        phoneField = new JTextField();
        phoneField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        formPanel.add(phoneField);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerButton.setBackground(new Color(46, 204, 113)); // A vibrant green
        registerButton.setForeground(Color.BLACK); // Change text color to black
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> registerParticipant());

        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerParticipant() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save the registration details to the database
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "INSERT INTO participants (name, email, phone) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to register. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}