import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private ArrayList<Event> events = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable eventTable;

    // JDBC connection variables
    private String jdbcURL = "jdbc:mysql://localhost:3306/EMS_DB"; // Change to your DB URL
    private String dbUser = "root"; // Change to your DB username
    private String dbPassword = "1234"; // Change to your DB password

    public MainFrame() {
        setTitle("Event Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Use BorderLayout for the frame
        setLayout(new BorderLayout(10, 10));

        // Create a title label
        JLabel titleLabel = new JLabel("Event Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));  // Padding
        add(titleLabel, BorderLayout.NORTH);

        // Main Panel with Border Layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));  // Padding

        // Button Panel (Add and Remove Buttons)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));  // 1 row, 2 columns, 10px gap
        JButton addButton = new JButton("Add Event");
        JButton removeButton = new JButton("Remove Event");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Event Table with 3 columns: Name, Venue, Date
        String[] columnNames = {"Name", "Venue", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);  // 0 means no initial rows
        eventTable = new JTable(tableModel);
        eventTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Event List"));

        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        // Add Event Button Action Listener
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEvent();
            }
        });

        // Remove Event Button Action Listener
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeEvent();
            }
        });
    }

    private void addEvent() {
        JTextField eventNameField = new JTextField();
        JTextField eventVenueField = new JTextField();
        JTextField eventDateField = new JTextField();

        // Form panel to get event details
        JPanel eventForm = new JPanel(new GridLayout(3, 2, 10, 10));
        eventForm.add(new JLabel("Event Name:"));
        eventForm.add(eventNameField);
        eventForm.add(new JLabel("Venue:"));
        eventForm.add(eventVenueField);
        eventForm.add(new JLabel("Date:"));
        eventForm.add(eventDateField);

        int result = JOptionPane.showConfirmDialog(this, eventForm, "Add Event", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String eventName = eventNameField.getText();
            String eventVenue = eventVenueField.getText();
            String eventDate = eventDateField.getText();

            if (!eventName.trim().isEmpty()) {
                Event newEvent = new Event(eventName);
                newEvent.setVenue(eventVenue);
                newEvent.setDate(eventDate);

                events.add(newEvent);
                
                // Add the event details to the table
                tableModel.addRow(new Object[]{eventName, eventVenue, eventDate});

                // Save the event to the database
                saveEventToDatabase(eventName, eventVenue, eventDate);
            } else {
                JOptionPane.showMessageDialog(this, "Event name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveEventToDatabase(String name, String venue, String date) {
        // Validate database connection
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "INSERT INTO events (name, venue, date) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, venue);
            statement.setString(3, date);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow != -1) {
            // Remove the event from the array and table
            Event eventToRemove = events.get(selectedRow);
            removeEventFromDatabase(eventToRemove.getName()); // Remove from DB
            events.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an event to remove", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeEventFromDatabase(String eventName) {
        try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
            String sql = "DELETE FROM events WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, eventName);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}