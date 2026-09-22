CREATE TABLE member (
    member_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(30) NOT NULL
);

CREATE TABLE store (
    store_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_name VARCHAR(100) NOT NULL
);

CREATE TABLE mission (
    mission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    store_id BIGINT NOT NULL,
    mission_title VARCHAR(100) NOT NULL,
    point INT NOT NULL DEFAULT 1000,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

CREATE TABLE member_mission (
    member_mission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    mission_id BIGINT NOT NULL,
    completed_at DATETIME NULL,
    awarded BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (mission_id) REFERENCES mission(mission_id)
);