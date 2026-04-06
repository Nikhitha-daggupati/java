package library_db;

import javax.swing.*;

public class ReturnUI extends JFrame {

    JTextField studentField, bookField;

    public ReturnUI() {

        setTitle("Return Book");
        setSize(300, 220);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel sLabel = new JLabel("User ID:");
        sLabel.setBounds(30, 40, 100, 25);
        add(sLabel);

        studentField = new JTextField(String.valueOf(Session.loggedInUserId));
        studentField.setBounds(120, 40, 120, 25);
        studentField.setEditable(false);
        add(studentField);

        JLabel bLabel = new JLabel("Book ID:");
        bLabel.setBounds(30, 80, 100, 25);
        add(bLabel);

        bookField = new JTextField();
        bookField.setBounds(120, 80, 120, 25);
        add(bookField);

        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(80, 130, 120, 30);
        add(returnBtn);

        returnBtn.addActionListener(e -> {
            int studentId = Integer.parseInt(studentField.getText());
            int bookId = Integer.parseInt(bookField.getText());

            BookService.returnBook(studentId, bookId);
        });

        setVisible(true);
    }
}
