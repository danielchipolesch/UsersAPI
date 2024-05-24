package br.com.cotiinformatica.application.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.AuthenticateUserResponseDto;
import br.com.cotiinformatica.domain.dtos.CreateUserRequestDto;
import br.com.cotiinformatica.domain.dtos.CreateUserResponseDto;
import br.com.cotiinformatica.domain.interfaces.UserDomainService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired UserDomainService userDomainService;

	@PostMapping("authenticate")
	public ResponseEntity<AuthenticateUserResponseDto> authenticate(@RequestBody @Valid AuthenticateUserRequestDto request) {
		AuthenticateUserResponseDto response = userDomainService.authenticate(request);
		return ResponseEntity.status(200).body(response);
	}

	@PostMapping("create")
	public ResponseEntity<CreateUserResponseDto> create(@RequestBody @Valid CreateUserRequestDto request) {
		CreateUserResponseDto response = userDomainService.create(request);
		return ResponseEntity.status(201).body(response);
	}
}