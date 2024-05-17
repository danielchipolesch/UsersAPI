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
import br.com.cotiinformatica.domain.interfaces.UserDomainService;
import br.com.cotiinformatica.infrastructure.components.SHA256Component;
import br.com.cotiinformatica.infrastructure.repositories.RoleRepository;
import br.com.cotiinformatica.infrastructure.repositories.UserRepository;

@Service
public class UserDomainServiceImpl implements UserDomainService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleReposity;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	SHA256Component sha256Component;

	@Override
	public AuthenticateUserResponseDto authenticate(AuthenticateUserRequestDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateUserResponseDto create(CreateUserRequestDto request) {
		User user = modelMapper.map(request, User.class);
		Role role = roleReposity.findByName("DEFAUL");
		
		user.setId(UUID.randomUUID());
		user.setPassword(sha256Component.hash(request.getPassword()));
		user.setRole(role);
		
		userRepository.save(user);
		
		CreateUserResponseDto response = modelMapper.map(user, CreateUserResponseDto.class);
		response.setRole(user.getRole().getName());
		response.setCreatedAt(Instant.now());
		
		return response;
		
	}

}
