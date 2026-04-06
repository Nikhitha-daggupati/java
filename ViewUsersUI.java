package library_db;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewUsersUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private String roleType;

    public ViewUsersUI(String roleType) {
        this.roleType = roleType;
        setTitle("View " + roleType.substring(0, 1).toUpperCase() + roleType.substring(1) + "s");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Username", "Phone"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            new AddUserUI(roleType);
            loadData(); // reload might require user to reopen or manual refresh, but we keep it here
        });

        updateBtn.addActionListener(e -> handleAction("Update Phone"));
        deleteBtn.addActionListener(e -> handleAction("Delete"));

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    handleAction("Options");
                }
            }
        });

        loadData();
        setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, username, phone FROM users WHERE role=?");
            ps.setString(1, roleType);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("phone")
                });
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAction(String actionType) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row first!");
            return;
        }

        int userId = (int) model.getValueAt(row, 0);
        String username = (String) model.getValueAt(row, 1);

        try {
            Connection con = DBConnection.getConnection();
            if (actionType.equals("Options")) {
                String[] options = {"Update Phone", "Reset Password", "Delete", "Cancel"};
                int response = JOptionPane.showOptionDialog(this,
                        "Action for user: " + username,
                        "User Options",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);
                
                if (response == 0) actionType = "Update Phone";
                else if (response == 1) actionType = "Reset Password";
                else if (response == 2) actionType = "Delete";
                else return;
            }

            if (actionType.equals("Update Phone")) {
                String newPhone = JOptionPane.showInputDialog(this, "Enter new phone for " + username + ":", model.getValueAt(row, 2));
                if (newPhone != null && !newPhone.trim().isEmpty()) {
                    PreparedStatement ps = con.prepareStatement("UPDATE users SET phone=? WHERE id=?");
                    ps.setString(1, newPhone);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                    loadData();
                }
            } else if (actionType.equals("Reset Password")) {
                String newPass = JOptionPane.showInputDialog(this, "Enter new password for " + username + ":");
                if (newPass != null && !newPass.trim().isEmpty()) {
                    PreparedStatement ps = con.prepareStatement("UPDATE users SET password=? WHERE id=?");
                    ps.setString(1, newPass);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
            } else if (actionType.equals("Delete")) {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete user " + username + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id=?");
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                    loadData();
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
