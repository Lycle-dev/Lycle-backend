package com.Lycle.Server.service;

import com.Lycle.Server.config.auth.token.JwtTokenProvider;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.UserRepository;
import com.Lycle.Server.dto.User.SearchProfileWrapper;
import com.Lycle.Server.dto.User.UpdateInfoDto;
import com.Lycle.Server.dto.User.UserJoinDto;
import com.Lycle.Server.dto.User.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static com.Lycle.Server.domain.User.Role.USER;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long saveUser(UserJoinDto userJoinDto) {
        //최초 가입시 일반 사용자로 설정
        return userRepository.save(User.builder()
                .email(userJoinDto.getEmail())
                .password(passwordEncoder.encode(userJoinDto.getPassword()))
                .nickname(userJoinDto.getNickname())
                .role(USER)
                .build()
        ).getId();
    }

    @Transactional
    public String loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(user);
    }


    @Transactional(readOnly = true)
    public boolean verifyEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean verifyNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public SearchProfileWrapper searchProfile(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 입니다."));
    }

    //친구 추가
    @Transactional
    public Long addFriends(Long id, String nickname) {

        User me = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        User friends = userRepository.findUserByNickname(nickname).orElseThrow(
                ()-> new IllegalArgumentException("친구 맺기가 불가능 합니다."));
        if (friends.getId() != null) {
            //현재 user 테이블 update
             me.updateFriend(friends.getId());
            //공유하고자 하는 user 테이블 업데이트
            friends.updateFriend(me.getId());
            return 0L;
        }
        return -1L;
    }

    //친구 삭제
    @Transactional
    public void deleteFriend(Long id, Long sharedId){
        User me = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원 입니다."));
        User friend = userRepository.findById(sharedId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
        //내 repository 에 친구 삭제
        me.updateFriend(null);
        //친구 repository 에 친구 삭제
        friend.updateFriend(null);
    }

    //회원정보 수정
    @Transactional
    public void updateInfo(Long id, UpdateInfoDto updateInfoDto) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new IllegalIdentifierException("존재하지 않는 회원 입니다."));
        user.updateInfo(updateInfoDto.getNickname(), updateInfoDto.getPassword());
    }

    @Transactional
    public void updateTime(Long id) throws ParseException {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //시간 포맷 변경 후 비교
        String joinDate = user.getCreatedDate();
        String loginDate = user.getModifiedDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        Date formatJoin = formatter.parse(joinDate);
        Date formatLogin = formatter.parse(loginDate);
        long diffDays = (formatLogin.getTime() - formatJoin.getTime()) / (24*60*60*1000); //일자수 차이

        user.updateTime(diffDays);
    }

}
