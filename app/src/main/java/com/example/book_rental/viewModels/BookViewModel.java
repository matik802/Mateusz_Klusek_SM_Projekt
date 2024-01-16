package com.example.book_rental.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.book_rental.models.Book;
import com.example.book_rental.repositories.BookRepository;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository bookRepository;
    private LiveData<List<Book>> books;

    public BookViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        books = bookRepository.findAll();
    }

    public LiveData<List<Book>> findAll() {
        return books;
    }
    public Book findById(int id) {
        return bookRepository.findById(id);
    }
    public LiveData<List<Book>> findBooksWithTitle(String title) { return bookRepository.findBooksWithTitle(title); }

    public void insert(Book book) {
        bookRepository.insert(book);
    }

    public void update(Book book) {
        bookRepository.update(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
