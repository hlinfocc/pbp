package net.hlinfo.pbp.etc;

import javax.sql.DataSource;

import org.beetl.core.GroupTemplate;
import org.beetl.sql.core.ClasspathLoader;
import org.beetl.sql.core.ConnectionSource;
import org.beetl.sql.core.ConnectionSourceHelper;
import org.beetl.sql.core.Interceptor;
import org.beetl.sql.core.SQLLoader;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.PostgresStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

import net.hlinfo.pbp.opt.beetlsql.BlankFun;
import net.hlinfo.pbp.opt.beetlsql.Vo2PgsqlFieldFun;

@Configuration
public class DatabaseConfig {
	
	@Bean(destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
	
	@Bean(name = {"beetlSqlManager"})
	public SQLManager beetlSqlManager(DataSource druidDataSource) {
		DBStyle sqlStyle = new PostgresStyle();
		// sql语句放在classpagth的/sql 目录下
		SQLLoader loader = new ClasspathLoader("/sqls");
		// 数据库命名跟java命名一样，所以采用DefaultNameConversion，还有一个是UnderlinedNameConversion，下划线风格的
		UnderlinedNameConversion nc = new  UnderlinedNameConversion();
		// 最后，创建一个SQLManager,DebugInterceptor 不是必须的，但可以通过它查看sql执行情况
		ConnectionSource source = ConnectionSourceHelper.getSingle(druidDataSource);
		SQLManager sqlManager = new SQLManager(sqlStyle
				,loader
				,source
				,nc
				,new Interceptor[]{new DebugInterceptor()});
		GroupTemplate gt = sqlManager.getBeetl().getGroupTemplate();
		gt.registerFunction("vo2PgsqlField", new Vo2PgsqlFieldFun());
		gt.registerFunction("isBlank", new BlankFun());
		return sqlManager;
	}
}
