package com.stage.diaaland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {

    private String fileName;
    private String uri;
    private String fileType;
}
