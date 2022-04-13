package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.request.UsuarioRequest;
import com.github.nadia.vacinasapi.api.DTO.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public class UsuarioRequestBuilder {

        @Builder.Default
        private String nome = "Caio";

        @Builder.Default
        private String cpf = "352.698.080-21";

        @Builder.Default
        private String email = "teste@teste.com.br";

        @Builder.Default
        private LocalDate dataNascimento = LocalDate.parse("1990-10-10");

        public UsuarioRequest toUsuarioRequest(){
            return new UsuarioRequest(nome,cpf,email,dataNascimento);
        }
}
