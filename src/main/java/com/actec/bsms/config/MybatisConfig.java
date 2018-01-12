package com.actec.bsms.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Mybatis配置
 *
 * @author zhangst
 * @create 2017-12-04 10:53 AM
 */
@Configuration // @Configuration注解（该注解类似于spring的配置文件）
@EnableTransactionManagement
@MapperScan(basePackages={"com.actec.bsms.repository.dao"})
public class MybatisConfig implements EnvironmentAware, TransactionManagementConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

    @Autowired
    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

    /**
     * 创建数据源
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     */
    @Bean
    public DataSource getDataSource() throws Exception{
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("spring.datasource.driverClassName"));
        props.put("url", env.getProperty("spring.datasource.url"));
        props.put("username", env.getProperty("spring.datasource.username"));
        props.put("password", env.getProperty("spring.datasource.password"));
        props.put("initialSize", env.getProperty("spring.datasource.initialSize"));
        props.put("minIdle", env.getProperty("spring.datasource.minIdle"));
        props.put("maxActive", env.getProperty("spring.datasource.maxActive"));
        props.put("maxWait", env.getProperty("spring.datasource.maxWait"));
        props.put("timeBetweenEvictionRunsMillis", env.getProperty("spring.datasource.timeBetweenEvictionRunsMillis"));
        props.put("minEvictableIdleTimeMillis", env.getProperty("spring.datasource.minEvictableIdleTimeMillis"));
        props.put("validationQuery", env.getProperty("spring.datasource.validationQuery"));
        props.put("testOnBorrow", env.getProperty("spring.datasource.testOnBorrow"));
        props.put("testOnReturn", env.getProperty("spring.datasource.testOnReturn"));
        props.put("testWhileIdle", env.getProperty("spring.datasource.testWhileIdle"));
        props.put("poolPreparedStatements", env.getProperty("spring.datasource.poolPreparedStatements"));
        props.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty("spring.datasource.maxPoolPreparedStatementPerConnectionSize"));
        props.put("filters", env.getProperty("spring.datasource.filters"));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(props);
        return dataSource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", env.getProperty("spring.datasource.username"));
        reg.addInitParameter("loginPassword", env.getProperty("spring.datasource.password"));
        reg.addInitParameter("logSlowSql", env.getProperty("spring.datasource.connectionProperties"));
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(getDataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(env.getProperty("mybatis.mapperLocations")));
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        try {
            return new DataSourceTransactionManager(getDataSource());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
