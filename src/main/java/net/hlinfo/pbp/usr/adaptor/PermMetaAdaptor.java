package net.hlinfo.pbp.usr.adaptor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;

import net.hlinfo.pbp.opt.vo.PermMeta;

public class PermMetaAdaptor  implements ValueAdaptor{
	
	public PermMeta get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		try {
			String obj = rs.getString(colName);
			return Json.fromJson(PermMeta.class, obj);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		stat.setObject(index, Json.toJson(obj));
	};

}