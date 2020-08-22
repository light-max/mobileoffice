package com.lfq.mobileoffice.model.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 自定义返回结果
 *
 * @param <T> 数据类型
 */
@Getter
@ToString
@NoArgsConstructor
public class Response<T> {
    /**
     * 状态
     */
    private Integer status = 200;
    /**
     * 信息
     */
    private String message = "操作成功";
    /**
     * 数据
     */
    private T data;

    private Response(T data) {
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> error(String message) {
        Response<T> response = new Response<>();
        response.status = 400;
        response.message = message;
        return response;
    }

    public static <T> Response<T> error() {
        return error(null);
    }

    public static Response<PagerData> pager(Pager pager, Object data) {
        return success(new PagerData(pager, data));
    }

    public boolean isSuccess() {
        return 200 == status;
    }
}
