package com.example.book_rental.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.book_rental.models.Book;
import com.example.book_rental.models.Borrow;
import com.example.book_rental.models.User;

public class BookAndUserForBorrow {
    @Embedded
    public Borrow borrow;
    @Relation(
            parentColumn = "book_id",
            entityColumn = "id"
    )
    public Book book;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "id"
    )
    public User user;
}
