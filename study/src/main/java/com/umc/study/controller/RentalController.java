package com.umc.study.controller;

import com.umc.study.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    // Java 21에서 사용 가능한 요청 DTO
    public record RentalRequest(Long userId, Long bookId) {}

    @PostMapping
    public ResponseEntity<Void> createRental(
            @RequestBody RentalRequest request) {

        if (request.userId() == null || request.bookId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "userId와 bookId는 필수입니다."
            );
        }

        rentalService.createRental(request.userId(), request.bookId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{rentalId}/return")
    public ResponseEntity<Void> returnBook(
            @PathVariable("rentalId") Long rentalId){
        boolean updated=rentalService.returnBook(rentalId);

        if(!updated){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}