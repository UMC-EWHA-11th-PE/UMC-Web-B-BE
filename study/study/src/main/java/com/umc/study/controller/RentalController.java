package com.umc.study.controller;

import com.umc.study.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public String createRental(@RequestBody Map<String, Object> body) {
        int userId = (int) body.get("userId");
        int bookId = (int) body.get("bookId");

        rentalService.createRental(userId, bookId);

        return "도서 대여가 완료되었습니다!";
    }
    @PatchMapping("/{rentalId}/return")
    public String returnBook(@PathVariable int rentalId) {
        rentalService.returnBook(rentalId);

        return "도서 반납이 완료되었습니다!";
    }
}