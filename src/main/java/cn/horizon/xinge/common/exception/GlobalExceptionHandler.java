package cn.horizon.xinge.common.exception;

import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.common.api.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author horizon
 * @create 2022-06-22 21:04
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 默认返回Error
     */
    private static final Result<Object> ERROR;

    static {
        ERROR = Result.failed(ResultCode.FAILED);
    }

    /**
     * 描述：默认异常提示
     * @param exception Exception 全部异常
     * @return 统一接口返回对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> defaultErrorHandler(Exception exception) {
        LOG.error(exception.getMessage(), exception);
        return ERROR;
    }

    /**
     * 描述：参数不合法默认异常提示
     * @param exception IllegalArgumentException
     * @return 统一接口返回对象
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> securityExceptionHandler(Exception exception) {
        return Result.failed(exception.getMessage());
    }

    /**
     * 描述：表单数据格式不正确异常提示
     * @param exception MethodArgumentNotValidException
     * @return 统一接口返回对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> illegalParamExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        if (!errors.isEmpty()) {
            List<String> list = errors.stream()
                    .map(error -> error.getField() + error.getDefaultMessage())
                    .collect(Collectors.toList());

            return Result.failed(ResultCode.BAD_REQUEST, list);
        } else {
            return Result.failed(ResultCode.BAD_REQUEST);
        }
    }

    /**
     * 描述：表单数据缺失异常提示
     * @param exception MissingServletRequestParameterException
     * @return 统一接口返回对象
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> servletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        return Result.failed(ResultCode.BAD_REQUEST);
    }

    /**
     * 描述：请求方法不支持异常提示
     * @param exception HttpRequestMethodNotSupportedException
     * @return 统一接口返回对象
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Object> methodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        String errorMsg = ResultCode.BAD_REQUEST.getMsg();
        if (!CollectionUtils.isEmpty(exception.getSupportedHttpMethods())) {
            String supportedMethods = exception.getSupportedHttpMethods().stream()
                    .map(HttpMethod::toString)
                    .collect(Collectors.joining("/"));
            errorMsg = "请求方法不合法,请使用方法" + supportedMethods;
        }

        return Result.failed(ResultCode.BAD_REQUEST, errorMsg);
    }

    /**
     * 描述：数据绑定失败异常提示
     * @param exception BindException
     * @return 统一接口返回对象
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> validationBindException(BindException exception) {
        String errors = exception.getFieldErrors().stream()
                .map(error -> error.getField() + error.getDefaultMessage())
                .collect(Collectors.joining(","));
        return Result.failed(ResultCode.BAD_REQUEST, errors);
    }

    /**
     * 自定义异常
     * @param exception CustomException
     * @return Result
     */
    @ExceptionHandler(CustomException.class)
    public Result<Object> customException(CustomException exception) {
        return Result.failed(exception.getCode(), exception.getMessage());
    }
    
}
