package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.service.ActivityService;
import com.Lycle.Server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final UserService userService;
    private final ActivityService activityService;

    @GetMapping("/friend/search")
    public ResponseEntity<BasicResponse> searchFriend(@RequestParam String nickname) {
        BasicResponse response;
        if (userService.verifyNickname(nickname)) {
            response = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("존재하는 사용자 입니다. 친구 맺기가 가능합니다.")
                    .build();
        } else {
            response = BasicResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("존재하지 않는 사용자 입니다.")
                    .build();
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }


    @PutMapping("/friend")
    public ResponseEntity<BasicResponse> addFriend(Authentication authentication, @RequestParam String nickname) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.addFriends(userPrincipal.getId(), nickname);
        BasicResponse basicResponse;
        basicResponse = BasicResponse.builder()
                .count(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("사용자와 친구 맺기가 완료 되었습니다.")
                .build();

        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }

    @PutMapping("/friend/del")
    public ResponseEntity<BasicResponse> deleteFriend(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.deleteFriend(userPrincipal.getId(), userPrincipal.getSharedId());
        BasicResponse basicResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("친구 삭제가 완료 되었습니다.")
                .build();
        return new ResponseEntity<>(basicResponse, basicResponse.getHttpStatus());
    }

    @GetMapping("/friend/profile")
    public ResponseEntity<BasicResponse> searchFriend(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long sharedId = userPrincipal.getSharedId();
        Map<String,Object> result = new HashMap<>();

        //Map 을 활용하여 profile 정보와 activity 조회 결과를 한 array 에 담아서 전송송
       result.put("profile", userService.searchProfile(sharedId));
        result.put("activities", activityService.searchActivity(sharedId));

        BasicResponse friendResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .count(2)
                .result(Collections.singletonList(result))
                .build();

        return new ResponseEntity<>(friendResponse, friendResponse.getHttpStatus());
    }

}
