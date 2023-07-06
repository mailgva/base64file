package com.horbatenko.base64file.model;

//import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBody {
//    @ApiModelProperty(notes = "File path", example = "~/Downloads/my_file.txt")
    private String path;
}
