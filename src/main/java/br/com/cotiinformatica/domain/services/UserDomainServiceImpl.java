package br.com.cotiinformatica.domain.services;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.domain.dtos.AuthenticateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserResponseDto;
import br.com.cotiinformatica.domain.dtos.CreateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.CreateUserResponseDto;
import br.com.cotiinformatica.domain.entities.Role;
import br.com.cotiinformatica.domain.entities.User;
import br.com.cotiinformatica.domain.exceptions.AccessDeniedException;
import br.com.cotiinformatica.domain.exceptions.EmailAlreadyRegisteredException;
import br.com.cotiinformatica.domain.interfaces.UserDomainService;
import br.com.cotiinformatica.infrastructure.components.JwtTokenComponent;
import br.com.cotiinformatica.infrastructure.components.RabbitMQProducerComponent;
import br.com.cotiinformatica.infrastructure.components.SHA256Component;
import br.com.cotiinformatica.infrastructure.repositories.RoleRepository;
import br.com.cotiinformatica.infrastructure.repositories.UserRepository;

@Service
public class UserDomainServiceImpl implements UserDomainService {

	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired ModelMapper modelMapper;
	@Autowired SHA256Component sha256Component;
	@Autowired RabbitMQProducerComponent rabbitMQProducerComponent;
	@Autowired JwtTokenComponent jwtTokenComponent;

	@Override
	public AuthenticateUserResponseDto authenticate(AuthenticateUserRequestDto request) {

		User user = userRepository.findByEmailAndPassword
				(request.getEmail(), sha256Component.hash(request.getPassword()));

		if(user == null)
			throw new AccessDeniedException();

		AuthenticateUserResponseDto response = new AuthenticateUserResponseDto();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole().getName());
		response.setAccessToken(jwtTokenComponent.generateToken(user));
		response.setExpiration(jwtTokenComponent.getExpirationDate().toInstant());

		return response;
	}

	@Override
	public CreateUserResponseDto create(CreateUserRequestDto request) {

		if(userRepository.findByEmail(request.getEmail()) != null)
			throw new EmailAlreadyRegisteredException(request.getEmail());

		User user = modelMapper.map(request, User.class);
		Role role = roleRepository.findByName("DEFAULT");

		user.setId(UUID.randomUUID());
		user.setPassword(sha256Component.hash(request.getPassword()));
		user.setRole(role);

		userRepository.save(user);

		try {
			rabbitMQProducerComponent.sendMessage(user);
		}
		catch(Exception e) {
			e.printStackTrace(); //TODO
		}

		CreateUserResponseDto response = modelMapper.map(user, CreateUserResponseDto.class);
		response.setRole(user.getRole().getName());
		response.setCreatedAt(Instant.now());

		return response;
	}
}