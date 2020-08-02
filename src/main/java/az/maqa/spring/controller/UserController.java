package az.maqa.spring.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import az.maqa.spring.dto.UserDTO;
import az.maqa.spring.request.RequestPasswordChange;
import az.maqa.spring.request.RequestPasswordReset;
import az.maqa.spring.request.RequestUser;
import az.maqa.spring.response.ResponseStatus;
import az.maqa.spring.response.ResponseUser;
import az.maqa.spring.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseUser createUser(@RequestBody RequestUser requestUser) {
		ModelMapper modelMapper = new ModelMapper();

		UserDTO userDTO = modelMapper.map(requestUser, UserDTO.class);

		UserDTO users = userService.createUser(userDTO);

		ResponseUser response = modelMapper.map(users, ResponseUser.class);

		return response;
	}

	@GetMapping(value = "/email-verification", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseStatus emailVerification(@RequestParam("token") String token) {
		ResponseStatus response = null;

		boolean isVerified = userService.verifyToken(token);

		if (isVerified)
			response = new ResponseStatus(1, "Your email has been successfully verified...");
		else
			response = new ResponseStatus(101, "Token is invalid or expired");

		return response;
	}

	@PostMapping(value = "/password-reset-request")
	@ResponseBody
	public ResponseStatus passwordResetRequest(@RequestBody RequestPasswordReset requestPasswordReset) {
		ResponseStatus response = null;

		boolean isSendEmailForPasswordReset = userService.passwordResetRequest(requestPasswordReset.getEmail());

		if (isSendEmailForPasswordReset)
			response = new ResponseStatus(1, "Email send...");
		else
			response = new ResponseStatus(101, "Token is invalid or expired");

		return response;
	}

	@PostMapping(value = "/password-reset")
	@ResponseBody
	public ResponseStatus passwordReset(@RequestBody RequestPasswordChange passwordChange) {
		ResponseStatus response = null;

		boolean isResetPassword = userService.passwordReset(passwordChange.getToken(), passwordChange.getPassword());

		if (isResetPassword)
			response = new ResponseStatus(1, "Password has been successfuly reseted...");
		else
			response = new ResponseStatus(102, "Password Reset Error");

		return response;
	}

}
