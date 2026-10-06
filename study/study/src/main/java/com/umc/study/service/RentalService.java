package com.umc.study.service;

import com.umc.study.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;

    public void createRental(int userId, int bookId) {
        rentalRepository.save(userId, bookId);
    }
    public void returnBook(int rentalId) {
        rentalRepository.returnBook(rentalId);
    }
}
