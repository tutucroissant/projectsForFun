package com.jt.web.service;

import com.jt.web.pojo.User;

public interface UserService {

	String saveUser(User user);

	String findUserByUP(User user);

}
