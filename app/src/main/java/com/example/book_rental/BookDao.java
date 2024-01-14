package com.example.book_rental;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {

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

    @Query("SELECT * FROM book WHERE title LIKE :title")
    List<Book> findBookWithTitle(String title);

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

    @Query("SELECT * FROM borrow JOIN book ON borrow.book_id = book.id")
    LiveData<List<BookAndUserForBorrow>> getBooksAndBorrows();


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
