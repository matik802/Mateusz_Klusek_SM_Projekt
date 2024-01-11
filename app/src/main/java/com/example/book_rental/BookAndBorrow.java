package com.example.book_rental;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BookAndBorrow {
    @Embedded
    Borrow borrow;
    @Relation(
            parentColumn = "book_id",
            entityColumn = "id"
    )
    Book book;
}
