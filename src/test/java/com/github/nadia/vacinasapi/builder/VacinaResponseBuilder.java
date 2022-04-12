package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.response.VacinaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
public class VacinaResponseBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "CoronaVac";

    @Builder.Default
    private String email = "teste@teste.com.br";

    @Builder.Default
    private LocalDate dataAplicacao = LocalDate.now();

    public VacinaResponse toVacinaResponse(){
        return new VacinaResponse(id,nome,email,dataAplicacao);
    }

}
