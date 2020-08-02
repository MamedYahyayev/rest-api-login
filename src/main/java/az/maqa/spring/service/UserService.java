package az.maqa.spring.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import az.maqa.spring.dto.UserDTO;

public interface UserService extends UserDetailsService {

	UserDTO createUser(UserDTO userDTO);

	UserDTO getUser(String email);

	boolean verifyToken(String token);

	boolean passwordResetRequest(String email);

	boolean passwordReset(String token, String password);
	
}
