package com.umc.study.service;

import com.umc.study.repository.RentalRepository;
import org.springframework.stereotype.Service;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public void createRental(Long userId, Long bookId) {
        rentalRepository.save(userId, bookId);
    }

    public boolean returnBook(Long rentalId) {
        return rentalRepository.updateReturnedAt(rentalId) > 0;
    }
}