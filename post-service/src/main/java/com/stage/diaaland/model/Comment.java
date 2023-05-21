package com.stage.diaaland.model;


import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {
    @Id
    private String id ;
    @NonNull
    private String commentDetails;
    @CreatedBy
    private String createdDate;
    @LastModifiedBy
    private String updatedDate;
    @NonNull
    private String username;
    @NonNull
    private String idPost;

    public Comment(String content) {
        this.commentDetails=content;
    }

}