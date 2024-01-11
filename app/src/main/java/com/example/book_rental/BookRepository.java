package com.example.book_rental;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.book_rental.BookDao;
import com.example.book_rental.Database;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> books;

    BookRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        books = bookDao.findAllBooks();
    }

    LiveData<List<Book>> findAll() {
        return books;
    }
    Book findById(int id) {
        return bookDao.findBookWithId(id);
    }
    List<Book> findBookWithTitle(String title) { return bookDao.findBookWithTitle(title); }

    void insert(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(book);
        });
    }

    void update(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(book);
        });
    }

    void delete(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(book);
        });
    }
}