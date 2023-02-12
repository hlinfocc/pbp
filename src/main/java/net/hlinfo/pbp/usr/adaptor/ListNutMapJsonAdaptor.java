package net.hlinfo.pbp.usr.adaptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.postgresql.util.PGobject;

public class ListNutMapJsonAdaptor implements ValueAdaptor{
	
	public List<NutMap> get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		 PGobject obj = (PGobject) rs.getObject(colName);
		 try {
			 if(obj != null && Strings.isNotBlank(obj.getValue())) {
				 System.out.println("list=" + obj.getValue());
				 List<NutMap> list = Json.fromJsonAsList(NutMap.class, obj.getValue());
				 return list;
			 }else {
				 return null;
			 }
		 }catch(Exception e) {
			 return null;
		 }
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		if (null == obj) {
			stat.setNull(index, Types.NULL);
	    } else {
	    	PGobject object = new PGobject();
	    	object.setType("json");
	    	object.setValue(Json.toJson(obj));
	    	
	    	stat.setObject(index, object);
	    }
	}
}