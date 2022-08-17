package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.Activity;
import com.Lycle.Server.dto.Activity.SearchActivityWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
    @Query(value = "SELECT a.id, a.created_date createdDate, a.category, a.activity_time activityTime,a.finish_checked finishChecked, " +
            "a.request_reward requestReward, a.reward_checked rewardChecked from activity a " +
            "WHERE a.user_id=:userId order by a.created_date desc", nativeQuery = true)
    List<SearchActivityWrapper> findAllByUserIdOrderByCreatedDateDesc(Long userId);

    //요청하지 않은 리워드 있을 때
    @Query(value = "select count(a.id) from activity a where a.request_reward=0" , nativeQuery = true)
    Long findActivityByUserId(Long id);

    //친구가 적립받지 못한 리워드가 있을 때
    @Query(value = "select count(a.id) from activity a where a.request_reward=1 and a.reward_checked=0", nativeQuery = true)
    Long findActivityById(Long id);
}
