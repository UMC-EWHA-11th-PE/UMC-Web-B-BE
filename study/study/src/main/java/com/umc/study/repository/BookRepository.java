package com.umc.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    //스프링의 DB 통신도구인 JdbcTemplate 주입
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findAll() {
        String sql = "SELECT * FROM book";

        //쿼리를 실행하고 결과를 List(Map) 형태의 날 것 데이터로 긁어온다.
        //Map의 Key는 컬럼명(title), Value는 실제 데이터(달빛 도서관)이 된다.
        return jdbcTemplate.queryForList(sql);
    }

    public void save(Map<String, Object> body) {
        String sql = "INSERT INTO book (category_id, title, description, is_available) VALUES (?, ?, ?, true)";

        jdbcTemplate.update(
                sql,
                body.get("categoryId"),
                body.get("title"),
                body.get("description")
        );
    }

    public List<Map<String, Object>> findByCategoryId(Long categoryId){
        String sql = "SELECT * FROM book WHERE category_id = ?";

       return jdbcTemplate.queryForList(sql, categoryId);
    }

}

