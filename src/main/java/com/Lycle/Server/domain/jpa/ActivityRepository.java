package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.Activity;
import com.Lycle.Server.dto.Activity.SearchActivityWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
    @Query(value = "SELECT a.id, a.created_date createdDate, a.category, a.activity_time activityTime,a.finish_checked finishChecked, " +
            "a.request_reward requestReward, a.reward_checked rewardChecked from activity a " +
            "WHERE a.user_id=:userId order by a.created_date desc", nativeQuery = true)
    List<SearchActivityWrapper> findAllByUserIdOrderByCreatedDateDesc(Long userId);

    //요청하지 않은 리워드 있을 때
    @Query(value = "select count(a.id) from activity a where a.request_reward=0 and a.user_id=:id and a.created_date=:activityTime" , nativeQuery = true)
    Long findActivityByRequestRewardAndUserId(Long id, String activityTime);

    //오늘의 챌린지 완료 여부
    @Query(value = "select count(a.id) from activity a " +
            "where a.user_id=:userId and a.finish_checked=1 and a.created_date=:activityTime", nativeQuery = true)
    Long findActivityByActivityTime(Long userId,String activityTime);

    //친구가 적립받지 못한 리워드가 있을 때
    @Query(value = "select count(a.id) from activity a where a.request_reward=1 and a.reward_checked=0 and a.user_id=:id", nativeQuery = true)
    Integer findActivityById(Long id);

    //일자별로 요청된 리워드가 있는지 조회
    @Query(value = "select count(a.id) from activity a " +
            "where a.user_id=:userId and a.request_reward=1 and a.created_date=:activityTime", nativeQuery = true)
    Long findActivityByActivityTimeAndRequestReward(Long userId ,String activityTime);

    Optional<Activity> findActivityByUserId(Long userId);
}
