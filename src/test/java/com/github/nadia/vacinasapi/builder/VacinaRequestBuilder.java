package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.request.VacinaRequest;
import com.github.nadia.vacinasapi.api.DTO.response.VacinaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
public class VacinaRequestBuilder {

    @Builder.Default
    private String nome = "CoronaVac";

    @Builder.Default
    private String email = "teste@teste.com.br";

    public VacinaRequest toVacinaResquet(){
        return new VacinaRequest(nome,email);
    }
}
