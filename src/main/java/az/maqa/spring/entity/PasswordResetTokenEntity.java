package az.maqa.spring.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "password_reset_token")
public class PasswordResetTokenEntity extends BaseEntity {

	private String token;

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
