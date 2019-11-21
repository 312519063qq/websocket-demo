package com.example.websocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class JsonResult<T> {

    public static final String OK = "ok";
    private String status;
    private String msg;
    private T data;

    public static <T> JsonResult<T> ok(T data) {
        JsonResult<T> result = new JsonResult();
        result.setStatus("ok");
        result.setMsg("ok");
        result.setData(data);
        return result;
    }

    public static <T> JsonResult<T> ok() {
        return ok((T) null);
    }

    public static JsonResult fail(String status, String msg) {
        if (status.equals("ok")) {
            throw new RuntimeException("ok is not fail");
        } else {
            JsonResult result = new JsonResult();
            result.setStatus(status);
            result.setMsg(msg);
            return result;
        }
    }


    public static JsonResult badRequest(String msg) {
        return fail("bad_request", msg);
    }

    @JsonIgnore
    public boolean isOk() {
        return "ok".equals(this.status);
    }
}
