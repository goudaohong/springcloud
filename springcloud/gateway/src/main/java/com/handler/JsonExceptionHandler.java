package com.handler;


import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理
 *
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {
    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 获取异常属性
     * @param request
     * @param includeStackTrace
     * @return
     */
    protected Map<String,Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace){
        int code =500;
        Throwable error = super.getError(request);
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException){
            code=404;
        }
        return response(code,this.buildMessage(request,error));
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     * @param errorAttributes
     * @return
     */
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes){
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     * @param errorAttributes
     * @return
     */
    protected HttpStatus getHttpStatus(Map<String,Object> errorAttributes){
        int statusCode = (int) errorAttributes.get("code");
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 构建异常信息
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerRequest request,Throwable ex){
        StringBuilder message = new StringBuilder("Failed to handler request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex !=null){
            message.append(":");
            message.append(ex.getMessage());
        }
        return message.toString();
    }
    public static Map<String,Object> response(int status,String errorMessage){
        Map<String,Object> map =new HashMap<>();
        map.put("code",status);
        map.put("message",errorMessage);
        map.put("data",null);
        return map;
    }
}
