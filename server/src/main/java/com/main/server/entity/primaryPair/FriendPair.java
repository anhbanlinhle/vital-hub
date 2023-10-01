package com.main.server.entity.primaryPair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendPair implements Serializable {
    private Long firstUserId;
    private Long secondUserId;

}
