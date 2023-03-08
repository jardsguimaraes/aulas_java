package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.Especialidade;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockBean
    private MedicoRepository repository;

    @Test
    @DisplayName("deveria devolver erro hhtp 400 quando as informações estão invalidas")
    @WithMockUser
    void testCadastrarCenario1() throws Exception {
        var responser = mvc.perform(post("/medicos")).andReturn().getResponse();
        assertThat(responser.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deveria o codigo hhtp 200 quando as informações estão validas")
    @WithMockUser
    void testCadastrarCenario2() throws Exception {
        var dadosCadastro = new DadosCadastroMedico("medico", "medico@gmail.com",
                "82999999999", "000000",
                Especialidade.ORTOPEDIA, dadosEndereco());

        when(repository.save(any())).thenReturn(new Medico(dadosCadastro));

        var responser = mvc.perform(post("/medicos").contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoMedico(null,
                dadosCadastro.nome(), dadosCadastro.email(),
                dadosCadastro.crm(), dadosCadastro.telefone(), dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco()));

        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        assertThat(responser.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(responser.getContentAsString()).isEqualTo(jsonEsperado);

    }

    private DadosEndereco dadosEndereco() {
        return new DadosEndereco("rua xpto", "jatiuca", "58055423", "Maceio", "al", null, null);
    }
}
