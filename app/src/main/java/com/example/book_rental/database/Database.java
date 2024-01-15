package com.example.book_rental.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.book_rental.models.Book;
import com.example.book_rental.models.Borrow;
import com.example.book_rental.models.User;
import com.example.book_rental.dao.Dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Book.class, Borrow.class, User.class}, version = 2, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();
    public abstract Dao bookDao();

    public static Database getDatabase(final Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class,"book_database")
                    .addCallback(roomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return databaseInstance;
    }

    public static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                Dao dao = databaseInstance.bookDao();
                Book book;
                book = new Book("Clean Code","Robert C. Martin", "978-83-900210-1-0", "https://content.wepik.com/statics/90897927/preview-page0.jpg",1);
                dao.insert(book);
                book =  new Book("The Psychology of Money"," Morgan Housel", "117-65-537888-4-4", "https://m.media-amazon.com/images/I/41gr3r3FSWL.jpg", 2);
                dao.insert(book);
                book = new Book("Deep Work: Rules for Focused Success in a Distracted World"," Cal Newport", "888-34-555555-6-6", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5wB4XU0KtV_bMX0E01U64h7SBgDkpedK7Lg&usqp=CAU", 49);
                dao.insert(book);
                book = new Book("Surrounded by Idiots: The Four Types of Human Behaviour (or, How to Understand Those Who Cannot Be Understood)"," Thomas Erikson", "233-77-645533-1-5", "https://www.adobe.com/express/create/cover/media_1042ff48ca7f6d77bde5524b0b57a191dd76ee0a0.jpeg?width=400&format=jpeg&optimize=medium", 40);
                dao.insert(book);
            });
        }
    };
}