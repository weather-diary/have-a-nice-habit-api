package com.hanh.web;

import static com.hanh.utils.ResponseEntityConstants.RESPONSE_CREATED;
import static com.hanh.utils.ResponseEntityConstants.RESPONSE_OK;

import com.hanh.domain.habit.Habit;
import com.hanh.domain.habit.HabitRepository;
import com.hanh.domain.user.User;
import com.hanh.domain.user.UserRepository;
import com.hanh.exception.DataNotFoundException;
import com.hanh.service.HabitService;
import com.hanh.web.dto.HabitRequestDto;
import com.hanh.web.dto.HabitResponseDto;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/habit")
public class HabitController {
    private final HabitService habitService;
    private final HabitRepository habitRepository;

    private final UserRepository userRepository;

    @PostMapping("{habitId}/change-status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long habitId,
                                             @RequestParam("date")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                             LocalDate date
    ) {
        try {
            Optional<Habit> findHabit = habitRepository.findById(habitId);
            habitRepository.save(findHabit.get().changeStatus(date));
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException();
        }

        return RESPONSE_OK;
    }

    @PostMapping
    public ResponseEntity createHabit(@RequestBody @Valid HabitRequestDto habitRequestDto, BindingResult result) {
        Habit savedHabit = habitService.createHabit(habitRequestDto);
        return RESPONSE_CREATED;
    }

    @GetMapping
    public ResponseEntity readAllHabit(String nickname,
                                       @RequestParam("date")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd")
                                       LocalDate date) {

        Optional<User> user = userRepository.findByNickname(nickname);
        List<HabitResponseDto> habitList = habitService.findAllByUser(user.get(), date);
        return ResponseEntity.status(HttpStatus.OK).body(habitList);

    }

    @PutMapping("{habitId}")
    public ResponseEntity updateHabit(@PathVariable("habitId") Long habitId,
                                      @RequestBody @Valid HabitRequestDto habitRequestDto, BindingResult result) {
        Habit updatedHabit = habitService.put(habitId, habitRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 해빗이 수정되었습니다.");
    }

    @PostMapping("{habitId}/add-date")
    public ResponseEntity addDate(@PathVariable Long habitId,
                                  @RequestParam("date")
                                  @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate date) {

        Optional<Habit> findHabit = habitRepository.findById(habitId);

        if (findHabit.isPresent()) {
            List<LocalDate> dateList = findHabit.get().getDateList();
            if (!dateList.contains(date)) {
                habitRepository.save(findHabit.get().addDate(date));
            } else {
                habitRepository.save(findHabit.get().removeDate(date));
            }
        } else {
            throw new DataNotFoundException();
        }

        return RESPONSE_OK;
    }

    // 로그아웃 확인용
    @GetMapping("/check")
    public ResponseEntity checkLogout() {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}
