package com.stage.diaaland.repository;

import com.stage.diaaland.model.ImageMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageMetadataRepository extends MongoRepository<ImageMetadata, String> {

}
