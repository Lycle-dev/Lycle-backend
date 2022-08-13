package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.dto.BasicResponse;
import com.Lycle.Server.dto.User.UpdateInfoDto;
import com.Lycle.Server.dto.User.UserJoinDto;
import com.Lycle.Server.dto.User.UserLoginDto;
import com.Lycle.Server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<BasicResponse> joinUser(@RequestBody UserJoinDto userJoinDto) {
        BasicResponse joinUserResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.CREATED)
                .message("회원가입이 완료되었습니다.")
                .count(1)
                .result(Collections.singletonList(userService.saveUser(userJoinDto)))
                .build();
        return new ResponseEntity<>(joinUserResponse, joinUserResponse.getHttpStatus());

    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponse> loginUser(@RequestBody UserLoginDto userLoginDto) {

        BasicResponse searchUser = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("로그인에 성공했습니다.")
                .count(1)
                .token(userService.loginUser(userLoginDto))
                .build();
        return new ResponseEntity<>(searchUser, searchUser.getHttpStatus());
    }

    @GetMapping("/verify/email")
    public ResponseEntity<BasicResponse> verifyEmail(@RequestParam String email) {
        BasicResponse verifyResponse;

        if (userService.verifyEmail(email)) {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 이메일 입니다.")
                    .build();

        } else {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용이 가능한 이메일 입니다.")
                    .build();
        }

        return new ResponseEntity<>(verifyResponse, verifyResponse.getHttpStatus());
    }

    @GetMapping("/verify/nickname")
    public ResponseEntity<BasicResponse> verifyNickname(@RequestParam String nickname) {
        BasicResponse verifyResponse;
        if (userService.verifyNickname(nickname)) {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 닉네임 입니다.")
                    .build();

        } else {
            verifyResponse = BasicResponse.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용이 가능한 닉네임 입니다.")
                    .build();
        }

        return new ResponseEntity<>(verifyResponse, verifyResponse.getHttpStatus());
    }

    @GetMapping("/user/friend")
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


    @PutMapping("/user/friend")
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

    @GetMapping("/user/profile")
    public ResponseEntity<BasicResponse> searchProfile(Authentication authentication) {
        BasicResponse profileResponse;
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        profileResponse = BasicResponse.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("회원 정보 조회가 완료되었습니다.")
                .count(1)
                .result(Collections.singletonList(userService.searchProfile(userPrincipal.getId())))
                .build();
        return new ResponseEntity<>(profileResponse, profileResponse.getHttpStatus());
    }


    @PutMapping("/user/profile")
    public ResponseEntity<BasicResponse> updateInfo(Authentication authentication, UpdateInfoDto updateInfoDto){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.updateInfo(userPrincipal.getId(), updateInfoDto);
        BasicResponse updateResponse = BasicResponse.builder()
                .code(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .message("회원정보 수정이 완료되었습니다.")
                .build();
        return new ResponseEntity<>(updateResponse, updateResponse.getHttpStatus());
    }
}
