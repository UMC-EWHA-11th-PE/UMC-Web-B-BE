package com.umc.study.repository;
import com.umc.study.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    //모든 책 조회
    List<Book> findAllByOrderByBookIdDesc();


}




