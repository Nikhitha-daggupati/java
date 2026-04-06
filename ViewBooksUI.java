package library_db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBooksUI extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewBooksUI() {

        setTitle("Books Available");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Quantity"});

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Only Admin can see the CRUD buttons
        if ("admin".equalsIgnoreCase(Session.loggedInRole)) {
            JPanel buttonPanel = new JPanel();
            JButton addBtn = new JButton("Add Book");
            JButton updateBtn = new JButton("Update Book");
            JButton deleteBtn = new JButton("Delete Book");
            
            buttonPanel.add(addBtn);
            buttonPanel.add(updateBtn);
            buttonPanel.add(deleteBtn);
            add(buttonPanel, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                new AddBookUI();
                loadBooks();
            });

            updateBtn.addActionListener(e -> handleAction("Update Quantity"));
            deleteBtn.addActionListener(e -> handleAction("Delete"));

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        handleAction("Options");
                    }
                }
            });
        }

        loadBooks();

        setVisible(true);
    }

    private void loadBooks() {
        model.setRowCount(0);
        try {
            ResultSet rs = BookService.getAvailableBooks();

            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e);
        }
    }

    private void handleAction(String actionType) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book first!");
            return;
        }

        int bookId = (int) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 1);

        try {
            Connection con = DBConnection.getConnection();

            if (actionType.equals("Options")) {
                String[] options = {"Update Quantity", "Delete", "Cancel"};
                int response = JOptionPane.showOptionDialog(this,
                        "Action for Book: " + title,
                        "Book Options",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                
                if (response == 0) actionType = "Update Quantity";
                else if (response == 1) actionType = "Delete";
                else return;
            }

            if (actionType.equals("Update Quantity")) { 
                String newQtyStr = JOptionPane.showInputDialog(this, "Enter new quantity for " + title + ":", model.getValueAt(row, 3));
                if (newQtyStr != null && !newQtyStr.trim().isEmpty()) {
                    int qty = Integer.parseInt(newQtyStr);
                    PreparedStatement ps = con.prepareStatement("UPDATE books SET quantity=? WHERE id=?");
                    ps.setInt(1, qty);
                    ps.setInt(2, bookId);
                    ps.executeUpdate();
                    loadBooks();
                }
            } else if (actionType.equals("Delete")) { 
                int confirm = JOptionPane.showConfirmDialog(this, "Delete book " + title + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM books WHERE id=?");
                    ps.setInt(1, bookId);
                    ps.executeUpdate();
                    loadBooks();
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
