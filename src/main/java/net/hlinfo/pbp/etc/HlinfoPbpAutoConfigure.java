package net.hlinfo.pbp.etc;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import net.hlinfo.utils.spring.configure.HlinfoMybatisDaoAutoConfiguration;

/**
 * PBP自动配置类
 * @author hlinfo
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnWebApplication
@ComponentScans(value= {
	@ComponentScan(basePackages = {"net.hlinfo.pbp"})
})
@AutoConfigureAfter(value = {MybatisAutoConfiguration.class})
@AutoConfigureBefore(value = {HlinfoMybatisDaoAutoConfiguration.class})
public class HlinfoPbpAutoConfigure {
	
}
