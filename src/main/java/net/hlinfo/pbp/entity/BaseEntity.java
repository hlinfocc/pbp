package net.hlinfo.pbp.entity;

import java.io.Serializable;
import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import net.hlinfo.opt.Func;

public class BaseEntity implements Serializable{
	@ExcelIgnore
	private static final long serialVersionUID = 1L;
	
	@Name
	@Column("id")
	@ColDefine(notNull=false, type=ColType.VARCHAR, width=32)
	@Comment(value="主键ID")
	@ApiModelProperty("权限主键，添加的时候不用传")
	@ExcelIgnore
	private String id;
	
	/**
	* 创建时间
	*/
	@Column("create_time")
	@ColDefine(notNull=true, type=ColType.DATETIME, width=25)
	@Comment(value="创建时间")
	@ApiModelProperty(hidden = true)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelIgnore
	private Date createTime;
	/**
	* 上一次更新时间
	*/
	@Column("update_time")
	@ColDefine(notNull=true, type=ColType.DATETIME, width=25)
	@Comment(value="上一次更新时间")
	@ApiModelProperty(hidden = true)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelIgnore
	private Date updateTime;
	
	/**
	* 是否删除
	*/
	@Column("isdelete")
	@ColDefine(notNull=true, type=ColType.INT, width=2)
	@Comment(value="是否删除： 0没有删除 1 删除")
	@ApiModelProperty(hidden = true)
	@ExcelIgnore
	@Default("0")
	private int isdelete;
	/**
	 * 初始化
	 */
	public void init() {
		this.setId(Func.longuuid()+"");
		this.setCreateTime(new Date());
		this.setUpdateTime(new Date());
		this.setIsdelete(0);
	}
	
	/**
	 * 更新updatetime
	 */
	public void upgrade() {
		this.setUpdateTime(new Date());
	}
	/**
	 * 设置删除标识
	 */
	public void deleted() {
		this.setIsdelete(1);
		this.setUpdateTime(new Date());
	}
	
	/**
	 * 软删除
	 */
	public int deletedSoft(Dao dao) {
		deleted();
		Object data = updated(dao);
		return data != null ? 1 : 0;
	}
	
	/**
	 * 直接删除
	 * @param dao NutzDao实例
	 * @return 若更新成功,返回值大于0, 否则小于等于0
	 */
	public int deletedHard(Dao dao) {
		return dao.delete(getClass(), id);
	}
	/**
	 * 根据对象的主键(@Id/@Name/@Pk)先查询, 如果存在就更新, 不存在就插入
	 * @param dao NutzDao实例
	 * @return 原对象
	 */
	public Object insertOrUpdate(Dao dao) {
		if(Strings.isBlank(this.getId())) {
			init();
		}
		return dao.insertOrUpdate(this);
	}
	
	/**
	 * 根据对象的主键(@Id/@Name/@Pk)先查询, 如果存在就更新, 不存在就插入(带init())
	 * @param cls 对象类型
	 * @param dao NutzDao实例
	 * @return 原对象
	 */
	public <T extends BaseEntity> T insertOrUpdate(Class<T> cls, Dao dao) {
		if(Strings.isBlank(this.getId())) {
			init();
		}
		T obj = (T)this;
		return dao.insertOrUpdate(obj);
	}
	
	public Object insertOrUpdateIgnoreNull(Dao dao) {
		if(Strings.isNotBlank(id)) {
			Table table = this.getClass().getAnnotation(Table.class);
			String tableName = table.value();
			Record db = dao.fetch(tableName, Cnd.where("id", "=", getId()));
			if(db != null) {
				return updateIgnoreNull(dao) > 0 ? this : null;
			}else {
				return insert(dao);
			}
		}else {
			return insert(dao);
		}
	}
	
	/**
	 * 插入/更新， 通过id查询数据库判断，若数据库里没有则插入，否则更新
	 * 在插入操作的时候不会调用初始化init函数进行初始化
	 * @param dao
	 * @return
	 */
	public Object insertOrUpdateNoInit(Dao dao) {
		Table table = this.getClass().getAnnotation(Table.class);
		String tableName = table.value();
		Record db = dao.fetch(tableName, Cnd.where("id", "=", getId()));
		if(db != null) {
			return updateIgnoreNull(dao) > 0 ? this : null;
		}else {
			return insert(dao);
		}
	}

	
	public Object insert(Dao dao) {
		init();
		return dao.insert(this);
	}
	
	public boolean save(Dao dao) {
		init();
		Object obj= dao.insert(this);
		return obj!=null;
	}
	
	public void updated() {
		this.setUpdateTime(new Date());
	}
	
	public int updated(Dao dao) {
		this.setUpdateTime(new Date());
		return dao.update(this,"^(?!create_time)");
	}
	
	public int updateIgnoreNull(Dao dao) {
		this.setUpdateTime(new Date());
		return dao.updateIgnoreNull(this);
	}
		
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getIsdelete() {
		return isdelete;
	}



	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}

	public String toString() {
		return Json.toJson(this);
	}
}
