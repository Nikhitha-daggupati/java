package library_db;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;

public class ReadingRoomUI extends JFrame {

    public ReadingRoomUI() {

        setTitle("Book Reading Room");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel dateLabel = new JLabel("Select Date:");
        JComboBox<String> dateCombo = new JComboBox<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            dateCombo.addItem(today.plusDays(i).toString());
        }

        JLabel timeLabel = new JLabel("Select Time:");
        String[] times = {
                "09:00 AM - 10:00 AM",
                "10:00 AM - 11:00 AM",
                "11:00 AM - 12:00 PM",
                "12:00 PM - 01:00 PM",
                "01:00 PM - 02:00 PM",
                "02:00 PM - 03:00 PM",
                "03:00 PM - 04:00 PM",
                "04:00 PM - 05:00 PM",
                "05:00 PM - 06:00 PM"
        };
        JComboBox<String> timeCombo = new JComboBox<>(times);

        JButton bookBtn = new JButton("Book Slot");

        add(dateLabel);
        add(dateCombo);
        add(timeLabel);
        add(timeCombo);
        add(new JLabel());
        add(bookBtn);

        bookBtn.addActionListener(e -> {
            String selectedDate = (String) dateCombo.getSelectedItem();
            String selectedTime = (String) timeCombo.getSelectedItem();

            try {
                Connection con = DBConnection.getConnection();
                String sql = "INSERT INTO reading_room_slots (student_id, slot_date, slot_time) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Session.loggedInUserId);
                ps.setDate(2, Date.valueOf(selectedDate));
                ps.setString(3, selectedTime);

                try {
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Slot Booked Successfully!\n" + selectedDate + " at " + selectedTime);
                    dispose();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(this, "Sorry, this slot is already booked. Please select another time or date.");
                }
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
