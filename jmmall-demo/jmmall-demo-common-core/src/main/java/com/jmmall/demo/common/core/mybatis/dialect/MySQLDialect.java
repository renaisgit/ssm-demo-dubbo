package com.jmmall.demo.common.core.mybatis.dialect;

/**
 * @描述：MySQL数据库分页适配器
 * @作者：renais
 * @创建时间：2016-12-6,下午1:23:12
 * @版本： 1.0
 */
public class MySQLDialect extends Dialect {
	public boolean supportsLimitOffset() {
		return true;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset > 0) {
			sql += " limit " + offsetPlaceholder + "," + limitPlaceholder;
		} else {
			sql += " limit " + limitPlaceholder;
		}
		return sql;
	}
}
