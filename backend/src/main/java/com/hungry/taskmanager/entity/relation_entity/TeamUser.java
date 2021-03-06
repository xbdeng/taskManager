package com.hungry.taskmanager.entity.relation_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeamUser {
    BigInteger tuId;
    BigInteger teamId;
    BigInteger userId;
    String identity;
}
