package com.horbatenko.base64file.model;

//import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespException {
//    @ApiModelProperty(notes = "Exception message")
    private String exception;
}
