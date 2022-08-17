package com.Lycle.Server.service;

import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.UserRepository;
import com.Lycle.Server.fabric.FabricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {
    private final FabricService fabricService;
    private final UserRepository userRepository;

    @Transactional
    public Long getReward(Long id) throws IOException, JSONException {
        Long currentReward;
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        //fabricService 호출 하여 byte [] 형태로 받아옴
        byte[] rewardInfo = fabricService.getReward(user.getEmail());

        //Json 에서 데이터 추출
        JSONObject reward = new JSONObject(new String(rewardInfo, StandardCharsets.UTF_8));

        if (reward.getString("StatusMessage").equals("success")) {
            JSONObject data = reward.getJSONObject("Data").getJSONObject("User");
            String point = data.getString("Point");

            //point 를 Long 타입으로 변환
            currentReward = Long.parseLong(point);

        } else {
            currentReward = -1L;

        }

        return currentReward;
    }

    @Transactional
    public boolean registerCheck(String email) throws IOException, JSONException {
        boolean registerChecked;

        //fabricService 호출 하여 byte [] 형태로 받아옴
        byte[] registerInfo = fabricService.registerUser(email);

        //Json 에서 데이터 추출
        JSONObject reward = new JSONObject(new String(registerInfo, StandardCharsets.UTF_8));

        registerChecked = reward.getString("StatusMessage").equals("success");

        return registerChecked;
    }

    @Transactional
    public Long transferReward(String userEmail, String friendEmail, int point) throws IOException, JSONException {
        Long exchangePoint;

        //fabricService 호출 하여 byte [] 형태로 받아옴
        byte[] transferInfo = fabricService.exchangeReward(userEmail, friendEmail, point);

        //Json 에서 데이터 추출
        JSONObject reward = new JSONObject(new String(transferInfo, StandardCharsets.UTF_8));

        if (reward.getString("StatusMessage").equals("success")) {
            JSONObject data = reward.getJSONObject("Data").getJSONObject("Sender");
            String payPoint= data.getString("Point");

            //point 를 Long 타입으로 변환
            exchangePoint = Long.parseLong(payPoint);

        } else {
            exchangePoint = -1L;

        }
        return exchangePoint;
    }
}
