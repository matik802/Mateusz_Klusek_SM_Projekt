package com.example.book_rental;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private BookDao bookDao;
    private LiveData<List<User>> users;

    UserRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        users = bookDao.findAllUsers();
    }

    LiveData<List<User>> findAll() {
        return users;
    }
    User findById(int id) {
        return bookDao.findUserWithId(id);
    }
    User findByEmail(String email) {
        return bookDao.findUserWithEmail(email);
    }

    void insert(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(user);
        });
    }

    void update(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(user);
        });
    }

    void delete(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(user);
        });
    }
}
