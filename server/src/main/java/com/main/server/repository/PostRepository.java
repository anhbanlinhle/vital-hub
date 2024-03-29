package com.main.server.repository;

import com.main.server.entity.Post;
import com.main.server.utils.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = """
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(r.step, ' steps') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN running r on e.id = r.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(r.step, ' steps') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN running r on e.id = r.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id NOT IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(b.distance, ' meters') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN bicycling b on e.id = b.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(b.distance, ' meters') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN bicycling b on e.id = b.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id NOT IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(pu.rep, ' reps') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN push_up pu on e.id = pu.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(pu.rep, ' reps') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN push_up pu on e.id = pu.exercise_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id NOT IN (:friendList))
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(COUNT(we.id), ' exercises') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN gym g on e.id = g.exercise_id JOIN workout_exercises we on g.group_id = we.group_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id IN (:friendList) GROUP BY we.group_id)
            UNION
            (SELECT p.user_id AS userId, p.id AS postId, u.name AS username, p.created_at AS createdAt, p.image AS image, e.type, e.calo, CONCAT(COUNT(we.id), ' exercises') AS score,
            p.title AS title, u.avatar AS avatar, IF(:currentUserId = p.user_id, TRUE, FALSE) AS isOwnedInt FROM post p JOIN exercise e on p.exercise_id = e.id JOIN gym g on e.id = g.exercise_id JOIN workout_exercises we on g.group_id = we.group_id JOIN user u ON p.user_id = u.id
            WHERE p.is_deleted = FALSE AND p.user_id NOT IN (:friendList) GROUP BY we.group_id)
            ORDER BY createdAt DESC
            LIMIT :pageSize OFFSET :page
            """, nativeQuery = true)
    List<PostDto> allPostOrderByCreatedTime(Integer page, Integer pageSize, List<Long> friendList, Long currentUserId);

    @Query("SELECT CASE WHEN p.userId = :userId THEN 1 ELSE 0 END AS isOwnedInt, p.userId AS userId, " +
            "p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, " +
            "CONCAT(e.type, '') AS type, e.calo AS calo, CONCAT(r.step, ' steps') AS score, " +
            "u.avatar AS avatar, p.image AS image FROM Post p JOIN Exercise e ON p.exerciseId = e.id JOIN Running r ON e.id = r.exerciseId " +
            "JOIN User u ON p.userId = u.id WHERE p.isDeleted = FALSE AND p.id = :id")
    Optional<PostDto> getPostRunningWithUserByPid(Long id, Long userId);

    @Query("SELECT CASE WHEN p.userId = :userId THEN 1 ELSE 0 END AS isOwnedInt, p.userId AS userId, " +
            "p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, " +
            "CONCAT(e.type, '') AS type, e.calo AS calo, CONCAT(b.distance, ' meters') AS score, " +
            "u.avatar AS avatar, p.image AS image FROM Post p JOIN Exercise e ON p.exerciseId = e.id JOIN Bicycling b ON e.id = b.exerciseId " +
            "JOIN User u ON p.userId = u.id WHERE p.isDeleted = FALSE AND p.id = :id")
    Optional<PostDto> getPostBicyclingWithUserByPid(Long id, Long userId);

    @Query("SELECT CASE WHEN p.userId = :userId THEN 1 ELSE 0 END AS isOwnedInt, p.userId AS userId, " +
            "p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, " +
            "CONCAT(e.type, '') AS type, e.calo AS calo, CONCAT(pu.rep, ' reps') AS score, " +
            "u.avatar AS avatar, p.image AS image FROM Post p JOIN Exercise e ON p.exerciseId = e.id JOIN PushUp pu ON e.id = pu.exerciseId " +
            "JOIN User u ON p.userId = u.id WHERE p.isDeleted = FALSE AND p.id = :id")
    Optional<PostDto> getPostPushUpWithUserByPid(Long id, Long userId);

    @Query("SELECT CASE WHEN p.userId = :userId THEN 1 ELSE 0 END AS isOwnedInt, p.userId AS userId, " +
            "p.id AS postId, u.name AS username, p.title AS title, p.createdAt AS createdAt, " +
            "CONCAT(e.type, '') AS type, e.calo AS calo, CONCAT(COUNT(we.id), ' reps') AS score, " +
            "u.avatar AS avatar, p.image AS image FROM Post p JOIN Exercise e ON p.exerciseId = e.id JOIN Gym g ON e.id = g.exerciseId " +
            "JOIN WorkoutExercises we ON g.groupId = we.groupId JOIN User u ON p.userId = u.id WHERE p.isDeleted = FALSE AND p.id = :id GROUP BY we.groupId")
    Optional<PostDto> getPostGymWithUserByPid(Long id, Long userId);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}