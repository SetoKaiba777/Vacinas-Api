package com.github.nadia.vacinasapi.controller;

import com.github.nadia.vacinasapi.api.DTO.request.UsuarioRequest;
import com.github.nadia.vacinasapi.api.DTO.request.UsuarioUpdateRequest;
import com.github.nadia.vacinasapi.api.DTO.request.VacinaRequest;
import com.github.nadia.vacinasapi.api.DTO.request.VacinaUpdateRequest;
import com.github.nadia.vacinasapi.api.DTO.response.UsuarioResponse;
import com.github.nadia.vacinasapi.api.DTO.response.VacinaResponse;
import com.github.nadia.vacinasapi.api.controller.UsuarioController;
import com.github.nadia.vacinasapi.api.controller.VacinaController;
import com.github.nadia.vacinasapi.builder.*;
import com.github.nadia.vacinasapi.core.mapper.UsuarioMapper;
import com.github.nadia.vacinasapi.core.mapper.VacinaMapper;
import com.github.nadia.vacinasapi.domain.entity.Usuario;
import com.github.nadia.vacinasapi.domain.entity.Vacina;
import com.github.nadia.vacinasapi.domain.exception.NotFoundException;
import com.github.nadia.vacinasapi.domain.exception.ServiceException;
import com.github.nadia.vacinasapi.domain.service.UsuarioService;
import com.github.nadia.vacinasapi.domain.service.VacinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.nadia.vacinasapi.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UsuarioControllerTest {

    private final String USUARIO_API_URL_PATH = "/api/v1/usuarios";
    private final String USUARIO_VACINA = "/vacinas";
    private final Long VALID_ID = 1L;
    private final Long INVALID_ID = 2L;
    private final String INVALID_USER_MAIL = "invalid@teste.com.br";
    private final String INVALID_USER_CPF = "288.580.430-06";

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private VacinaMapper vacinaMapper;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }
    @Test
    void whenGETListUsuariosIsCalled_ThenAnListOfUsuariosIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioResponse usuarioResponse = UsuarioResponseBuilder.builder().build().toUsuarioResponse();

        //Estabelecendo comportamento dos Mocks
        when(usuarioService.buscarTodos()).thenReturn(Collections.singletonList(usuario));
        when(usuarioMapper.toUsuarioResponseList(Collections.singletonList(usuario))).thenReturn(Collections.singletonList(usuarioResponse));

        //Realizando o teste via MockMVC

        mockMvc.perform(get(USUARIO_API_URL_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((usuarioResponse.getId().intValue()))))
                .andExpect(jsonPath("$[0].nome", is(usuarioResponse.getNome())))
                .andExpect(jsonPath("$[0].email", is(usuarioResponse.getEmail())))
                .andExpect(jsonPath("$[0].cpf", is(usuarioResponse.getCpf())))
                .andExpect(jsonPath("$[0].dataNascimento", is(parse(usuarioResponse.getDataNascimento()))));
    }

    @Test
    void whenGETUsuarioWithValidIdIsCalled_ThenUsuarioIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioResponse usuarioResponse = UsuarioResponseBuilder.builder().build().toUsuarioResponse();

        //Estabelecendo comportamento dos Mocks
        when(usuarioService.buscarPorId(VALID_ID)).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);

        //Realizando o teste via MockMVC
        mockMvc.perform(get(USUARIO_API_URL_PATH+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(usuarioResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((usuarioResponse.getId().intValue()))))
                .andExpect(jsonPath("$.nome", is(usuarioResponse.getNome())))
                .andExpect(jsonPath("$.email", is(usuarioResponse.getEmail())))
                .andExpect(jsonPath("$.cpf", is(usuarioResponse.getCpf())))
                .andExpect(jsonPath("$.dataNascimento", is(parse(usuarioResponse.getDataNascimento()))));
    }

    @Test
    void whenGETUsuarioWithInvalidIdIsCalled_ThenAnErrorIsReturned() throws Exception {
        //Estabelecendo comportamento dos Mocks
        doThrow(NotFoundException.class).when(usuarioService).buscarPorId(INVALID_ID);

        //Realizando o teste via MockMVC
        mockMvc.perform(get(USUARIO_API_URL_PATH+"/"+INVALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPOSTUsuarioIsCalled_ThenUsuarioIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioResponse usuarioResponse = UsuarioResponseBuilder.builder().build().toUsuarioResponse();
        UsuarioRequest usuarioRequest = UsuarioRequestBuilder.builder().build().toUsuarioRequest();

        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioRequest)).thenReturn(usuario);
        when(usuarioService.salvar(usuario)).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);

        //Realizando o teste via MockMVC
        mockMvc.perform(post(USUARIO_API_URL_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioResponse)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is((usuarioResponse.getId().intValue()))))
                .andExpect(jsonPath("$.nome", is(usuarioResponse.getNome())))
                .andExpect(jsonPath("$.email", is(usuarioResponse.getEmail())))
                .andExpect(jsonPath("$.cpf", is(usuarioResponse.getCpf())))
                .andExpect(jsonPath("$.dataNascimento", is(parse(usuarioResponse.getDataNascimento()))));
    }

    @Test
    void whenPOSTUsuarioWithInvalidEmailIsCalled_ThenAnErrorIsReturned() throws Exception {
        //Configurações iniciais
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setEmail(INVALID_USER_MAIL);
        UsuarioRequest usuarioRequest = UsuarioRequestBuilder.builder().build().toUsuarioRequest();
        usuarioRequest.setEmail(INVALID_USER_MAIL);

        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioRequest)).thenReturn(usuario);
        doThrow(ServiceException.class).when(usuarioService).salvar(usuario);


        //Realizando o teste via MockMVC
        mockMvc.perform(post(USUARIO_API_URL_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPOSTUsuarioWithInvalidCPFIsCalled_ThenAnErrorIsReturned() throws Exception {
        //Configurações iniciais
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        usuario.setCpf(INVALID_USER_CPF);
        UsuarioRequest usuarioRequest = UsuarioRequestBuilder.builder().build().toUsuarioRequest();
        usuarioRequest.setCpf(INVALID_USER_CPF);

        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioRequest)).thenReturn(usuario);
        doThrow(ServiceException.class).when(usuarioService).salvar(usuario);


        //Realizando o teste via MockMVC
        mockMvc.perform(post(USUARIO_API_URL_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void whenPUTUsuarioWithValidIdIsCalled_ThenAUsuarioIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioResponse usuarioResponse = UsuarioResponseBuilder.builder().build().toUsuarioResponse();
        UsuarioUpdateRequest usuarioUpdateRequest = UsuarioUpdateRequestBuilder.builder().build().toUsuarioUpdateRequest();
        updateSet(usuarioUpdateRequest,usuarioResponse);
        updateSet(usuarioUpdateRequest,usuario);


        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioUpdateRequest)).thenReturn(usuario);
        when(usuarioMapper.toUsuarioResponse(usuario)).thenReturn(usuarioResponse);
        when(usuarioService.atualizar(VALID_ID,usuario)).thenReturn(usuario);

        //Realizando o teste via MockMVC
        mockMvc.perform(put(USUARIO_API_URL_PATH+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((usuarioResponse.getId().intValue()))))
                .andExpect(jsonPath("$.nome", is(usuarioResponse.getNome())))
                .andExpect(jsonPath("$.email", is(usuarioResponse.getEmail())))
                .andExpect(jsonPath("$.cpf", is(usuarioResponse.getCpf())))
                .andExpect(jsonPath("$.dataNascimento", is(parse(usuarioResponse.getDataNascimento()))));
    }

    @Test
    void whenPUTInvalidUsuarioIdIsCalled_ThenAUsuarioIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioUpdateRequest usuarioUpdateRequest = UsuarioUpdateRequestBuilder.builder().build().toUsuarioUpdateRequest();
        updateSet(usuarioUpdateRequest,usuario);
        usuario.setId(INVALID_ID);

        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioUpdateRequest)).thenReturn(usuario);
        doThrow(ServiceException.class).when(usuarioService).atualizar(INVALID_ID,usuario);

        //Realizando o teste via MockMVC
        mockMvc.perform(put(USUARIO_API_URL_PATH+"/"+INVALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTUsuarioWithInvalidCPFIsCalled_ThenAnErrorIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioUpdateRequest usuarioUpdateRequest = UsuarioUpdateRequestBuilder.builder().build().toUsuarioUpdateRequest();
        usuarioUpdateRequest.setCpf(INVALID_USER_CPF);
        updateSet(usuarioUpdateRequest,usuario);


        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioUpdateRequest)).thenReturn(usuario);
        doThrow(ServiceException.class).when(usuarioService).atualizar(VALID_ID,usuario);


        //Realizando o teste via MockMVC
        mockMvc.perform(put(USUARIO_API_URL_PATH+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPUTUsuarioWithInvalidEmailIsCalled_ThenAnErrorIsReturned() throws Exception {
        //Configurações iniciais
        Usuario usuario = UsuarioBuilder.builder().build().toUsuario();
        UsuarioUpdateRequest usuarioUpdateRequest = UsuarioUpdateRequestBuilder.builder().build().toUsuarioUpdateRequest();
        usuarioUpdateRequest.setEmail(INVALID_USER_MAIL);
        updateSet(usuarioUpdateRequest,usuario);


        //Estabelecendo comportamento dos Mocks
        when(usuarioMapper.toUsuarioEntity(usuarioUpdateRequest)).thenReturn(usuario);
        doThrow(ServiceException.class).when(usuarioService).atualizar(VALID_ID,usuario);


        //Realizando o teste via MockMVC
        mockMvc.perform(put(USUARIO_API_URL_PATH+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void whenDELETECalledWithValidId_ThenStatusNoContentIsReturned() throws Exception {
        //Estabelecendo comportamento dos Mocks
        doNothing().when(usuarioService).deletar(VALID_ID);

        //Realizando Testes
        mockMvc.perform(delete(USUARIO_API_URL_PATH+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETECalledWithInvalidId_ThenAnErrorIsReturned() throws Exception {
        //Estabelecendo comportamento dos Mocks
        doThrow(ServiceException.class).when(usuarioService).deletar(INVALID_ID);

        //Realizando Testes
        mockMvc.perform(delete(USUARIO_API_URL_PATH+"/"+INVALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETUsuarioVacinaWithValidIdIsCalled_ThenAListOfVacinasIsReturned() throws Exception {
        //Estabelecendo comportamento dos Mocks
        Vacina vacina = VacinaBuilder.builder().build().toVacina();
        VacinaResponse vacinaResponse = VacinaResponseBuilder.builder().build().toVacinaResponse();

        //Estabelecendo Comportamento dos Mocks
        when(usuarioService.listarVacinas(VALID_ID)).thenReturn(Collections.singletonList(vacina));
        when(vacinaMapper.toVacinaResponseList(Collections.singletonList(vacina))).thenReturn(Collections.singletonList(vacinaResponse));

        //Realizando os testes
        mockMvc.perform(get(USUARIO_API_URL_PATH+USUARIO_VACINA+"/"+VALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is((vacinaResponse.getId().intValue()))))
                .andExpect(jsonPath("$[0].nome", is(vacinaResponse.getNome())))
                .andExpect(jsonPath("$[0].email", is(vacinaResponse.getEmail())))
                .andExpect(jsonPath("$[0].dataAplicacao", is(parse(vacinaResponse.getDataAplicacao()))));
    }

    @Test
    void whenGETUsuarioVacinaWithInvalidIdIsCalled_ThenAnErrorIsReturned() throws Exception {

        //Estabelecendo Comportamento dos Mocks
        doThrow(ServiceException.class).when(usuarioService).listarVacinas(INVALID_ID);

        //Realizando os testes
        mockMvc.perform(get(USUARIO_API_URL_PATH+USUARIO_VACINA+"/"+INVALID_ID.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private void updateSet(UsuarioUpdateRequest usuarioUpdateRequest, UsuarioResponse usuarioResponse){
        usuarioResponse.setCpf(usuarioUpdateRequest.getCpf());
        usuarioResponse.setEmail(usuarioUpdateRequest.getEmail());
        usuarioResponse.setDataNascimento(usuarioUpdateRequest.getDataNascimento());
        usuarioResponse.setNome(usuarioUpdateRequest.getNome());
    }

    private void updateSet(UsuarioUpdateRequest usuarioUpdateRequest, Usuario usuario){
        usuario.setCpf(usuarioUpdateRequest.getCpf());
        usuario.setEmail(usuarioUpdateRequest.getEmail());
        usuario.setDataNascimento(usuarioUpdateRequest.getDataNascimento());
        usuario.setNome(usuarioUpdateRequest.getNome());
    }
    private List<Integer> parse(LocalDate localDate){
        List<Integer> dataList = new ArrayList<>();
        dataList.add(localDate.getYear());
        dataList.add(localDate.getMonthValue());
        dataList.add(localDate.getDayOfMonth());
        return dataList;
    }
}



