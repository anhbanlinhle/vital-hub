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
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    calo float
);
ALTER TABLE workout_history ADD CONSTRAINT wh_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE workout_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bmi_upper_bound FLOAT,
    bmi_lower_bound FLOAT,
    body_status ENUM ('SUPER_THIN', 'THIN', 'NORMAL', 'FAT', 'OBESITY'),
    suggest_group_id BIGINT
);

CREATE TABLE workout_exercises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    group_id BIGINT,
    name VARCHAR(50),
    description text,
    sets INT,
    reps_per_set INT,
    total_calo FLOAT
);

CREATE TABLE jogging (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    distance FLOAT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    calo FLOAT
);
ALTER TABLE jogging ADD CONSTRAINT jg_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    jogging_id BIGINT,
    title text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
ALTER TABLE post ADD CONSTRAINT p_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    post_id BIGINT,
    content text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);
ALTER TABLE comment ADD CONSTRAINT cmt_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;
ALTER TABLE comment ADD CONSTRAINT cmt_fk_2 FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE;

CREATE TABLE competition (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    host_id BIGINT,
    title text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    winner_id BIGINT
);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (1, 'Push Ups' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 60);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (1, 'Dumbbell Bench Press' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 12, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (1, 'Incline Dumbbell' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 70);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (1, 'Dumbbell Fly' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 15, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (1, 'Cable Crossovers' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 8, 60);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (2, 'Pull Ups' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 60);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (2, 'Wide Grip Lat Pull Downs' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 3, 20, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (2, 'Seated Row Machine' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 70);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (2, 'Bent Over Row' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 12, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (2, 'Straight Bar Curl' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 100);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (3, 'Squats' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (3, 'Leg Press Machine' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 3, 20, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (3, 'Walking Lunges' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (3, 'Leg Extension Machine' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 12, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (3, 'Straight Leg Deadlifts' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 3, 10, 100);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (4, 'Handstand Push Up' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (4, 'Military Press' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (4, 'Upright Rows' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 15, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (4, 'Lateral Raises' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 20, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (4, 'Rear Delt Raise' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 80);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (5, 'Tricep Pullovers' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 60);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (5, 'Tricep Kickbacks' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (5, 'Dips' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 15, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (5, 'Concentration Curls' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 12, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (5, 'Preacher Curl' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 70);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (6, 'Preacher Curl' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 70);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (6, 'Rear Delt Raise' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (6, 'Handstand Push Up' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (6, 'Bent Over Row' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 12, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (6, 'Cable Crossovers' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 8, 60);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (7, 'Straight Bar Curl' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (7, 'Wide Grip Lat Pull Downs' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 3, 20, 120);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (7, 'Dumbbell Fly' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 15, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (7, 'Cable Crossovers' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 8, 60);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (7, 'Dips' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 15, 80);

INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (8, 'Seated Row Machine' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 70);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (8, 'Tricep Kickbacks' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (8, 'Walking Lunges' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 10, 100);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (8, 'Lateral Raises' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 4, 20, 80);
INSERT INTO workout_exercises (group_id, name, description, sets, reps_per_set, total_calo)
VALUES (8, 'Incline Dumbbell' , 'Lorem ipsum dolor sit amet, consectetur adipiscing elit', 5, 10, 70);

INSERT INTO workout_mapping (bmi_upper_bound, bmi_lower_bound, body_status, suggest_group_id)
VALUES (17, 0, 'SUPER_THIN', 5);
INSERT INTO workout_mapping (bmi_upper_bound, bmi_lower_bound, body_status, suggest_group_id)
VALUES (18.5, 17, 'THIN', 7);
INSERT INTO workout_mapping (bmi_upper_bound, bmi_lower_bound, body_status, suggest_group_id)
VALUES (25, 18.5, 'NORMAL', 2);
INSERT INTO workout_mapping (bmi_upper_bound, bmi_lower_bound, body_status, suggest_group_id)
VALUES (30, 25, 'FAT', 1);
INSERT INTO workout_mapping (bmi_upper_bound, bmi_lower_bound, body_status, suggest_group_id)
VALUES (1000, 30, 'OBESITY', 3);