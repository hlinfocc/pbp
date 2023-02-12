package net.hlinfo.pbp.usr.adaptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.json.Json;
import org.postgresql.util.PGobject;


import net.hlinfo.opt.Jackson;

public class JsonbToListListValueAdaptor implements ValueAdaptor{
	public List<List> get(ResultSet rs, String colName) throws SQLException {
		// TODO Auto-generated method stub
		PGobject obj = (PGobject) rs.getObject(colName);
		if(obj!=null && !"".equals(obj.getValue())) {
			List<List> list = Jackson.toList(obj.getValue(), List.class);
//			System.out.println(list);
			return list;
		}else {
			return new ArrayList<List>();
		}
	}
	
	public void set(java.sql.PreparedStatement stat, Object obj, int index) throws SQLException {
		if(obj!=null) {
			PGobject pgobject = new PGobject();
			pgobject.setType("jsonb");
			pgobject.setValue(Json.toJson(obj));
			stat.setObject(index, pgobject);
		}
	};
}
