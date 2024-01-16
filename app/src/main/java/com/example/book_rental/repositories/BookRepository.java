package com.example.book_rental.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.book_rental.dao.Dao;
import com.example.book_rental.database.Database;
import com.example.book_rental.models.Book;

import java.util.List;

public class BookRepository {
    private Dao bookDao;
    private LiveData<List<Book>> books;

    public BookRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        books = bookDao.findAllBooks();
    }

    public LiveData<List<Book>> findAll() {
        return books;
    }
    public Book findById(int id) {
        return bookDao.findBookWithId(id);
    }
    public LiveData<List<Book>> findBooksWithTitle(String title) { return bookDao.findBooksWithTitle(title); }

    public void insert(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(book);
        });
    }

    public void update(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(book);
        });
    }

    public void delete(Book book) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(book);
        });
    }
}