package com.example.book_rental;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BorrowRepository {
    private BookDao bookDao;
    private LiveData<List<Borrow>> borrows;
    private LiveData<List<BookAndUserForBorrow>> booksAndUsersForBorrows;

    BorrowRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        borrows = bookDao.findAllBorrows();
        booksAndUsersForBorrows = bookDao.findBooksAndUsersForBorrows();
    }

    LiveData<List<Borrow>> findAll() {
        return borrows;
    }
    LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrows() {
        return booksAndUsersForBorrows;
    }
    LiveData<List<BookAndUserForBorrow>> findBooksAndUsersForBorrowsForUser(int userId) {
        return bookDao.findBooksAndUsersForBorrowsForUser(userId);
    }
    Borrow findById(int id) {
        return bookDao.findBorrowWithId(id);
    }

    void insert(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(borrow);
        });
    }

    void update(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(borrow);
        });
    }

    void delete(Borrow borrow) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(borrow);
        });
    }
}
