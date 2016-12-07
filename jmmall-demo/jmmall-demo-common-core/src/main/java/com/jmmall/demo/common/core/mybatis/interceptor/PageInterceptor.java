package com.jmmall.demo.common.core.mybatis.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.jmmall.demo.common.core.mybatis.dialect.Dialect;
import com.jmmall.demo.common.page.PageBean;

/**
 * @描述：分页拦截器
 * @作者：renais
 * @创建时间：2016-12-6,下午3:36:22
 * @版本： 1.0
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	private static Dialect dialect;
	private static String pageFlag;

	public Object intercept(Invocation ivk) throws Throwable {

		if (ivk.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");

			if (mappedStatement.getId().matches(pageFlag)) {
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject error");
				} else {
					Connection connection = (Connection) ivk.getArgs()[0];
					String sql = boundSql.getSql().replaceAll("\\s{2,}", " ").replace(" from", " FROM").replace("order by", "ORDER BY")
							.replace("GROUP BY", "group by").trim();
					String countSql = sql;
					if (countSql.split("FROM").length > 2 || countSql.split("ORDER BY").length > 2 || countSql.indexOf("GROUP BY") > -1) {
						countSql = "SELECT COUNT(1) FROM (" + countSql + ") TMP";
					} else {
						int fromIndex = countSql.indexOf(" FROM");
						countSql = "SELECT COUNT(1)" + countSql.substring(fromIndex);
						
						int orderByIndex = countSql.indexOf("ORDER BY");
						if (orderByIndex > 0) {
							countSql = countSql.substring(0, orderByIndex);
						}
					}

					PreparedStatement countStmt = connection.prepareStatement(countSql);
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
					setParameters(countStmt, mappedStatement, countBS, parameterObject);
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();

					PageBean pageBean = null;
					if (parameterObject instanceof PageBean) {
						pageBean = (PageBean) parameterObject;
						pageBean.setTotalCount(count);
					} else {
						Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "pageBean");
						if (pageField != null) {
							pageBean = (PageBean) ReflectHelper.getValueByFieldName(parameterObject, "pageBean");
							if (pageBean == null)
								pageBean = new PageBean();
							pageBean.setTotalCount(count);
							ReflectHelper.setValueByFieldName(parameterObject, "pageBean", pageBean);
						} else {
							throw new NoSuchFieldException(parameterObject.getClass().getName());
						}
					}
					String pageSql = dialect.getLimitString(sql, pageBean.getCurrentPage(), pageBean.getNumPerPage());
					ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);
				}
			}
		}
		return ivk.proceed();
	}

	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties properties) {
		String dialectClass = properties.getProperty("dialectClass");
		pageFlag = properties.getProperty("pageFlag");
		if (StringUtils.isEmpty(pageFlag) || StringUtils.isEmpty(dialectClass)) {
			try {
				throw new PropertyException("pageFlag dialectClass property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		try {
			dialect = (Dialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot create dialect instance by dialect:" + dialect, e);
		}

	}
}