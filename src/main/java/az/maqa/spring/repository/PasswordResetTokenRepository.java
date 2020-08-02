package az.maqa.spring.repository;

import org.springframework.data.repository.CrudRepository;

import az.maqa.spring.entity.PasswordResetTokenEntity;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

	PasswordResetTokenEntity findByToken(String token);

	
	
	
}
