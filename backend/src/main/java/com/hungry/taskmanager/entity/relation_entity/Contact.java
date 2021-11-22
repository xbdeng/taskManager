package com.hungry.taskmanager.entity.relation_entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @TableId(type= IdType.AUTO)
    BigInteger uuId;
    BigInteger person;
    BigInteger friend;
}
