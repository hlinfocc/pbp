package net.hlinfo.pbp.usr.adaptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.nutz.lang.Strings;

public class SetStringAdaptor  implements ValueAdaptor{
	public Set<String> get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		 String obj = rs.getString(colName);
		 if(Strings.isNotBlank(obj)) {
			 Set<String> set = new HashSet<String>();
			 List<String> list = Json.fromJsonAsList(String.class, obj);
			 set.addAll(list);
			 return set;
		 }else {
			 return null;
		 }
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		if (null == obj) {
			stat.setNull(index, Types.NULL);
	    } else {
            stat.setString(index, Json.toJson(obj));
	    }
	}
}