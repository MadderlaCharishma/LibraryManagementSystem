package librarymanagement;

import java.sql.Date;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean isIssued;
    private Date issueDate;

    public Book(int id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = false;
        this.issueDate = null;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isIssued() { return isIssued; }
    public Date getIssueDate() { return issueDate; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setIssued(boolean issued) { this.isIssued = issued; }
    public void setIssueDate(Date date) { this.issueDate = date; }
}
