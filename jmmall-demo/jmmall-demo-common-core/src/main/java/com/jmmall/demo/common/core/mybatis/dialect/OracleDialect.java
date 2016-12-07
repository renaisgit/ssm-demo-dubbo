package com.jmmall.demo.common.core.mybatis.dialect;

/**
 * @描述：Oracle数据库分页适配器
 * @作者：renais
 * @创建时间：2016-12-6,下午1:23:12
 * @版本： 1.0
 */
public class OracleDialect extends Dialect {
	public boolean supportsLimitOffset() {
		return true;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {

		sql = sql.trim();
		boolean isForUpdate = false;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}
		
		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			pagingSelect.append(" ) row_ ) where rownum_ <= ").append(offset+limit).append(" and rownum_ > ").append(offset);
		}
		else {
			pagingSelect.append(" ) where rownum <= ").append(limit);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}
		
		return pagingSelect.toString();
	}
}
