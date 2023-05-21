package com.stage.diaaland.payload;


import lombok.Data;

@Data
public class FollowRequest {

    UserPayload follower;
    UserPayload following;
}
