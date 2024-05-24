package br.com.cotiinformatica.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document
public class MessageLogger {

    @Id
    private UUID id;
    private String status;
    private Instant createdAt;
    private String message;

    @DBRef
    private User user;
}
