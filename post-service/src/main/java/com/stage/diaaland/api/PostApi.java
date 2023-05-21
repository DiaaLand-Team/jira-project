package com.stage.diaaland.api;


import com.stage.diaaland.model.Comment;
import com.stage.diaaland.model.Post;
import com.stage.diaaland.payload.ApiResponse;
import com.stage.diaaland.payload.CommentRequest;
import com.stage.diaaland.payload.PostRequest;
import com.stage.diaaland.service.CommentService;
import com.stage.diaaland.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
public class PostApi {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest){
        log.info("received a request to create a post for image {}", postRequest.getImageUrl());

        Post post = postService.createPost(postRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/posts/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "Post created successfully"));
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user) {
        log.info("received a delete request for post id {} from user {}", id, user.getName());
        postService.deletePost(id, user.getName());
        List<Comment> comments= commentService.findCommentByPostId(id);
        for(int i=0;i<comments.size();i++){
            commentService.deleteComment(id,comments.get(i).getId());

        }
    }

    @GetMapping("/posts/me")
    public ResponseEntity<?> findCurrentUserPosts(@AuthenticationPrincipal Principal principal) {
        log.info("retrieving posts for user {}", principal.getName());

        List<Post> posts = postService.postsByUsername(principal.getName());
        log.info("found {} posts for user {}", posts.size(), principal.getName());

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{username}")
    public ResponseEntity<?> findUserPosts(@PathVariable("username") String username) {
        log.info("retrieving posts for user {}", username);

        List<Post> posts = postService.postsByUsername(username);
        log.info("found {} posts for user {}", posts.size(), username);

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts/in")
    public ResponseEntity<?> findPostsByIdIn(@RequestBody List<String> ids) {
        log.info("retrieving posts for {} ids", ids.size());

        List<Post> posts = postService.postsByIdIn(ids);
        log.info("found {} posts", posts.size());

        return ResponseEntity.ok(posts);
    }
   //get comment from the post
    @GetMapping("/posts/{id}/comments/")
    public ResponseEntity<?> getCommentFromPostId(@PathVariable("id") String id) {
       List<Comment> comments=commentService.findCommentByPostId(id);
       for(int i=0;i<comments.size();i++){
           System.out.println(comments.get(i));
       }
       System.out.println(id);
       return ResponseEntity.ok(comments);

    }
    //add comment to the post
    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> CreateCommentToPost(@RequestBody CommentRequest commentRequest, @PathVariable("id") String id) {

       Comment comment= commentService.createComment(commentRequest,id);
        Map<String, Object> pathVariableMap = new HashMap<>();
        pathVariableMap.put("id",id);
        pathVariableMap.put("commentId",comment.getId());
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/posts/{id}/comments/{commentId}")
                .buildAndExpand(pathVariableMap).toUri();

        return ResponseEntity
                .created(location).body(new ApiResponse(true, "Comment created successfully"));

    }
    //delete comment
    @DeleteMapping("/posts/{id}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment( @AuthenticationPrincipal Principal user,@PathVariable("commentId") String commentId,@PathVariable("id") String id) {
        log.info("received a delete request for comment id {} from user {}", commentId, user.getName());
        commentService.deleteComment(id,commentId);
    }
    @PutMapping(path = ("/posts/{id}/comments/{commentId}"))
    public ResponseEntity<?> updateComment(@PathVariable String commentId, @RequestBody Comment comment ) {
        Comment commentUpdated=commentService.findCommentById(commentId);
        commentUpdated.setCommentDetails(comment.getCommentDetails());
        commentUpdated.setUpdatedDate(new Date().toString());
        return
                 ResponseEntity.ok(commentUpdated);

    }

}







