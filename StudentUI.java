package library_db;

import javax.swing.*;
import java.awt.event.*;

public class StudentUI extends JFrame {

    public StudentUI() {

        setTitle("Student Dashboard");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Student Menu");
        title.setBounds(150, 20, 150, 25);
        add(title);

        JButton viewBtn = new JButton("View Books");
        viewBtn.setBounds(100, 60, 200, 30);
        add(viewBtn);

        JButton issueBtn = new JButton("Issue Book");
        issueBtn.setBounds(100, 110, 200, 30);
        add(issueBtn);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.setBounds(100, 160, 200, 30);
        add(returnBtn);

        JButton bookRoomBtn = new JButton("Book Reading Room");
        bookRoomBtn.setBounds(100, 210, 200, 30);
        add(bookRoomBtn);

        JButton changePassBtn = new JButton("Change Password");
        changePassBtn.setBounds(100, 260, 200, 30);
        add(changePassBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(100, 310, 200, 30);
        add(logoutBtn);

        // Button Actions
        viewBtn.addActionListener(e -> new ViewBooksUI());
        issueBtn.addActionListener(e -> new IssueUI());
        returnBtn.addActionListener(e -> new ReturnUI());
        bookRoomBtn.addActionListener(e -> new ReadingRoomUI());
        changePassBtn.addActionListener(e -> new ChangePasswordUI());

        logoutBtn.addActionListener(e -> {
            Session.clear();
            new LoginUI();
            dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
