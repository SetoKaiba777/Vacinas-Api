package com.github.nadia.vacinasapi.service;

import com.github.nadia.vacinasapi.builder.UsuarioBuilder;
import com.github.nadia.vacinasapi.builder.VacinaBuilder;
import com.github.nadia.vacinasapi.domain.entity.Usuario;
import com.github.nadia.vacinasapi.domain.entity.Vacina;
import com.github.nadia.vacinasapi.domain.exception.NotFoundException;
import com.github.nadia.vacinasapi.domain.exception.ServiceException;
import com.github.nadia.vacinasapi.domain.repository.UsuarioRepository;
import com.github.nadia.vacinasapi.domain.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    private final String VALID_EMAIL = "validmail@teste.com.";
    private final String INVALID_EMAIL = "invalidmail@teste.com.";

    private final String VALID_CPF = "000.000.000-00";
    private final String INVALID_CPF = "999.999.999-99";

    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 2L;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void whenListUsuariosIsCalled_thenShownThis(){
        //Condições iniciais
        Usuario usuario1 = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuario2 = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuario3 = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuario4 = UsuarioBuilder.builder().build().toUsuario();
        var usuarios = Arrays.asList(usuario1,usuario2,usuario3,usuario4);

        //Estabelecendo comportamento dos Mocks
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        //Realizando o teste
        var usuariosRetornados = usuarioService.buscarTodos();
        assertEquals(usuarios,usuariosRetornados);
    }

    @Test
    void whenSalvarIsCalledWithValidCPFAndEmail_thenShownUser(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setCpf(VALID_CPF);
        usuario.setEmail(VALID_EMAIL);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(usuarioRepository.findByCpf(VALID_CPF)).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        var usuarioSalvo = usuarioService.salvar(usuario);
        assertEquals(usuario,usuarioSalvo);
    }

    @Test
    void whenSalvarIsCalledWithInvalidCPF_thenAnErrorShouldBeShown(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuarioJaRegistrado = UsuarioBuilder.builder().build().toUsuario();
        usuarioJaRegistrado.setCpf(INVALID_CPF);
        usuario.setCpf(INVALID_CPF);
        usuario.setEmail(VALID_EMAIL);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(usuarioRepository.findByCpf(INVALID_CPF)).thenReturn(Optional.of(usuarioJaRegistrado));

        assertThrows(ServiceException.class,()->usuarioService.salvar(usuario));
    }

    @Test
    void whenSalvarIsCalledWithInvalidEmail_thenAnErrorShouldBeShown(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuarioJaRegistrado = UsuarioBuilder.builder().build().toUsuario();
        usuarioJaRegistrado.setEmail(INVALID_EMAIL);
        usuario.setCpf(VALID_CPF);
        usuario.setEmail(INVALID_EMAIL);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.of(usuarioJaRegistrado));
        when(usuarioRepository.findByCpf(VALID_CPF)).thenReturn(Optional.empty());

        //Realização do teste
        assertThrows(ServiceException.class,()->usuarioService.salvar(usuario));
    }

    @Test
    void whenBuscarPorIdIsCalledWithValidId_thenShownUser(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setId(VALID_ID);

        //Estabelecendo comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuario));

        //Realização do teste
        Usuario usuarioEncontrado = usuarioService.buscarPorId(VALID_ID);
        assertEquals(usuario,usuarioEncontrado);
    }

    @Test
    void whenBuscarPorIdIsCalledWithInvalidId_thenAnErrorShouldBeShown(){

        //Estabelecendo comportamento dos Mocks
        when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        //Realização do teste
        assertThrows(NotFoundException.class,()-> usuarioService.buscarPorId(INVALID_ID));
    }

    @Test
    void whenAtualizarIsCalledWithValidCPFEmailAndId_thenShownUser(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setCpf(VALID_CPF);
        usuario.setEmail(VALID_EMAIL);
        usuario.setId(VALID_ID);
        Usuario usuarioParaAtualizar = UsuarioBuilder.builder().build().toUsuario();
        usuarioParaAtualizar.setId(VALID_ID);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuarioParaAtualizar));
        when(usuarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(usuarioRepository.findByCpf(VALID_CPF)).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        //Realizando o teste
        var usuarioAtualizado = usuarioService.atualizar(VALID_ID,usuario);
        assertEquals(usuario,usuarioAtualizado);
    }

    @Test
    void whenAtualizarIsCalledWithInvalidId_thenAnErrorShouldBeShown(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setId(INVALID_ID);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        //Realizando o teste
        assertThrows(ServiceException.class,()->usuarioService.atualizar(INVALID_ID,usuario));
    }

    @Test
    void whenAtualizarIsCalledWithInvalidCPF_thenAnErrorShouldBeShown(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuarioParaAtualizar = UsuarioBuilder.builder().build().toUsuario();
        usuarioParaAtualizar.setCpf(INVALID_CPF);
        usuario.setId(VALID_ID);
        usuario.setCpf(INVALID_CPF);
        usuario.setEmail(VALID_EMAIL);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuarioParaAtualizar));
        when(usuarioRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());
        when(usuarioRepository.findByCpf(INVALID_CPF)).thenReturn(Optional.of(usuarioParaAtualizar));

        assertThrows(ServiceException.class,()->usuarioService.atualizar(VALID_ID,usuario));
    }

    @Test
    void whenAtualizarIsCalledWithInvalidEmail_thenAnErrorShouldBeShown(){
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        Usuario usuarioParaAtualizar = UsuarioBuilder.builder().build().toUsuario();
        usuario.setId(VALID_ID);
        usuarioParaAtualizar.setEmail(INVALID_EMAIL);
        usuario.setCpf(VALID_CPF);
        usuario.setEmail(INVALID_EMAIL);

        //Estabelecendo Comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuarioParaAtualizar));
        when(usuarioRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.of(usuarioParaAtualizar));

        //Realização do teste
        assertThrows(ServiceException.class,()->usuarioService.atualizar(VALID_ID,usuario));
    }

    @Test
    void whenListaDeVacinasRequested_thenShownThis() {
        //Condições Iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setId(VALID_ID);
        Vacina vacina1 = VacinaBuilder.builder().build().toVacina();
        usuario.getVacinas().add(vacina1);
        Vacina vacina2 = VacinaBuilder.builder().build().toVacina();
        usuario.getVacinas().add(vacina2);
        Vacina vacina3 = VacinaBuilder.builder().build().toVacina();
        usuario.getVacinas().add(vacina3);
        Vacina vacina4 = VacinaBuilder.builder().build().toVacina();
        usuario.getVacinas().add(vacina4);

        //Estabelecendo comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuario));

        //Realizando o teste
        var vacinasRetornadas = usuarioService.listarVacinas(VALID_ID);
        assertEquals(usuario.getVacinas(),vacinasRetornadas);
    }

    @Test
    void whenListaDeVacinasRequestedWithInvalidId_thenAnErrorShouldBeShown() {

        //Estabelecendo comportamento dos Mocks
        when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        //Realizando o teste
        assertThrows(ServiceException.class,()-> usuarioService.listarVacinas(INVALID_ID));
    }

    @Test
    void whenDeletarIsRequestedWithValidId_thenUsuarioShouldBeExcluded() {
        //Condições Iniciais
        var usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setId(VALID_ID);

        //Estabelecer comportamento dos Mocks
        when(usuarioRepository.findById(VALID_ID)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(VALID_ID);

        //Realizar teste
        usuarioService.deletar(usuario.getId());

        verify(usuarioRepository,times(1)).findById(usuario.getId());
        verify(usuarioRepository,times(1)).deleteById(usuario.getId());
    }

    @Test
    void whenDeletarIsRequestedWithInvalidId_thenAnErrorShouldBeShown() {

        //Estabelecer comportamento dos Mocks
        when(usuarioRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        //Realizar teste
        assertThrows(ServiceException.class,()->usuarioService.deletar(INVALID_ID));
    }

}
