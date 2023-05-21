package com.stage.diaaland.client;

import com.stage.diaaland.model.User;
import com.stage.diaaland.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(serviceId = "GRAPH-SERVICE")
public interface GraphServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/paginated/{username}/followers")
    ResponseEntity<PagedResult<User>> findFollowers(
            @RequestHeader("Authorization") String token,
            @PathVariable String username,
            @RequestParam int page,
            @RequestParam int size);
}
