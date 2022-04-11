package com.github.nadia.vacinasapi.builder;

import com.github.nadia.vacinasapi.domain.entity.Usuario;
import com.github.nadia.vacinasapi.domain.entity.Vacina;
import lombok.Builder;
import net.bytebuddy.pool.TypePool;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
public class UsuarioBuilder {


    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Caio";

    @Builder.Default
    private String cpf = "000.000.000-00";

    @Builder.Default
    private String email = "teste@teste.com.br";

    @Builder.Default
    private LocalDate dataNascimento = LocalDate.parse("1990-10-10");

    @Builder.Default
    private List<Vacina> vacinas = new ArrayList<>();

    public Usuario toUsuario(){
        return new Usuario(id,nome,cpf,email,dataNascimento,vacinas);
    }
}
