package com.example.book_rental.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.book_rental.dao.Dao;
import com.example.book_rental.database.Database;
import com.example.book_rental.models.User;

import java.util.List;

public class UserRepository {
    private Dao bookDao;
    private LiveData<List<User>> users;

    public UserRepository(Application application) {
        Database database = Database.getDatabase(application);
        bookDao = database.bookDao();
        users = bookDao.findAllUsers();
    }

    public LiveData<List<User>> findAll() {
        return users;
    }
    public User findById(int id) {
        return bookDao.findUserWithId(id);
    }
    public User findByEmail(String email) {
        return bookDao.findUserWithEmail(email);
    }

    public void insert(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.insert(user);
        });
    }

    public void update(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.update(user);
        });
    }

    public void delete(User user) {
        Database.databaseWriteExecutor.execute(() -> {bookDao.delete(user);
        });
    }
}
