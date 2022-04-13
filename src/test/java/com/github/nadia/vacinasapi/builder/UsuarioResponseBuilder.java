package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
public class UsuarioResponseBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Caio";

    @Builder.Default
    private String cpf = "352.698.080-21";

    @Builder.Default
    private String email = "teste@teste.com.br";

    @Builder.Default
    private LocalDate dataNascimento = LocalDate.parse("1990-10-10");

    public UsuarioResponse toUsuarioResponse(){
        return new UsuarioResponse(id,nome,cpf,email,dataNascimento);
    }
}
