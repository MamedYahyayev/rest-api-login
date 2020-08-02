package az.maqa.spring.security;

public class SecurityConstants {

	public static final long EXPIRATION_TIME = 864000000;
	public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000;// 1 hour
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users"; // create user url
	public static final String TOKEN_SECRET = "sadasd21341241";
	public static final String EMAIL_VERIFICATION_URL = "/users/email-verification"; // verify email
	public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request"; //send Email for reset password
	public static final String PASSWORD_RESET_URL = "/users/password-reset"; // reset password
	

	
}
