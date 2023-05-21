package com.stage.diaaland.service;

import com.stage.diaaland.client.PostServiceClient;
import com.stage.diaaland.config.JwtConfig;
import com.stage.diaaland.exception.UnableToGetPostsException;
import com.stage.diaaland.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {

    @Autowired private PostServiceClient postServiceClient;
    @Autowired private JwtConfig jwtConfig;

    public List<Post> findPostsIn(String token, List<String> ids) {
        log.info("finding posts for ids {}", ids);

        ResponseEntity<List<Post>> response =
                postServiceClient.findPostsByIdIn(jwtConfig.getPrefix() + token, ids);

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UnableToGetPostsException(
                    String.format("unable to get posts for ids", ids));
        }
    }
}
