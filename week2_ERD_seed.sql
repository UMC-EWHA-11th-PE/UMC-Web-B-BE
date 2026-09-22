INSERT INTO member (nickname)
VALUES
('민서'),
('수현');

INSERT INTO store (store_name)
VALUES
('달빛 식당'),
('행복 카페'),
('우리 동네 치킨');

INSERT INTO mission (store_id, mission_title, point)
VALUES
(1, '대표 메뉴 주문하기', 1000),
(1, '리뷰 작성하기', 1000),
(2, '음료 주문하기', 1000),
(3, '치킨 메뉴 주문하기', 1000);

INSERT INTO member_mission
    (member_id, mission_id, completed_at, awarded)
VALUES
    (1, 1, '2026-09-20 15:00:00', TRUE),
    (1, 3, '2026-09-21 18:00:00', TRUE),
    (2, 2, NULL, FALSE);