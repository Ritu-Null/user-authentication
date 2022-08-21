package com.digitalhonors.authorization.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.digitalhonors.authorization.dao.UserDao;
import com.digitalhonors.authorization.model.DAOUser;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class JwtUserDetailsServiceTest {
	
	
	@InjectMocks
	JwtUserDetailsService jwtUserDetailsService;
	
	@Mock
	UserDao mockUserDao;
	
	@Test
	void loadUserByUsername(){
	
		DAOUser dummyUser=new DAOUser();
		when(mockUserDao.findByUsername(anyString())).thenReturn(dummyUser);
		
	}
	
	@Test
	void saveUser() {
		DAOUser dummyUserDto=new DAOUser();
		when(mockUserDao.save(dummyUserDto)).thenReturn(dummyUserDto);
	}
}
