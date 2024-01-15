package com.example.book_rental.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.book_rental.models.BookAndUserForBorrow;
import com.example.book_rental.dao.Dao;
import com.example.book_rental.database.Database;
import com.example.book_rental.models.Borrow;

import java.util.List;

public class BorrowRepository {
    private Dao bookDao;
    private LiveData<List<Borrow>> borrows;
    private LiveData<List<BookAndUserForBorrow>> booksAndUsersForBorrows;

    public BorrowRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        borrows = bookDao.findAllBorrows();
        booksAndUsersForBorrows = bookDao.findBooksAndUsersForBorrows();
    }

    public LiveData<List<Borrow>> findAll() {
        return borrows;
    }
    public LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrows() {
        return booksAndUsersForBorrows;
    }
    public LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrowsForUser(int userId) {
        return bookDao.findBooksAndUsersForBorrowsForUser(userId);
    }
    public Borrow findById(int id) {
        return bookDao.findBorrowWithId(id);
    }

    public void insert(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(borrow);
        });
    }

    public void update(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(borrow);
        });
    }

    public void delete(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(borrow);
        });
    }
}
