package com.digitalhonors.authorization.service;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.digitalhonors.authorization.dao.UserDao;
import com.digitalhonors.authorization.model.DAOUser;
import com.digitalhonors.authorization.model.UserDTO;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtUserDetailsService.class);
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DAOUser user = userDao.findByUsername(username);
		if (user == null) {
			logger.warn("User "+ username + " is not a valid used.");
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
	
	public DAOUser save(UserDTO user) {
		
		
		DAOUser newUser = new DAOUser();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		logger.info("New user registered.");
		return userDao.save(newUser);
	}
	

}