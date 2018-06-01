package com.jt.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;

//利用通用Mapper
public interface UserMapper extends SysMapper<User>{
	
	int findCheckUser(@Param("param")String param, 
			@Param("cloumn")String cloumn);

	User findUserByUP(User user);

}
