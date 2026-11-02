package com.umc.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository //DB에 접근하는 클래스로 등록
@RequiredArgsConstructor
public class RentalRepository {

    private final JdbcTemplate jdbcTemplate; //MySQL에 SQL 실행하는 도구

    //대여 기록을 저장하는 메서드
    public void save(int userId, int bookId) {
        String sql = """
                INSERT INTO rental (user_id, book_id, rented_at, due_at)
                VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY))
                """;

        jdbcTemplate.update(sql, userId, bookId);
    }
}