package com.Lycle.Server.controller;

import com.Lycle.Server.config.auth.UserPrincipal;
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
