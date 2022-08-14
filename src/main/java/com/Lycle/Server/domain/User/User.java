package com.Lycle.Server.domain.User;

import com.Lycle.Server.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;;

@NoArgsConstructor
@Entity
@Getter

public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 300)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = true)
    private Long sharedId;

    @Column(nullable = true)
    private Long totalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder
    public User(Long id, String email,String password, String nickname, Long sharedId, Long totalTime, Role role){
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

    public void updateInfo(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
    }

    public void updateFriend(Long sharedId){
        this.sharedId = sharedId;
    }

    public void updateTime(Long activityTime){
        this.totalTime = totalTime;
    }

}
