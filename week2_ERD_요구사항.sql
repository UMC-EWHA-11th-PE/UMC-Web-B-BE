SELECT
    s.store_name,
    m.mission_title,
    m.point,
    mm.completed_at
FROM member_mission mm
JOIN mission m
    ON mm.mission_id = m.mission_id
JOIN store s
    ON m.store_id = s.store_id
WHERE mm.member_id = 1
ORDER BY mm.completed_at DESC;