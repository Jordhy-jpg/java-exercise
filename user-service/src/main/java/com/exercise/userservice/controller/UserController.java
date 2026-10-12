package com.exercise.userservice.controller;

import com.exercise.userservice.dto.CreateUserRequestDto;
import com.exercise.userservice.dto.CreateUserResponseDto;
import com.exercise.userservice.dto.GetUserRequestDto;
import com.exercise.userservice.dto.GetUserResponseDto;
import com.exercise.userservice.dto.GetUsersRequestDto;
import com.exercise.userservice.dto.GetUsersResponseDto;
import com.exercise.userservice.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public GetUsersResponseDto getUsers(
            @RequestParam(name = "pageNum", defaultValue = "1") @Min(1) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) Integer pageSize
    ) {
        GetUsersRequestDto requestDto = new GetUsersRequestDto();
        requestDto.setPageNum(pageNum - 1);
        requestDto.setPageSize(pageSize);

        return userService.getUsers(requestDto);
    }

    @GetMapping("/users/{id}")
    public GetUserResponseDto getUser(
            @PathVariable("id") @Min(1) Integer id
    ) {
        GetUserRequestDto requestDto = new GetUserRequestDto();
        requestDto.setId(id);

        return userService.getUser(requestDto);
    }

    @PostMapping("/users")
    public CreateUserResponseDto createUser(@Valid CreateUserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }
}
