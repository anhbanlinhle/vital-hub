DROP DATABASE IF EXISTS `vital_hub`;
CREATE DATABASE `vital_hub`;
USE vital_hub;

CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    gmail VARCHAR(100) UNIQUE,
    name VARCHAR(100),
    sex ENUM ('MALE', 'FEMALE'),
    phone_no VARCHAR(15),
    avatar text,
    dob DATE
);

CREATE TABLE user_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    current_height FLOAT,
    current_weight FLOAT,
    exercise_days_per_week INT(1),
    description text
);
ALTER TABLE user_detail ADD CONSTRAINT ud_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE friend (
    first_user_id BIGINT,
    second_user_id BIGINT,
    status ENUM ('PENDING', 'ACCEPTED')
);
ALTER TABLE friend ADD CONSTRAINT fr_prk PRIMARY KEY (first_user_id, second_user_id);

CREATE TABLE workout_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    group_id BIGINT,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    calo float
);
ALTER TABLE workout_history ADD CONSTRAINT wh_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE workout_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bmi_upper_bound FLOAT,
    bmi_lower_bound FLOAT,
    body_status ENUM ('SUPER_THIN, THIN, NORMAL, FAT, OBESITY'),
    suggest_group_id BIGINT
);

CREATE TABLE workout_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT,
    name VARCHAR(50),
    description text,
    total_calo FLOAT
);

CREATE TABLE jogging (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    distance FLOAT,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    calo FLOAT
);
ALTER TABLE jogging ADD CONSTRAINT jg_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    jogging_id BIGINT,
    title text,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
ALTER TABLE post ADD CONSTRAINT p_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    post_id BIGINT,
    content text,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
ALTER TABLE comment ADD CONSTRAINT cmt_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;
ALTER TABLE comment ADD CONSTRAINT cmt_fk_2 FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE;

CREATE TABLE competition (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    host_id BIGINT,
    title text,
    created_at TIMESTAMP,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE participants (
    participant_id BIGINT,
    comp_id BIGINT,
    PRIMARY KEY (participant_id, comp_id)
);

CREATE TABLE competition_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    competition_id BIGINT,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    winner_id BIGINT
);