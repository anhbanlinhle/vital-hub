package com.main.server.entity;

import com.main.server.entity.primaryPair.FriendPair;
import com.main.server.entity.primaryPair.PlayerAndCompPair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "participants")
@IdClass(PlayerAndCompPair.class)
public class Participants implements Serializable {

    private static final long serialVersionUID = -197553281792804396L;

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "comp_id")
    private Long compId;
}
