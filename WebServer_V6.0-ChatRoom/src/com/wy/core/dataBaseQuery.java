package com.wy.core;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wy.common.CommonChar;

public class dataBaseQuery {
	private String uri;
	private int flag=0;
	private Map<String, String> sqlMap; 
	private Connection conn = null;
	private PreparedStatement ps =null;
	private ResultSet rs = null;
	private ComboPooledDataSource pool;
	private File file = null;
	public dataBaseQuery(String uri,int flag) {
		this.uri = uri;
		this.flag = flag;
		sqlMap = new HashMap<String, String>();
		pool = new ComboPooledDataSource();
	}
	public File init() {
		try {
			conn = pool.getConnection();
			//储存待查找的信息，可以通过Key中（username 和 password来查找相应信息）
			String draftString = uri.substring(uri.indexOf("?")+1);
			String[] draftStrings = draftString.split("&");		
			for (String sds : draftStrings) {
				String name = sds.split("=")[0]; 
				String value = sds.split("=")[1];
				sqlMap.put(name, value);
				System.out.println("name = "+name+";value = "+value);
			}
			if(flag==1) return regisService();
			if(flag==2)	return loginService();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(conn, ps, rs);
		}
		return null;
	}

	private File loginService() {	
		String sql = "select * from user where username=? and password=?";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1,sqlMap.get("username"));
			ps.setString(2,sqlMap.get("password"));
			rs = ps.executeQuery();
			if(rs.next()){
				System.out.println("登陆成功");
				return new File(CommonChar.htmlUrl+"/sucessLogin.html");
			}
			else {
				System.out.println("登录失败");
				return new File(CommonChar.htmlUrl+"/FailureLoginOrRegis.html");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	private File regisService() {//注册信息
			String sql = "insert into user values(null,?,?)";
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1,sqlMap.get("username"));
				ps.setString(2,sqlMap.get("password"));
				ps.executeUpdate();//这里不能写ps.executeUpdate(sql);
				System.out.println("注册成功");
				return new File(CommonChar.htmlUrl+"/sucessLogin.html");
		} catch (Exception e) {
			e.printStackTrace();
		}	return null;	
	}
	private void close(Connection conn, PreparedStatement ps, ResultSet 
			rs) { 
			  if(rs != null){ 
			    try { 
			      rs.close(); 
			    } catch (Exception e) { 
			      e.printStackTrace(); 
			    } finally{ 
			      rs = null; 
			    } 
			  } 
			  if(ps != null){ 
			    try { 
			      ps.close(); 
			    } catch (Exception e) { 
			      e.printStackTrace(); 
			    } finally{ 
			      ps = null; 
			    } 
			  } 
			  if(conn != null){ 
			    try { 
			      /* 如果 conn 是自己找数据库获取的 
			       * 连接对象，此处是关连接 
			       * 如果 conn 是通过 c3p0 连接池获取 
			       * 的连接对象，此处是还连接 
			       */ 
			      conn.close(); 
			    } catch (Exception e) { 
			      e.printStackTrace(); 
			    } finally{ 
			      conn = null; 
			    } 
			  } 
			} 
	
	
}
