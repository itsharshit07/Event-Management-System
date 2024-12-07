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

    private String jdbcURL = "jdbc:mysql://localhost:3306/Your database name";
    private String dbUser = "root";
    private String dbPassword = "Your Password";

    public MainFrame() {
        setTitle("Event Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 223, 186));

        JLabel titleLabel = new JLabel("Event Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBackground(new Color(255, 153, 204));
        JButton addButton = new JButton("Add Event");
        JButton removeButton = new JButton("Remove Event");

        addButton.setBackground(new Color(102, 204, 255));
        addButton.setFont(new Font("Arial", Font.BOLD, 18));
        removeButton.setBackground(new Color(255, 102, 102));
        removeButton.setFont(new Font("Arial", Font.BOLD, 18));

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Name", "Venue", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel);
        eventTable.setFont(new Font("SansSerif", Font.PLAIN, 18));
        eventTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Event List"));

        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addEvent();
            }
        });

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

        JPanel eventForm = new JPanel(new GridLayout(3, 2, 10, 10));
        eventForm.setBackground(new Color(255, 255, 204));
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
                tableModel.addRow(new Object[]{eventName, eventVenue, eventDate});
                saveEventToDatabase(eventName, eventVenue, eventDate);
            } else {
                JOptionPane.showMessageDialog(this, "Event name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveEventToDatabase(String name, String venue, String date) {
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
            Event eventToRemove = events.get(selectedRow);
            removeEventFromDatabase(eventToRemove.getName());
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