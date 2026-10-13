package com.exercise.publicapi.service.impl;

import com.exercise.publicapi.dto.CreateListingRequestDto;
import com.exercise.publicapi.dto.CreateListingResponseDto;
import com.exercise.publicapi.dto.CreateUserRequestDto;
import com.exercise.publicapi.dto.CreateUserResponseDto;
import com.exercise.publicapi.dto.GetListingsRequestDto;
import com.exercise.publicapi.dto.GetListingsResponseDto;
import com.exercise.publicapi.dto.ListingDto;
import com.exercise.publicapi.dto.ListingServiceGetListingsResponseDto;
import com.exercise.publicapi.dto.ListingWithUserDto;
import com.exercise.publicapi.dto.UserServiceGetUserResponseDto;
import com.exercise.publicapi.service.PublicApiService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicApiServiceImpl implements PublicApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.listing.url:http://localhost:8081}")
    private String listingServiceUrl;

    @Value("${services.user.url:http://localhost:8082}")
    private String userServiceUrl;

    @Override
    public GetListingsResponseDto getListings(GetListingsRequestDto requestDto) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(listingServiceUrl + "/listings")
                .queryParam("pageNum", requestDto.getPageNum())
                .queryParam("pageSize", requestDto.getPageSize());

        if (requestDto.getUserId() != null) {
            uriBuilder.queryParam("userId", requestDto.getUserId());
        }

        ListingServiceGetListingsResponseDto listingResponse = restTemplate.getForObject(
                uriBuilder.toUriString(),
                ListingServiceGetListingsResponseDto.class
        );

        List<ListingDto> listings = (listingResponse != null && listingResponse.getListings() != null)
                ? listingResponse.getListings()
                : Collections.emptyList();

        List<ListingWithUserDto> listingWithUserDtoList = listings.stream()
                .map(this::convertToListingWithUserDto)
                .collect(Collectors.toList());

        GetListingsResponseDto responseDto = new GetListingsResponseDto();
        responseDto.setResult(true);
        responseDto.setListings(listingWithUserDtoList);

        return responseDto;
    }

    @Override
    public CreateUserResponseDto createUser(CreateUserRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", requestDto.getName());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        return restTemplate.postForObject(
                userServiceUrl + "/users",
                entity,
                CreateUserResponseDto.class
        );
    }

    @Override
    public CreateListingResponseDto createListing(CreateListingRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("userId", String.valueOf(requestDto.getUserId()));
        formData.add("listingType", requestDto.getListingType());
        formData.add("price", String.valueOf(requestDto.getPrice()));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        return restTemplate.postForObject(
                listingServiceUrl + "/listings",
                entity,
                CreateListingResponseDto.class
        );
    }

    private ListingWithUserDto convertToListingWithUserDto(ListingDto listingDto) {
        ListingWithUserDto listingWithUserDto = new ListingWithUserDto();
        BeanUtils.copyProperties(listingDto, listingWithUserDto);

        if (listingDto.getUserId() != null) {
            UserServiceGetUserResponseDto userResponse = restTemplate.getForObject(
                    userServiceUrl + "/users/" + listingDto.getUserId(),
                    UserServiceGetUserResponseDto.class
            );
            if (userResponse != null) {
                listingWithUserDto.setUser(userResponse.getUser());
            }
        }

        return listingWithUserDto;
    }
}
