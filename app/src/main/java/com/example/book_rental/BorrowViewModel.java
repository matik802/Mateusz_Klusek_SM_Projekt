package com.example.book_rental;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BorrowViewModel extends AndroidViewModel {
    private BorrowRepository borrowRepository;
    private LiveData<List<Borrow>> borrows;

    public BorrowViewModel(@NonNull Application application) {
        super(application);
        borrowRepository = new BorrowRepository(application);
        borrows = borrowRepository.findAll();
    }

    public LiveData<List<Borrow>> findAll() {
        return borrows;
    }
    Borrow findById(int id) {
        return borrowRepository.findById(id);
    }

    public void insert(Borrow borrow) {
        borrowRepository.insert(borrow);
    }

    public void update(Borrow borrow) {
        borrowRepository.update(borrow);
    }

    public void delete(Borrow borrow) {
        borrowRepository.delete(borrow);
    }
}
