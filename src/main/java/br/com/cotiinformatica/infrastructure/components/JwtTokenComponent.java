package br.com.cotiinformatica.infrastructure.components;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.cotiinformatica.domain.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenComponent {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpiration;

    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setSubject(user.getId().toString()) //identificação do usuário
                .setNotBefore(new Date()) //data de geração do token
                .setExpiration(getExpirationDate()) //data e hora de expiração do token
                .addClaims(claims) //permissões (claims) do usuário
                .signWith(SignatureAlgorithm.HS256, jwtSecret) //chave para assinatura do token
                .compact();
    }

    public Date getExpirationDate() {
        Date dataAtual = new Date();
        return new Date(dataAtual.getTime() + Long.parseLong(jwtExpiration));
    }
}


