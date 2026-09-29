package com.umc.study.service;

import com.umc.study.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookService {
    //Repository를 생성자 주입으로 데려온다.
    private final BookRepository bookRepository;

    public List<Map<String, Object>> getAllBooks() {
        //Repository가 가져온 도서 목록을 그대로 반환
        return bookRepository.findAll();
    }

    public void createBook(Map<String, Object> body) {
        bookRepository.save(body);
    }

    public List<Map<String, Object>> getBooksByCategory(Long categoryId) {

        return bookRepository.findByCategoryId(categoryId);
    }
}