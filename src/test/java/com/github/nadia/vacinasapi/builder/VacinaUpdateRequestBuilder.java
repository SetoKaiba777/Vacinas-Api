package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.api.DTO.request.VacinaUpdateRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
public class VacinaUpdateRequestBuilder {

    @Builder.Default
    public String nome = "Febre Amarela";

    public VacinaUpdateRequest ToVacinaUpdateRequest(){
        return new VacinaUpdateRequest(nome);
    }

}
