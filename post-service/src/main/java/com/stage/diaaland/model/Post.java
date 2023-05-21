package com.stage.diaaland.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@RequiredArgsConstructor
@Document
public class Post {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String username;

    @LastModifiedBy
    private String lastModifiedBy;

    @NonNull
    private String imageUrl; //for now post will contain only one image

    @NonNull
    private String caption;

   // @DBRef
    //private List<Comment> comments;
}
