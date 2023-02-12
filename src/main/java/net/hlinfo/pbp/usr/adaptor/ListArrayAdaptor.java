package net.hlinfo.pbp.usr.adaptor;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nutz.dao.impl.jdbc.psql.PsqlArrayAdaptor;
import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.postgresql.jdbc.PgArray;

import cn.hutool.core.util.ArrayUtil;

public class ListArrayAdaptor implements ValueAdaptor{
	
	public List<String> get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		 Array array = rs.getArray(colName);
		 if(array != null) {
			 String[] objs = (String[]) array.getArray();
			 List<String> list = new ArrayList<String>();
			 for(int i = 0; i < objs.length; i ++) {
				 list.add(String.valueOf(objs[i]));
			 }
			 return list;
		 }else {
			 return null;
		 }
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		if (null == obj) {
			stat.setNull(index, Types.NULL);
	    } else {
	    	String[] objs = Json.fromJsonAsArray(String.class, Json.toJson(obj));
	    	Array a = stat.getConnection().createArrayOf("text", objs);
	    	stat.setArray(index, a);
	    }
	}
}