package br.com.cotiinformatica.infrastructure.runners;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import br.com.cotiinformatica.domain.entities.Role;
import br.com.cotiinformatica.domain.entities.User;
import br.com.cotiinformatica.infrastructure.components.SHA256Component;
import br.com.cotiinformatica.infrastructure.repositories.RoleRepository;
import br.com.cotiinformatica.infrastructure.repositories.UserRepository;

@Component
public class LoadData implements ApplicationRunner {
	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired SHA256Component sha256Component;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//Criando o perfil DEFAULT
		Role defaultRole = roleRepository.findByName("DEFAULT");
		if(defaultRole == null) {
			defaultRole = new Role();
			defaultRole.setId(UUID.randomUUID());
			defaultRole.setName("DEFAULT");
			
			roleRepository.save(defaultRole);
		}
		
		//Criando o perfil ADMIN
		Role adminRole = roleRepository.findByName("ADMIN");
		if(adminRole == null) {
			adminRole = new Role();
			adminRole.setId(UUID.randomUUID());
			adminRole.setName("ADMIN");
			
			roleRepository.save(adminRole);
		}
		
		//Criando o usuário ADMIN padrão do banco de dados
		User adminUser = userRepository.findByEmail("admin@cotiinformatica.com.br");
		if(adminUser == null) {
			adminUser = new User();
			adminUser.setId(UUID.randomUUID());
			adminUser.setName("Usuário Administrador");
			adminUser.setEmail("admin@cotiinformatica.com.br");
			adminUser.setPassword(sha256Component.hash("@Admin123"));
			adminUser.setRole(adminRole);
			
			userRepository.save(adminUser);
		}
	}
}
