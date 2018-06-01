package com.jt.sso.service;

import com.jt.sso.pojo.User;

public interface UserService {

	Boolean findCheckUser(String param, Integer type);

	String saveUser(User user);

	String findUserByUP(User user);

}
