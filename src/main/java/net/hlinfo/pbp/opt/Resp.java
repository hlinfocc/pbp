package net.hlinfo.pbp.opt;

import java.util.List;

import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;

import cn.hutool.core.util.PageUtil;
import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.QueryPages;

public class Resp<T> {
	public static int OK = 200; //操作成功
	public static int NOT_LOGIN = 401; //未登录
	public static int ERROR = 402; //操作失败
	public static int NOT_PERM = 403; //无权限
	public static int NOT_FOUND = 404; //资源不存在
	public static int NOT_DATA = 200; //查询的时候没有数据了
	public static int SERVER_ERROR = 500; //服务器内部错误
	
	@ApiModelProperty(value = "200:操作成功, 401:未登录, 402:操作失败, 403:无权限, 404:资源不存在, 405:查询(列表查询/ID查询等)的时无数据了, 500:服务器内部错误")
	private int code;
	
	@ApiModelProperty(value = "消息")
	private String msg;
	
	@ApiModelProperty(value = "数据")
	private T data;
	
	public Resp() {}
	
	public static<T> Resp<T> NEW() {
		return new Resp<T>();
	}
	public static<T> Resp<T> NEW(Class<T> cls) {
		return new Resp<T>();
	}
	public Resp(int code) {
		this.code = code;
	}
	public static <T> Resp<T> NEW(int code) {
		return new Resp<T>(code);
	}
	
	public Resp(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public static <T> Resp<T> NEW(int code, String msg) {
		return new Resp<T>(code, msg);
	}
	
	public static <T> Resp<T> NEW(int code, String msg, T obj) {
		return new Resp<T>(code, msg, obj);
	}
	
	public Resp(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static <T> Resp<T> OK(String msg) {
		Resp<T> resp = NEW(OK, msg);
		return resp;
	}
	
	public static <T> Resp<T> OK(String msg, T data) {
		Resp<T> resp = NEW(OK, msg, data);
		return resp;
	}

	public Resp<T> ok(String msg) {
		this.setCode(OK);
		this.setMsg(msg);
		return this;
	}
	/**
	 * 操作成功
	 * @return
	 */
	public static <T>  Resp<T> SUCCESS() {
		Resp<T> resp = NEW(OK, "操作成功");
		return resp;
	}
	
	public static <T>  Resp<T> SUCCESS(String msg) {
		Resp<T> resp = NEW(OK, msg);
		return resp;
	}
	
	public Resp<T> ok(String msg, T data) {
		this.setCode(OK);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	
	public static <T> Resp<T> ERROR(String msg) {
		Resp<T> resp = NEW(ERROR, msg);
		return resp;
	}
	
	public static <T> Resp<T> NO_DATA(String msg, T data) {
		Resp<T> resp = NEW(NOT_DATA, msg);
		resp.setData(data);
		return resp;
	}
	
	public static <T> Resp<T> NO_DATA(String msg) {
		Resp<T> resp = NEW(NOT_DATA, msg);
		return resp;
	}
	
	public static <T> Resp<T> ERROR(String msg, T data) {
		Resp<T> resp = NEW(ERROR, msg, data);
		return resp;
	}
	
	public Resp<T> error(String msg) {
		this.setCode(ERROR);
		this.setMsg(msg);
		return this;
	}
	/**
	 * 操作失败
	 * @return
	 */
	public static <T> Resp<T> FAIL() {
		return Resp.NO_DATA("操作失败");
	}
	public static <T> Resp<T> FAIL(String msg) {
		return Resp.NO_DATA(msg);
	}
	
	public Resp<T> error(String msg, T data) {
		this.setCode(ERROR);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	
	public Resp<T> code(int code) {
		this.setCode(code);
		return this;
	}
	public Resp<T> msg(String msg) {
		this.setMsg(msg);
		return this;
	}
	
	public Resp<T> data(T data) {
		this.setData(data);
		return this;
	}
	
	public static <T> Resp<List<T>> LIST(List<T> list, String okMsg, String errorMsg) {
		if(list != null && list.size() > 0) {
			return Resp.OK(okMsg, list);
		}else {
			return Resp.NO_DATA(errorMsg);
		}
	}
	
	public static <T> Resp<List<T>> LIST_O(List<T> list) {
		return LIST(list, "操作成功", "操作失败");
	}
	
	public static <T> Resp<List<T>> LIST_Q(List<T> list) {
		return LIST(list, "查询成功", "没有数据了");
	}
	
	public static <T> Resp<NutzPages<List<T>>> LIST_PAGE(List<T> list, Pager pager) {
		NutzPages<List<T>> result = new NutzPages(list, pager);
		if(list != null && list.size() > 0) {
			return Resp.OK("查询成功", result);
		}else {
			return Resp.NO_DATA("没有数据了", result);
		}
	}
	
	public static <T> Resp<T> OBJ_O(T obj) {
		boolean b = (
			obj instanceof Integer 
			&& obj != null 
			&& (Integer) obj > 0)
		|| (!(obj instanceof Integer) && obj != null);
		if(b) {
			return Resp.OK("操作成功", obj);
		}else {
			return Resp.ERROR("操作失败");
		}
	}
	
	public static <T> Resp<T> OBJ_Q(T obj) {
		if(obj != null) {
			return Resp.OK("查询成功", obj);
		}else {
			return Resp.NO_DATA("没有数据了");
		}
	}
}
