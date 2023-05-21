package com.stage.diaaland.service;
import com.stage.diaaland.exception.NotAllowedException;
import com.stage.diaaland.exception.ResourceNotFoundException;
import com.stage.diaaland.model.Comment;;
import com.stage.diaaland.payload.CommentRequest;
import com.stage.diaaland.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    //create comment
    public Comment createComment(CommentRequest CommentRequest,String postId) {

        Comment comment = new Comment(CommentRequest.getContent());
        comment.setCreatedDate(new Date().toString());
        comment.setUpdatedDate("");
        comment.setIdPost(postId);

        comment = commentRepository.save(comment);

        return comment;
    }


    //delete comment
    public void deleteComment(String postId, String commentId) {

        commentRepository
                .findById(commentId)
                .map(com -> {
                    if(!com.getIdPost().equals(postId)) {
                        throw new NotAllowedException(postId, "com id " + commentId, "delete");
                    }

                    commentRepository.delete(com);
                    return com;
                })
                .orElseThrow(() -> {
                    return new ResourceNotFoundException(commentId);
                });
    }

    public List<Comment> findCommentByPostId(String id){
        List<Comment> comments= new ArrayList<Comment>();
        List<Comment> all=commentRepository.findAll();
        for(int i=0;i<all.size();i++){
            if(all.get(i).getIdPost().equals(id)){
                comments.add(all.get(i));
            }
        }

        return comments;
    }

    public Comment findCommentById(String id){

      return  commentRepository.findCommentById(id);

    }





}
