package com.hungry.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TeamStatisticsDTO {
    int totalTeamTaskNumber;
    int totalTeamFinishTaskNumber;
    int totalYourFinishTeamTaskNumber;
}
