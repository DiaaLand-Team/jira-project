package com.stage.diaaland.repository;


import com.stage.diaaland.model.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

     Comment findCommentById(String id);


}
