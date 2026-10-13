package com.exercise.publicapi.controller;

import com.exercise.publicapi.dto.CreateListingRequestDto;
import com.exercise.publicapi.dto.CreateListingResponseDto;
import com.exercise.publicapi.dto.CreateUserRequestDto;
import com.exercise.publicapi.dto.CreateUserResponseDto;
import com.exercise.publicapi.dto.GetListingsRequestDto;
import com.exercise.publicapi.dto.GetListingsResponseDto;
import com.exercise.publicapi.service.PublicApiService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/public-api")
public class PublicApiController {

    @Autowired
    private PublicApiService publicApiService;

    @GetMapping("/listings")
    public GetListingsResponseDto getListings(
            @RequestParam(name = "pageNum", defaultValue = "1") @Min(1) Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(name = "userId", required = false) @Min(1) Integer userId
    ) {
        GetListingsRequestDto requestDto = new GetListingsRequestDto();
        requestDto.setPageNum(pageNum);
        requestDto.setPageSize(pageSize);
        requestDto.setUserId(userId);

        return publicApiService.getListings(requestDto);
    }

    @PostMapping("/users")
    public CreateUserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto requestDto) {
        return publicApiService.createUser(requestDto);
    }

    @PostMapping("/listings")
    public CreateListingResponseDto createListing(@Valid @RequestBody CreateListingRequestDto requestDto) {
        return publicApiService.createListing(requestDto);
    }
}
