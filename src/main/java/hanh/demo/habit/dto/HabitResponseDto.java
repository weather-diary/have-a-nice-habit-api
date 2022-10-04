package hanh.demo.habit.dto;

import hanh.demo.habit.domain.Habit;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class HabitResponseDto {

    private Long id;

    private boolean isDisplay;

    // 오늘 했는지 안했는지 확인
    private boolean isAchieved;

    private Integer count;

    @Builder
    public HabitResponseDto(
            Habit habit
    ){
        this.id = habit.getId();
        this.isDisplay = habit.getIsDisplay();
        this.count = habit.getCount();
        this.isAchieved = getIsAchieved(habit.getDateList());
    }

    private boolean getIsAchieved(
            List<Date> dateList
    ){
        Date now = new Date();
        return dateList.contains(now);
    }

}
