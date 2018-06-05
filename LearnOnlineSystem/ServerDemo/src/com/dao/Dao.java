package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("dao")
public class Dao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	//查询表中数据数量
	public long getCount(String tabName) 
	{
		String sql = "select count(*) from " + tabName;  
        long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count;
	}
	
	
	//
	public int add(String sql,Object... objects) 
	{
		return jdbcTemplate.update(sql, objects);
	}
	
	
	//
	public int delete(String sql,Object... objects) 
	{
		return jdbcTemplate.update(sql, objects);
	}
	
	
	//
	public long selectCount(String sql,Object... objects) 
	{ 
        return jdbcTemplate.queryForObject(sql, Long.class, objects);
	}
	
	
	public <T> T selectObject(String sql, RowMapper<T> rowMapper, Object... objects) 
	{		
		return jdbcTemplate.queryForObject(sql, rowMapper, objects);
	}	
	
	public <T> List<T> selectList(String sql,RowMapper<T> rowMapper, Object... objects) 
	{
        return jdbcTemplate.query(sql, rowMapper,objects); 
	}
	
	public int modify(String sql, Object... objects) 
	{
	    return jdbcTemplate.update(sql,objects);		
	}

}
