package com.umc.study.service;

import com.umc.study.dto.BookResponse;
import com.umc.study.dto.CreateBookRequest;
import com.umc.study.entity.Book;
import com.umc.study.entity.Category;
import com.umc.study.repository.BookJpaRepository;
import com.umc.study.repository.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookJpaRepository bookJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    // 실습 1: 전체 도서 조회
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookJpaRepository.findAllByOrderByBookIdDesc()
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    // 실습 2: 도서 등록
    @Transactional
    public BookResponse createBook(CreateBookRequest request) {
        Category category = categoryJpaRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "존재하지 않는 카테고리입니다."
                ));

        Book book = new Book(category, request.title(), request.description());

        return BookResponse.from(bookJpaRepository.save(book));
    }

    // 3주차 미션 1: 카테고리별 도서 조회 유지
    @Transactional(readOnly = true)
    public List<BookResponse> getBooksByCategory(int categoryId) {
        return bookJpaRepository.findByCategory_CategoryId((long) categoryId)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}