package com.horbatenko.base64file.model;

//import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBody {
//    @ApiModelProperty(notes = "URL that returns encoded base64 file in field", example = "http://localhost:8080/", required = true)
    private String url;
//    @ApiModelProperty(notes = "Field name that will contain encoded file", example = "context", required = true)
    private String contextField;
//    @ApiModelProperty(notes = "File name after decoding. If not set name will generated automatically", example = "downloaded_file", required = false)
    private String fileName;
//    @ApiModelProperty(notes = "Field name that contains extension for file. If not set, extension will generated automatically", example = "extension", required = false)
    private String extensionField;
//    @ApiModelProperty(notes = "File extension. If not set, extension will generated automatically", example = "txt", required = false)
    private String extension;
//    @ApiModelProperty(notes = "Path where file will saved. If not set, path will set by default = USER_HOME", example = "txt", required = false)
    private String outputDir;
//    @ApiModelProperty(notes = "Flag is open saved file. Default false", required = false)
    private boolean open;
    private HttpMethod httpMethod = HttpMethod.GET;
    private JsonNode content;
}
