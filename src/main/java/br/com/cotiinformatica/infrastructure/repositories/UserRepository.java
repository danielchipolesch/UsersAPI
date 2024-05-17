package br.com.cotiinformatica.infrastructure.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.domain.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, UUID>{
	
	@Query("{ 'email' : ?0 }")
	User findByEmail(String email);
	
	@Query("{ 'email' : ?0, 'password' : ?1 }")
	User findByEmailAndPassword(String email, String password);

}
