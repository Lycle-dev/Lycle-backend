package com.Lycle.Server.service;

import com.Lycle.Server.config.auth.token.JwtTokenProvider;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.UserRepository;
import com.Lycle.Server.dto.User.SearchProfileWrapper;
import com.Lycle.Server.dto.User.UpdateInfoDto;
import com.Lycle.Server.dto.User.UserJoinDto;
import com.Lycle.Server.dto.User.UserLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.Lycle.Server.domain.User.Role.USER;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RewardService rewardService;

    @Transactional
    public Long saveUser(UserJoinDto userJoinDto) throws JSONException, IOException {
        //체인 서버에 회원 정보 생성
        rewardService.registerCheck(userJoinDto.getEmail());
        //회원 가입 시 자동으로 1000 포인트 지급
        rewardService.depositPoint(userJoinDto.getEmail());
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
        User user = userRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
        if(updateInfoDto.getNickname() != null && updateInfoDto.getPassword() != null){
            user.updateNickname(updateInfoDto.getNickname());
            user.updatePassword(updateInfoDto.getPassword());
        }
        else if(updateInfoDto.getNickname() == null && updateInfoDto.getPassword() != null){
            user.updatePassword(updateInfoDto.getPassword());
        }else{
            user.updateNickname(updateInfoDto.getNickname());
        }
    }

    @Transactional
    public void updateTime(Long id) throws ParseException {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //가입 일자 포맷 변경
        String joinDate = user.getCreatedDate();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        Date formatJoin = formatter.parse(joinDate);

        //현재 시간 포맷 변경
         LocalDate now = LocalDate.now();
         DateTimeFormatter nowFormatter = DateTimeFormatter.ofPattern("yy/MM/dd");
         String formattedNow = now.format(nowFormatter);
         Date formatNow = formatter.parse(formattedNow);

         Integer diffDays = formatJoin.compareTo(formatNow);

         //날짜가 로그인 날 보다 뒤일때만 활동일자 증가
         if(diffDays > 0){
             user.updateTime(user.getTotalTime()+1);
         }

    }


    /*
    //친구와 리워드 주고 받기
    public boolean exchangeReward(String email, Long sharedId, int point) throws JSONException, IOException {
        //친구의 정보 찾기
        User friend = userRepository.findById(sharedId).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 회원 입니다."));

        //리워드 주기
        Long result = rewardService.transferReward(email, friend.getEmail(), point);
        if(result < 0L){
            return false;
        }
        return true;
    }

     */


}
