package com.Lycle.Server.dto.Activity;

public interface SearchActivityWrapper {
    String getCreatedDate();
    String getCategory();
    String getActivityTime();
    Boolean getFinishChecked();
    Boolean getRewardChecked();
    Boolean getRewardRequested();
}
