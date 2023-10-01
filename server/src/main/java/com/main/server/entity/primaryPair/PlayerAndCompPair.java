package com.main.server.entity.primaryPair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlayerAndCompPair implements Serializable {

    private Long participantId;
    private Long compId;
}
