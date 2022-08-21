package com.digitalhonors.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digitalhonors.authorization.config.JwtTokenUtil;
import com.digitalhonors.authorization.model.JwtRequest;
import com.digitalhonors.authorization.model.JwtResponse;
import com.digitalhonors.authorization.model.UserDTO;
import com.digitalhonors.authorization.service.JwtUserDetailsService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class JwtAuthenticationController {
	
	private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		logger.info("Authentication Begin.");
		
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		logger.info("Fetching username and password from request body.");
		logger.info("Checking if "+authenticationRequest.getUsername()+" is valid user.");
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());
		
		final String token = jwtTokenUtil.generateToken(userDetails);
		logger.info("Token generated.");
		logger.info("Sending response back.");
		return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		
		logger.info("Registering new user.");
		
		return ResponseEntity.ok(userDetailsService.save(user));
		
	}
	
	

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			logger.info("User authenticated");
		} catch (DisabledException e) {
			logger.warn("User is disabled.");
			throw new Exception("USER_DISABLED", e);
		
		} catch (BadCredentialsException e) {
			logger.warn("Invalid credentials.");
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}