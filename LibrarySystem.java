import javax.swing.*;
import java.io.*;
import java.util.*;
public class LibrarySystem {
    private static String bookFile = "bookData.txt";
    private static String studentFile = "studentData.txt";
    private static String historyFile = "historyData.txt";
    private static String entryHistoryFile = "entryHistory.txt";
    private static String entryStudentFile = "entryStudent.txt";
    public static void main(String[] args) {
        LoginSystem loginSystem = new LoginSystem();
        if (!loginSystem.login()) {
            System.exit(0);
        }

        while (true) {
            String[] mainOptions = {"Entry System", "Book System", "Exit"};
            int mainChoice = JOptionPane.showOptionDialog(null, "Select a system:", "Library Management System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, mainOptions, mainOptions[0]);

            switch (mainChoice) {
                case 0:
                    entrySystem();
                    break;
                case 1:
                    bookSystem();
                    break;
                case 2:
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.");
                    break;
            }
        }
    }

    private static void entrySystem() {
        String[] entryOptions = {"Add Student", "Record Entry", "Back"};
        int entryChoice = JOptionPane.showOptionDialog(null, "Select an option:", "Library Management System - Entry System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, entryOptions, entryOptions[0]);

        switch (entryChoice) {
            case 0:
                addStudent();
                break;
            case 1:
                recordEntry();
                break;
            case 2:
                return;
            default:
                JOptionPane.showMessageDialog(null, "Invalid choice.");
                break;
        }
    }

    private static void addStudent() {
        String studentID = JOptionPane.showInputDialog(null, "Enter student ID:");
        String studentName = JOptionPane.showInputDialog(null, "Enter student name:");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(entryStudentFile, true));
            pw.println(studentID + "," + studentName);
            pw.close();
            JOptionPane.showMessageDialog(null, "Student added successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error adding student: " + e.getMessage());
        }
    }

    private static void recordEntry() {
        String studentID = JOptionPane.showInputDialog(null, "Enter student ID:");

        try {
            Scanner scanner = new Scanner(new File(entryStudentFile));
            boolean found = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(studentID)) {
                    PrintWriter pw = new PrintWriter(new FileWriter(entryHistoryFile, true));
                    pw.println("Student entered: " + parts[1] + " (" + studentID + ") at " + new Date());
                    pw.close();
                    JOptionPane.showMessageDialog(null, "Entry recorded successfully.");
                    found = true;
                    break;
                }
            }
            scanner.close();
            if (!found) {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error recording entry: " + e.getMessage());
        }
    }

    private static void bookSystem() {
        while (true) {
            String[] bookOptions = {"Add Book", "Search Book", "Delete Book", "Borrow Book", "Return Book", "View History", "Back"};
            int bookChoice = JOptionPane.showOptionDialog(null, "Select an option:", "Library Management System - Book System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, bookOptions, bookOptions[0]);

            switch (bookChoice) {
                case 0:
                    addBook();
                    break;
                case 1:
                    searchBook();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    viewHistory();
                    break;
                case 6:
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice.");
                    break;
            }
        }
    }

    private static void addBook() {
        String title = JOptionPane.showInputDialog(null, "Enter book title:");
        String author = JOptionPane.showInputDialog(null, "Enter author:");
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:");
        String publisher = JOptionPane.showInputDialog(null, "Enter publisher:");
        String year = JOptionPane.showInputDialog(null, "Enter year of publication:");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(bookFile, true));
            pw.println(title + "," + author + "," + isbn + "," + publisher + "," + year);
            pw.close();
            // Write to history file
            PrintWriter pwHistory = new PrintWriter(new FileWriter(historyFile, true));
            pwHistory.println("Book added: " + title + "," + author + "," + isbn + "," + publisher + "," + year);
            pwHistory.close();
            JOptionPane.showMessageDialog(null, "Book added successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error adding book: " + e.getMessage());
        }
    }

    private static void searchBook() {
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:");

        try {
            Scanner scanner = new Scanner(new File(bookFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[2].equals(isbn)) {
                    JOptionPane.showMessageDialog(null, "Title: " + parts[0] + "\nAuthor: " + parts[1] + "\nPublisher: " + parts[3] + "\nYear: " + parts[4]);
                    scanner.close();
                    return;
                }
            }
            scanner.close();
            JOptionPane.showMessageDialog(null, "Book not found.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error searching book: " + e.getMessage());
        }
    }


    private static void deleteBook() {
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:");

        try {
            File inputFile = new File(bookFile);
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            String lineToRemove = null;
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts[2].equals(isbn)) {
                    lineToRemove = currentLine;
                    break;
                }
            }

            if (lineToRemove != null) {
                String trimmedLine = lineToRemove.trim();
                String replacement = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals(trimmedLine)) {
                        replacement += line + "\n";
                    }
                }
                writer.print(replacement);
                reader.close();
                writer.close();
                inputFile.delete();
                tempFile.renameTo(inputFile);
                // Write to history file
                PrintWriter pwHistory = new PrintWriter(new FileWriter(historyFile, true));
                pwHistory.println("Book deleted: " + trimmedLine);
                pwHistory.close();
                JOptionPane.showMessageDialog(null, "Book deleted successfully.");
            } else {
                reader.close();
                writer.close();
                tempFile.delete();
                JOptionPane.showMessageDialog(null, "Book not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error deleting book: " + e.getMessage());
        }
    }

    private static void borrowBook() {
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:");

        try {
            Scanner scanner = new Scanner(new File(bookFile));
            boolean bookFound = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[2].equals(isbn)) {
                    bookFound = true;
                    break;
                }
            }
            scanner.close();

            if (bookFound) {
                String name = JOptionPane.showInputDialog(null, "Enter student name:");
                String id = JOptionPane.showInputDialog(null, "Enter student ID:");
                String course = JOptionPane.showInputDialog(null, "Enter student course:");
                String college = JOptionPane.showInputDialog(null, "Enter student college:");

                PrintWriter pw = new PrintWriter(new FileWriter(studentFile, true));
                pw.println(name + "," + id + "," + course + "," + college + "," + isbn);
                pw.close();
                JOptionPane.showMessageDialog(null, "Book borrowed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Book not available.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error borrowing book: " + e.getMessage());
        }
    }

    private static void returnBook() {
        String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:");

        try {
            File inputFile = new File(studentFile);
            File tempFile = new File("temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
            String lineToRemove = null;
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(",");
                if (parts[4].equals(isbn)) {
                    lineToRemove = currentLine;
                    break;
                }
            }

            if (lineToRemove != null) {
                String trimmedLine = lineToRemove.trim();
                String replacement = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().equals(trimmedLine)) {
                        replacement += line + "\n";
                    }
                }
                writer.print(replacement);
                reader.close();
                writer.close();
                inputFile.delete();
                tempFile.renameTo(inputFile);
                JOptionPane.showMessageDialog(null, "Book returned successfully.");
            } else {
                reader.close();
                writer.close();
                tempFile.delete();
                JOptionPane.showMessageDialog(null, "Book not borrowed.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error returning book: " + e.getMessage());
        }
    }

    private static void viewHistory() {
        try {
            Scanner scanner = new Scanner(new File("historyData.txt"));
            String message = "";
            while (scanner.hasNextLine()) {
                message += scanner.nextLine() + "\n";
            }
            scanner.close();
            JOptionPane.showMessageDialog(null, message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error viewing history: " + e.getMessage());
        }
    }
}


