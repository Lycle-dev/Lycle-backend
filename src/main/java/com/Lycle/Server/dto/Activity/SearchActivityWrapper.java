package com.Lycle.Server.dto.Activity;

public interface SearchActivityWrapper {
    Long getId();
    String getCreatedDate();
    String getCategory();
    String getActivityTime();
    boolean isFinishChecked();
    boolean isRewardChecked();
    boolean isRequestReward();
}
