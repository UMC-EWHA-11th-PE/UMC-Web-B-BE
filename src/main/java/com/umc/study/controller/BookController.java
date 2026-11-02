package com.umc.study.controller;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 실습 1: 전체 도서 조회
    @GetMapping
    public List<BookResponse> getBooks() {
        return bookService.getAllBooks();
    }

    // 실습 2: 신규 도서 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    // 3주차 미션 1: 카테고리별 도서 조회
    @GetMapping("/category/{categoryId}")
    public List<BookResponse> getBooksByCategory(
            @PathVariable int categoryId) {
        return bookService.getBooksByCategory(categoryId);
    }
}