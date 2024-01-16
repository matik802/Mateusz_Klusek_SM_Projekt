package com.example.book_rental.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.book_rental.models.Book;
import com.example.book_rental.models.BookAndUserForBorrow;
import com.example.book_rental.models.Borrow;
import com.example.book_rental.models.User;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("DELETE FROM book")
    void deleteAll();

    @Query("SELECT * FROM book ORDER BY title")
    LiveData<List<Book>> findAllBooks();

    @Query("SELECT * FROM book WHERE title LIKE  '%' || :title || '%'")
    LiveData<List<Book>> findBooksWithTitle(String title);

    @Query("SELECT * FROM book WHERE id = :id")
    Book findBookWithId(int id);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Borrow borrow);

    @Update
    void update(Borrow borrow);

    @Delete
    void delete(Borrow borrow);

    @Query("SELECT * FROM borrow WHERE id = :id")
    Borrow findBorrowWithId(int id);

    @Query("SELECT * FROM borrow")
    LiveData<List<Borrow>> findAllBorrows();

    @Query("SELECT * FROM borrow ORDER BY borrow.date desc")
    LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrows();

    @Query("SELECT * FROM borrow " +
            "JOIN book ON borrow.book_id = book.id " +
            "JOIN user ON borrow.user_id = user.id " +
            "WHERE borrow.user_id = :userId")
    LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrowsForUser(int userId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE id = :id")
    User findUserWithId(int id);
    @Query("SELECT * FROM user WHERE email = :email")
    User findUserWithEmail(String email);

    @Query("SELECT * FROM user")
    LiveData<List<User>> findAllUsers();
}
