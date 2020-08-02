package az.maqa.spring.utils;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import az.maqa.spring.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Util {

	public String generateUserId() {
		return UUID.randomUUID().toString();
	}

	public boolean hasTokenExpired(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.TOKEN_SECRET)
				.parseClaimsJws(token)
				.getBody();

		Date tokenExpirationDate = claims.getExpiration();
		Date today = new Date();
		
		return tokenExpirationDate.before(today);
	}

	public String generateEmailVerificationToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
				.compact();
		
		return token;
	}

	public String generatePasswordResetToken(String userId) {
		String token = Jwts.builder()
				.setSubject(userId)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
				.compact();
		
		return token;
	}

}
