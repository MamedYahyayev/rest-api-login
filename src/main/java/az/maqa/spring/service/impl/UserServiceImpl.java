package az.maqa.spring.service.impl;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import az.maqa.spring.dto.UserDTO;
import az.maqa.spring.repository.PasswordResetTokenRepository;
import az.maqa.spring.repository.UserRepository;
import az.maqa.spring.service.UserService;
import az.maqa.spring.utils.Util;
import az.maqa.spring.entity.PasswordResetTokenEntity;
import az.maqa.spring.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private Util util;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public UserDTO createUser(UserDTO userDTO) {
		UserEntity userEmail = userRepository.findByEmail(userDTO.getEmail());
		if (userEmail != null)
			throw new RuntimeException("User already exists");

		ModelMapper modelMapper = new ModelMapper();

		UserEntity user = modelMapper.map(userDTO, UserEntity.class);

		user.setUserId(util.generateUserId());
		user.setEncryptedPassword(passwordEncoder.encode(userDTO.getPassword()));
		user.setEmailVerificationToken(util.generateEmailVerificationToken(user.getUserId()));
		user.setEmailVerificationStatus(false);

		UserEntity savedUser = userRepository.save(user);

		UserDTO returnValue = modelMapper.map(savedUser, UserDTO.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
				userEntity.getEmailVerificationStatus(), true, true, true, new ArrayList<>());
	}

	@Override
	public UserDTO getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		ModelMapper mapper = new ModelMapper();

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDTO userDto = mapper.map(userEntity, UserDTO.class);

		return userDto;
	}

	@Override
	public boolean verifyToken(String token) {
		boolean isVerified = false;

		UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
		if (userEntity != null) {
			boolean hasExpired = util.hasTokenExpired(token);
			if (!hasExpired) {
				userEntity.setEmailVerificationToken(null);
				userEntity.setEmailVerificationStatus(Boolean.TRUE);
				userRepository.save(userEntity);
				isVerified = true;
			}
		}

		return isVerified;
	}

	@Override
	public boolean passwordResetRequest(String email) {
		boolean isReset = false;

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null)
			return isReset;

		String token = util.generatePasswordResetToken(userEntity.getUserId());

		PasswordResetTokenEntity passwordResetToken = new PasswordResetTokenEntity();
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(userEntity);
		PasswordResetTokenEntity savedPasswordResetToken = passwordResetTokenRepository.save(passwordResetToken);
		
		if (savedPasswordResetToken != null)
			isReset = true;

		// Send Email if email send , isReset will be true
		// isReset = util.sendEmail(email);

		return isReset;
	}

	@Override
	public boolean passwordReset(String token, String password) {
		boolean isReset = false;

		if (util.hasTokenExpired(token))
			return isReset;

		PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

		if (passwordResetTokenEntity == null)
			return isReset;

		// Create new Encoded Password
		String encodedPassword = passwordEncoder.encode(password);

		// change password
		UserEntity userEntity = passwordResetTokenEntity.getUser();
		userEntity.setEncryptedPassword(encodedPassword);
		UserEntity savedUserEntity = userRepository.save(userEntity);

		// check password is change or not
		if (savedUserEntity != null && savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)) {
			isReset = true;
		}

		// delete token from database
		passwordResetTokenRepository.delete(passwordResetTokenEntity);

		return isReset;
	}

}
