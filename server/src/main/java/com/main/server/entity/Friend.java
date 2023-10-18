package com.main.server.entity;

import com.main.server.entity.primaryPair.FriendPair;
import com.main.server.utils.enums.FriendStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friend")
@IdClass(FriendPair.class)
public class Friend implements Serializable {

    private static final long serialVersionUID = -197553281792804396L;

    @Id
    @Column(name = "first_user_id")
    private Long firstUserId;

    @Id
    @Column(name = "second_user_id")
    private Long secondUserId;

    @Column(name = "status")
    private FriendStatus status;
}
