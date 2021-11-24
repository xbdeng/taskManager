package com.hungry.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PersonalStatisticsDTO {
    int totalTaskNumber;
    int totalFinishTaskNumber;
    int totalOverdueTaskNumber;

    int totalTeamTaskNumber;
    int totalFinishTeamTaskNumber;
    int totalOverdueTeamTaskNumber;

    List<Year_count> finishTaskNumberByYears;
    List<Month_count> finishTaskNumbersByMonths;
    List<Day_count> finishTaskNumbersByDays;

    List<Year_count> startTaskNumberByYears;
    List<Month_count> startTaskNumbersByMonths;
    List<Day_count> startTaskNumbersByDays;

    List<Year_count> dueTaskNumberByYears;
    List<Month_count> dueTaskNumbersByMonths;
    List<Day_count> dueTaskNumbersByDays;

}


