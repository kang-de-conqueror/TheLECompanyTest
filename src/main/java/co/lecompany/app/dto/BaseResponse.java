package co.lecompany.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {
    private Integer status;
    private String message;
    private Object data;

    public BaseResponse() {
        this.status = -1;
        this.message = "Error";
    }

    public BaseResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public BaseResponse(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}