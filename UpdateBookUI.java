package library_db;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateBookUI extends JFrame {

    public UpdateBookUI() {

        setTitle("Update Book Quantity");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField idField = new JTextField();
        JTextField quantityField = new JTextField();

        JButton updateBtn = new JButton("Update");

        add(new JLabel("Book ID:"));
        add(idField);
        add(new JLabel("New Quantity:"));
        add(quantityField);
        add(new JLabel());
        add(updateBtn);

        updateBtn.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "UPDATE books SET quantity=? WHERE id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(quantityField.getText()));
                ps.setInt(2, Integer.parseInt(idField.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex);
            }
        });

        setVisible(true);
    }
}
