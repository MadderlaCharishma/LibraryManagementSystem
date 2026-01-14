package librarymanagement;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Library {

    // Add book
    public void addBook(Book book) {
        String sql = "INSERT INTO books (id, title, author, category, isIssued, issueDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, book.getId());
            pst.setString(2, book.getTitle());
            pst.setString(3, book.getAuthor());
            pst.setString(4, book.getCategory());
            pst.setBoolean(5, book.isIssued());
            pst.setDate(6, null);
            pst.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    // Display books
    public void displayBooks() {
        String sql = "SELECT * FROM books";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("ID\tTitle\tAuthor\tCategory\tStatus");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                boolean isIssued = rs.getBoolean("isIssued");
                String status = isIssued ? "Issued" : "Available";
                System.out.println(id + "\t" + title + "\t" + author + "\t" + category + "\t" + status);
            }

        } catch (SQLException e) {
            System.out.println("Error displaying books: " + e.getMessage());
        }
    }

    // Search books
    public void searchBooks(String keyword) {
        String sql = "SELECT * FROM books WHERE id LIKE ? OR title LIKE ? OR author LIKE ? OR category LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            pst.setString(3, "%" + keyword + "%");
            pst.setString(4, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();
            boolean found = false;
            System.out.println("ID\tTitle\tAuthor\tCategory\tStatus");
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String category = rs.getString("category");
                boolean isIssued = rs.getBoolean("isIssued");
                String status = isIssued ? "Issued" : "Available";
                System.out.println(id + "\t" + title + "\t" + author + "\t" + category + "\t" + status);
            }
            if (!found) System.out.println("No books found for keyword: " + keyword);

        } catch (SQLException e) {
            System.out.println("Error searching books: " + e.getMessage());
        }
    }

    // Issue book
    public void issueBook(int id) {
        String sql = "UPDATE books SET isIssued = TRUE, issueDate = CURDATE() WHERE id = ? AND isIssued = FALSE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0)
                System.out.println("Book issued successfully!");
            else
                System.out.println("Book not found or already issued!");

        } catch (SQLException e) {
            System.out.println("Error issuing book: " + e.getMessage());
        }
    }

    // Return book with late fee
    public void returnBook(int id) {
        String sqlSelect = "SELECT issueDate FROM books WHERE id = ? AND isIssued = TRUE";
        String sqlUpdate = "UPDATE books SET isIssued = FALSE, issueDate = NULL WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstSelect = con.prepareStatement(sqlSelect);
             PreparedStatement pstUpdate = con.prepareStatement(sqlUpdate)) {

            pstSelect.setInt(1, id);
            ResultSet rs = pstSelect.executeQuery();

            if (rs.next()) {
                Date issueDateSQL = rs.getDate("issueDate");
                if (issueDateSQL != null) {
                    LocalDate issueDate = issueDateSQL.toLocalDate();
                    long days = ChronoUnit.DAYS.between(issueDate, LocalDate.now());
                    long fine = 0;
                    if (days > 7) fine = (days - 7) * 5; // ₹5/day
                    System.out.println("Book returned successfully! Late fee: ₹" + fine);
                }
            } else {
                System.out.println("Book not found or was not issued!");
            }

            pstUpdate.setInt(1, id);
            pstUpdate.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error returning book: " + e.getMessage());
        }
    }

    // Delete book
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Book deleted successfully!");
            else System.out.println("Book not found!");

        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }

    // Update book
    public void updateBook(int id, String title, String author, String category) {
        String sql = "UPDATE books SET title = ?, author = ?, category = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, title);
            pst.setString(2, author);
            pst.setString(3, category);
            pst.setInt(4, id);

            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Book updated successfully!");
            else System.out.println("Book not found!");

        } catch (SQLException e) {
            System.out.println("Error updating book: " + e.getMessage());
        }
    }
}
