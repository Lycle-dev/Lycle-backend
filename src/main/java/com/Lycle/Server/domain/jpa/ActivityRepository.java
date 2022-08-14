package com.Lycle.Server.domain.jpa;

import com.Lycle.Server.domain.Activity;
import com.Lycle.Server.dto.Activity.SearchActivityWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long>{
    @Query(value = "SELECT a.created_date createdDate, a.category, a.activity_time activityTime,a.finish_checked finishChecked " +
            "FROM activity a WHERE a.user_id=:userId order by a.created_date desc", nativeQuery = true)
    List<SearchActivityWrapper> findAllByUserIdOrderByCreatedDateDesc(Long userId);
}
