package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.domain.entity.Usuario;
import com.github.nadia.vacinasapi.domain.entity.Vacina;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class VacinaBuilder {


    @Builder.Default
    private Long id =1L;

    @Builder.Default
    private String nome = "CoronaVac";

    @Builder.Default
    private Usuario usuario = UsuarioBuilder.builder().build().toUsuario();

    @Builder.Default
    private LocalDate dataAplicacao = LocalDate.now();

    public Vacina toVacina(){
        var vacinne = new Vacina(id,nome,usuario,dataAplicacao);
        vacinne.getUsuario().getVacinas().add(vacinne);
        return vacinne;
    }
}
