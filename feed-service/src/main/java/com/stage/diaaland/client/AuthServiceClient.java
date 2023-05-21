package com.stage.diaaland.client;

import com.stage.diaaland.payload.JwtAuthenticationResponse;
import com.stage.diaaland.payload.ServiceLoginRequest;
import com.stage.diaaland.payload.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(serviceId = "AUTH-SERVICE")
public interface AuthServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "signin")
    ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody ServiceLoginRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/users/summary/in")
    ResponseEntity<List<UserSummary>> findByUsernameIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usernames);
}
