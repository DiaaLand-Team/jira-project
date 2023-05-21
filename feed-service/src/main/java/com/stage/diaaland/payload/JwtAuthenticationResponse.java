package com.stage.diaaland.payload;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType;
}
