package net.hlinfo.pbp.usr.auth;

import java.lang.reflect.Field;

/**
 * 用户权限类型
 * @author hlinfo
 *
 */
public class AuthType {
	public final static String getPermName(int type) {
		return getName(type, "PERM");
	}
	public final static String getRoleName(int type) {
		return getName(type, "ROLE");
	}
	public final static String getName(int type, String name) {
		Class[] clss = AuthType.class.getDeclaredClasses();
		for(Class cls : clss) {
			try {
				Field field = cls.getField("TYPE");
				int t = field.getInt(null);
				if(type == t) {
					Field fieldPermName = cls.getField(name);
					return (String)fieldPermName.get(null);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		throw new RuntimeException("【" + type + "】没有对应的类型");
	}
	
	/**
	 * 后台超级管理员
	 *
	 */
	public class Root {
		public final static String PERM = "PbpRootPerm";
		public final static String ROLE = "PbpRootRole";
		public final static int TYPE = 0;
	}
	/**
	 * 后台管理员
	 *
	 */
	public class Admin {
		public final static String PERM = "PbpAdminPerm";
		public final static String ROLE = "PbpAdminRole";
		public final static int TYPE = 1000;
	}
	
	/**
	 * 普通用户
	 * 
	 */
	public class Member {
		public final static String PERM = "PbpMemberPerm";
		public final static String ROLE = "PbpMemberRole";
		public final static int TYPE = 1001;
	}
	
	/**
	 * 商户用户
	 * 
	 */
	public class Merchant {
		public final static String PERM = "PbpMemberPerm";
		public final static String ROLE = "PbpMemberRole";
		public final static int TYPE = 1002;
	}
	
	/**
	 * 教师用户
	 * 
	 */
	public class Teacher {
		public final static String PERM = "PbpTeacherPerm";
		public final static String ROLE = "PbpTeacherRole";
		public final static int TYPE = 1003;
	}
	
	/**
	 * 学生用户
	 * 
	 */
	public class Student {
		public final static String PERM = "PbpStudentPerm";
		public final static String ROLE = "PbpStudentRole";
		public final static int TYPE = 1004;
	}
	/**
	 * 其他
	 * 
	 */
	public class Other {
		public final static String PERM = "PbpOtherPerm";
		public final static String ROLE = "PbpOtherRole";
		public final static int TYPE = 1005;
	}
}
