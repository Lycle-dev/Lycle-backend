package com.Lycle.Server.domain.User;

import com.Lycle.Server.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.bind.DefaultValue;


import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@DynamicInsert
@DynamicUpdate
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column(length = 20,nullable = false)
    private String nickname;

    private Long sharedId;

    @ColumnDefault("0")
    private Integer totalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(Long id, String email,String password, String nickname, Long sharedId, Integer totalTime, Role role){
        this.id = id;
        this.email =email;
        this.password = password;
        this.nickname = nickname;
        this.sharedId = sharedId;
        this.totalTime = totalTime;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updateFriend(Long sharedId){
        this.sharedId = sharedId;
    }

    public void updateTime(Integer totalTime){
        this.totalTime = totalTime;
    }

}
