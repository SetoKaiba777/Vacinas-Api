package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.request.UsuarioRequest;
import com.github.nadia.vacinasapi.api.DTO.request.UsuarioUpdateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Builder
public class UsuarioUpdateRequestBuilder {

    @Builder.Default
    private String nome = "Luiz";

    @Builder.Default
    private String cpf = "352.698.080-21";

    @Builder.Default
    private String email = "update@teste.com.br";

    @Builder.Default
    private LocalDate dataNascimento = LocalDate.parse("1991-10-10");

    public UsuarioUpdateRequest toUsuarioUpdateRequest(){
        return new UsuarioUpdateRequest(nome,cpf,email,dataNascimento);
    }
}

