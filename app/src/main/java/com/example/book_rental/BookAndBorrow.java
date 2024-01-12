package com.example.book_rental;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BookAndBorrow {
    @Embedded
    public Borrow borrow;
    @Relation(
            parentColumn = "book_id",
            entityColumn = "id"
    )
    public Book book;
}
