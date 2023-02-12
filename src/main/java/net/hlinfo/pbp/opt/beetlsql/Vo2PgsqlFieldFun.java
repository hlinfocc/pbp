package net.hlinfo.pbp.opt.beetlsql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.lang.Strings;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.hlinfo.annotation.MColmnIgnore;
import net.hlinfo.opt.Func;

public class Vo2PgsqlFieldFun  implements Function{

	@Override
	public Object call(Object[] paras, Context ctx) {
		// TODO Auto-generated method stub
		String str = "*";
		try {
			if(paras.length == 3) {
				str = this.vo2PgsqlField(Class.forName(paras[0] + "")
						, paras[1] + ""
						, paras[2] + ""
						,true);
			}else if(paras.length == 5) {
				str = this.vo2PgsqlField(Class.forName(paras[0] + "")
						, paras[1] + ""
						, paras[2] + ""
						, paras[3] + ""
						, paras[4] + ""
						,true);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体class
	 * @param prex 表名或表的别名
	 * @param fieldPrex 字段前缀
	 * @param isExtends 是否有继承
	 * @return
	 */
	private String vo2PgsqlField(Class<?> cls
			, String prex
			, String fieldPrex,boolean isExtends) {
		prex = Func.isBlank(prex)?"":prex+".";
		fieldPrex = Func.isBlank(fieldPrex)?"":fieldPrex;
		
		List<Field> fs = new ArrayList<>();
		if(isExtends) {
			do {
				Field[] pfs = cls.getDeclaredFields();
				fs.addAll(Arrays.asList(pfs));
					cls = cls.getSuperclass();
			}while(!Strings.equals(cls.getName(), Object.class.getName()));
		}else {
			Field[] pfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(pfs));
		}
		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			JsonIgnore jsonIgnore = fs.get(i).getAnnotation(JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			
			MColmnIgnore fsi = fs.get(i).getAnnotation(MColmnIgnore.class);
//			System.out.println("fsi=" + fsi);
			if(fsi != null) {
				continue;
			}
			Column cd = fs.get(i).getDeclaredAnnotation(Column.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("to_char("+prex+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(prex+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
					}
					index ++;
				}
			}
		}
//		System.out.println("SB2=" + sb.toString());
		return sb.toString();
	}
	
	
	/**
	 * 将实体对象的属性转为Pgsql查询语句的select 和from中的字段
	 * prex.user_name as fieldPrexuserName
	 * @param cls 实体class
	 * @param alias 表的别名或表名
	 * @param fieldPrex 字段前缀
	 * @param filterField 需要过滤的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
	 * @param allowField 只允许的字段，格式为pojo实体名,多个用竖线|分隔，如：userName|userPasswd
	 * @param isExtends 是否存在继承的类
	 * @return
	 */
	private String vo2PgsqlField(Class<?> cls
			, String alias
			, String fieldPrex
			,String filterField
			,String allowField
			,boolean isExtends) {
		alias = Func.isBlank(alias) ? "" : alias+".";
		fieldPrex = Func.isBlank(fieldPrex)?"":fieldPrex;
		List<Field> fs = new ArrayList<>();
		
		if(isExtends) {
			do {
				Field[] dfs = cls.getDeclaredFields();
				fs.addAll(Arrays.asList(dfs));
					cls = cls.getSuperclass();
			}while(Func.notequals(cls.getName(), Object.class.getName()));
		}else {
			Field[] dfs = cls.getDeclaredFields();
			fs.addAll(Arrays.asList(dfs));
		}
		
		if(fs.size() <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int i = 0; i < fs.size(); i ++) {
			JsonIgnore jsonIgnore = fs.get(i).getAnnotation(JsonIgnore.class);
			if(jsonIgnore != null) {
				continue;
			}
			MColmnIgnore fsi = fs.get(i).getAnnotation(MColmnIgnore.class);
			if(fsi != null) {
				continue;
			}
			
			if(Func.isNotBlank(filterField)) {
				String[] filterFields = filterField.split("\\|");
				if(Arrays.asList(filterFields).contains(fs.get(i).getName())) {
					continue;
				}
			}
			if(Func.isNotBlank(allowField)) {
				String[] allowFields = allowField.split("\\|");
				if(!Arrays.asList(allowFields).contains(fs.get(i).getName())) {
					continue;
				}
			}
			Column cd = fs.get(i).getDeclaredAnnotation(Column.class);
			String fieldType = fs.get(i).getType().toString();
			if(cd != null) {
				if(Func.isNotBlank(cd.value())) {
					if(index > 0) {
						sb.append(",");
					}
					if(fieldType.endsWith("Date")) {
						sb.append("to_char("+alias+cd.value()+",'yyyy-MM-DD HH24:MI:SS') as " + fieldPrex + fs.get(i).getName());
					}else {
						sb.append(alias+cd.value() + " as \"" + fieldPrex + fs.get(i).getName()+"\"");
					}
					index ++;
				}
			}
		}
//		System.out.println("SB: " + sb.toString());
		return sb.toString();
	}
}
