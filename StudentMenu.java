package library_db;

import java.util.Scanner;

public class StudentMenu {

    public static void studentMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1. View Books");
            System.out.println("2. Issue Book");
            System.out.println("3. Return Book");
            System.out.println("4. Logout");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    BookService.viewBooks();
                    break;
                case 2:
                    IssueService.issueBook();
                    break;
                case 3:
                    IssueService.returnBook();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
