import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ParticipantFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable eventTable;

    // Database credentials
    private final String jdbcURL = "jdbc:mysql://localhost:3306/Your database name";
    private final String dbUser = "root";
    private final String dbPassword = "Your password";

    public ParticipantFrame() {
        setTitle("Participant - View Events");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Event Viewer", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Venue", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel);
        eventTable.setFont(new Font("SansSerif", Font.PLAIN, 18));
        eventTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(eventTable);

        loadEvents();

        // Create a label for registration
        JLabel registerLabel = new JLabel("Register for Event");
        registerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        registerLabel.setForeground(Color.BLACK); // Set text color to black
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registerForEvent();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerLabel); // Add register label to button panel

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEvents() {
        // Fetch and load events from the database
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "SELECT name, venue, date FROM events";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String venue = resultSet.getString("venue");
                String date = resultSet.getString("date");
                tableModel.addRow(new Object[]{name, venue, date});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load events from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerForEvent() {
        new RegisterEventFrame().setVisible(true);
    }
}