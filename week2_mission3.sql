USE library;
SELECT 
    b.title,
    t.name AS tag_name,
    CASE
        WHEN bl.user_id IS NOT NULL THEN TRUE
        ELSE FALSE
    END AS is_liked
FROM book b 
JOIN book_tag bt 
    ON b.book_id = bt.book_id
JOIN tag t 
    ON bt.tag_id = t.tag_id
LEFT JOIN book_like bl
    ON b.book_id = bl.book_id
    AND bl.user_id = q
WHERE b.book_id = 1;