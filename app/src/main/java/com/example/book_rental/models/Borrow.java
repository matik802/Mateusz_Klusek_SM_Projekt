package com.example.book_rental.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "borrow", foreignKeys = {
        @ForeignKey(
                entity = Book.class,
                parentColumns = {"id"},
                childColumns = {"book_id"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = User.class,
                parentColumns = {"id"},
                childColumns = {"user_id"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
}
)
public class Borrow {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "book_id", index = true)
    private int bookId;
    @ColumnInfo(name = "user_id", index = true)
    private int userId;
    private String status;
    private long date;
    public Borrow(int bookId, int userId, String status, long date) {
        this.bookId = bookId;
        this.userId = userId;
        this.status = status;
        this.date = date;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
}
