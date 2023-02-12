package net.hlinfo.pbp.usr.adaptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.postgresql.util.PGobject;

import com.fasterxml.jackson.databind.JsonNode;

import net.hlinfo.opt.Jackson;

public class JacksonArrayValueAdaptor implements ValueAdaptor{
	public JsonNode get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		PGobject obj = (PGobject) rs.getObject(colName);
		if(obj!=null && !"".equals(obj.getValue())) {
			return Jackson.toJsonObject(obj.getValue());
		}else {
			return Jackson.arrayNode();
		}
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		if(obj!=null) {
			PGobject pgobject = new PGobject();
			pgobject.setType("json");
			pgobject.setValue(Json.toJson(obj));
			stat.setObject(index, pgobject);
		}
	};
}
