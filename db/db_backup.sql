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

CREATE TABLE post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    exercise_id BIGINT,
    title text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    image text
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
    is_deleted BOOLEAN DEFAULT FALSE,
    type ENUM('RUNNING', 'BICYCLING', 'PUSHUP'),
    duration TIME,
    background text
);
ALTER TABLE competition ADD CONSTRAINT compe_fk_1 FOREIGN KEY (host_id) REFERENCES user (id) ON DELETE SET NULL;

CREATE TABLE participants (
    participant_id BIGINT,
    comp_id BIGINT,
    PRIMARY KEY (participant_id, comp_id)
);

CREATE TABLE exercise (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    type ENUM('RUNNING', 'BICYCLING', 'PUSHUP', 'GYM'),
    calo FLOAT,
    user_id BIGINT
);
ALTER TABLE exercise ADD CONSTRAINT ex_fk_1 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

CREATE TABLE bicycling (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exercise_id BIGINT,
    distance FLOAT
);
ALTER TABLE bicycling ADD CONSTRAINT bic_fk_1 FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE;

CREATE TABLE running (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exercise_id BIGINT,
    step INT
);
ALTER TABLE running ADD CONSTRAINT run_fk_1 FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE;

CREATE TABLE push_up (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exercise_id BIGINT,
    rep INT
);
ALTER TABLE push_up ADD CONSTRAINT pu_fk_1 FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE;

CREATE TABLE gym (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exercise_id BIGINT,
    group_id BIGINT
);
ALTER TABLE gym ADD CONSTRAINT gym_fk_1 FOREIGN KEY (exercise_id) REFERENCES exercise (id) ON DELETE CASCADE;

CREATE TABLE compe_ex (
    exercise_id BIGINT,
    compe_id BIGINT
);
ALTER TABLE compe_ex ADD CONSTRAINT compe_ex_prk PRIMARY KEY (exercise_id, compe_id);

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

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (1, 'test@gmail.com', 'test', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (1, 100, 100, 5, 'test description');

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (2, 'test2@gmail.com', 'test2', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (2, 100, 100, 5, 'test2 description');

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (3, 'nhimcoi262@gmail.com', 'test2', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (3, 100, 100, 5, 'test2 description');

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (4, 'minhtuan3154@gmail.com', 'test2', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (4, 100, 100, 5, 'test2 description');

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (5, 'kobehng@gmail.com', 'test2', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (5, 100, 100, 5, 'test2 description');

INSERT INTO user (id, gmail, name, sex, phone_no, avatar, dob)
VALUES (6, 'linhyeunguyen1111@gmail.com', 'test2', 'MALE', '12312422', 'https://lh3.googleusercontent.com/a/ACg8ocLY7LrsvJy9YlvROkNVsHsBFfRrjxgkwv26Q-cuyy7YFes=s96-c', '2003-12-28');

INSERT INTO user_detail (user_id, current_height, current_weight, exercise_days_per_week, description)
VALUES (6, 100, 100, 5, 'test2 description');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (1, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://c02.purpledshub.com/uploads/sites/48/2023/02/why-sky-blue-2db86ae.jpg');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (2, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://upload.wikimedia.org/wikipedia/vi/thumb/a/a1/Man_Utd_FC_.svg/1200px-Man_Utd_FC_.svg.png');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (3, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://baocantho.com.vn/image/fckeditor/upload/2018/20180829/images/malone.jpg');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (4, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://staticg.sportskeeda.com/editor/2023/09/f29a8-16954713783435-1920.jpg?w=840');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (5, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://storage.googleapis.com/cdn-entrade/bovagau-meme/ban-qua-hu-ban-se-bi-dam-daddy-p_1680063754');

INSERT INTO post (id ,user_id, exercise_id, title, created_at, updated_at, is_deleted, image)
VALUES (6, 1, 1, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'https://storage.googleapis.com/cdn-entrade/bovagau-meme/yda1680038570_1680063486');

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (1, 1, 'dep trai 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (2, 2, 'dep trai 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (1, 1, 'dep trai 3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (2, 1, 'dep trai 4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (2, 3, 'dep trai 5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO comment (user_id, post_id, content, created_at, updated_at, is_deleted)
VALUES (3, 4, 'dep trai 6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 1, 'RUNNING');

INSERT INTO running (exercise_id, step)
VALUES (1, 5000);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 1, 'RUNNING');

INSERT INTO running (exercise_id, step)
VALUES (5, 4000);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 2, 'RUNNING');

INSERT INTO running (exercise_id, step)
VALUES (6, 3000);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 2, 'RUNNING');

INSERT INTO running (exercise_id, step)
VALUES (7, 4200);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 3, 'RUNNING');

INSERT INTO running (exercise_id, step)
VALUES (8, 10000);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 1, 'BICYCLING');

INSERT INTO bicycling (exercise_id, distance)
VALUES (2, 500.1234);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 1, 'PUSHUP');

INSERT INTO push_up (exercise_id, rep)
VALUES (3, 500);

INSERT INTO exercise (id, started_at, ended_at, calo, user_id, type)
VALUES (4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 300, 1, 'GYM');

INSERT INTO gym (exercise_id, group_id)
VALUES (4, 4);

INSERT INTO competition (id, host_id, title, created_at, started_at, ended_at, is_deleted, type, duration, background)
VALUES (1, 2, 'lorem ipsum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'RUNNING', '00:30:00', 'https://nld.mediacdn.vn/2020/12/8/13047134238384917728367743121356127102584377n-1607402858403113582018.jpg');

INSERT INTO competition (id, host_id, title, created_at, started_at, ended_at, is_deleted, type, duration, background)
VALUES (2, 2, 'push up', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'PUSHUP', '00:30:00', 'https://i.ytimg.com/vi/7fFVQO1vJjI/maxresdefault.jpg');

INSERT INTO compe_ex (exercise_id, compe_id) VALUES (1, 1);
INSERT INTO compe_ex (exercise_id, compe_id) VALUES (6, 1);
INSERT INTO compe_ex (exercise_id, compe_id) VALUES (3, 2);
INSERT INTO compe_ex (exercise_id, compe_id) VALUES (8, 1);

INSERT INTO participants (participant_id, comp_id) VALUES (1, 1);
INSERT INTO participants (participant_id, comp_id) VALUES (2, 1);
INSERT INTO participants (participant_id, comp_id) VALUES (3, 1);
INSERT INTO participants (participant_id, comp_id) VALUES (1, 2);
INSERT INTO participants (participant_id, comp_id) VALUES (2, 2);
INSERT INTO participants (participant_id, comp_id) VALUES (3, 2);

INSERT INTO friend (first_user_id, second_user_id, status) VALUES (1, 2, 'ACCEPTED');
INSERT INTO friend (first_user_id, second_user_id, status) VALUES (3, 2, 'ACCEPTED');
INSERT INTO friend (first_user_id, second_user_id, status) VALUES (3, 4, 'ACCEPTED');