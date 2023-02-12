package net.hlinfo.pbp.usr.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;

import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import net.hlinfo.pbp.opt.Resp;




@RestControllerAdvice
public class CustomExceptionResolver {
	private static final Logger log = LoggerFactory.getLogger(CustomExceptionResolver.class);
	@Value("${spring.servlet.multipart.max-file-size:2M}")
	private String maxFileSize;
	
	@Value("${spring.servlet.multipart.max-request-size:100M}")
	private String maxRequestSize;
	/**
	 * 捕获全局不可知的异常
	 * @param e
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value=Exception.class)
	public Object handleException(Exception e, HttpServletRequest req) {
		log.error("全局异常处理...",e);
		return new Resp<>().error("服务器或网络异常").data(NutMap.NEW()
			.addv("url", req.getRequestURL())
			.addv("message", e.getMessage())
			.addv("stackTrace", e.getStackTrace()));
	}
	/**
	 * 捕获全局参数校验的异常
	 * @param e
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value= { MethodArgumentNotValidException.class 
			, UnexpectedTypeException.class})
	public Object handleException(MethodArgumentNotValidException e, HttpServletRequest req) {
		log.error("参数异常处理...",e);
		String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		List<NutMap> errorMaps = new ArrayList<>();
		for (ObjectError error : e.getBindingResult().getAllErrors()) {
			NutMap errorMap = NutMap.WRAP(Json.toJson(error));
			errorMap.remove("arguments");
			errorMap.remove("codes");
			errorMaps.add(errorMap);
		}
		e.printStackTrace();
		return new Resp<>().error(msg).data(errorMaps);
		
	}
	/**
	 * 捕获上传文件异常
	 * @author cy
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value=MultipartException.class)
	public Object fileUploadExceptionHandler(MultipartException e){
		log.error("上传文件失败，服务器异常:"+e.getMessage(),e);
		return new Resp<String>().error("上传文件失败，服务器异常");
	}
	
	/**
	 * 捕获上传文件大小超出限制异常
	 * @author cy
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value=MaxUploadSizeExceededException.class)
    public Object uploadException(MaxUploadSizeExceededException e) {
		log.error("上传文件大小超出限制,单个文件不能超过："+maxFileSize+"，多文件总大小不能超过："+maxRequestSize,e);
       return new Resp<String>().error("上传文件大小超出限制,单个文件不能超过："+maxFileSize+"，多文件总大小不能超过："+maxRequestSize);
    }
	
	/**
	 * 捕获未登录操作异常
	 * @param e
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value=NotLoginException.class)
	public Object handleException(NotLoginException e, HttpServletRequest req) {
		log.error("未登录:"+e.getMessage(),e);
		return Resp.NEW(Resp.NOT_LOGIN, "未登录", e.getMessage());
	}
	
	/**
	 * 捕获无权限操作异常
	 * @param e
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value=NotPermissionException.class)
	public Object handleException(NotPermissionException e, HttpServletRequest req) {
		log.error("无权限:"+e.getMessage(),e);
		return Resp.NEW(Resp.NOT_PERM, e.getMessage(), e.getMessage());
	}
	
	/**
	 * 捕获无角色操作异常
	 * @param e
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value=NotRoleException.class)
	public Object handleException(NotRoleException e, HttpServletRequest req) {
		log.error("无角色:"+e.getMessage(),e);
		return Resp.NEW(Resp.NOT_PERM, e.getMessage(), e.getMessage());
	}

}
