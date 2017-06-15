package br.com.torcedor.api.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.torcedor.api.client.CampanhaServiceClient;
import br.com.torcedor.api.client.CampanhaServiceClientImpl;
import br.com.torcedor.api.exception.ValidateException;
import br.com.torcedor.api.model.Campanha;
import br.com.torcedor.api.model.Torcedor;
import br.com.torcedor.api.repository.TorcedorRepository;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * TorcedorServiceTest
 */
public class TorcedorServiceTest {

    @InjectMocks
    TorcedorService service;
    @Mock
    TorcedorRepository repository;
    @Mock
    CampanhaServiceClient campanhaService;

    @Before
	public void setup() {
		initMocks(this);
	}

    @Test
    public void shouldTryToCreateRegisteredTorcedorAndReturnCampanhas() {
        final Torcedor created = new Torcedor();
        created.setId("1910");
        created.setNome("Torcedor");
        created.setDtNascimento(new Date());
        created.setEmail("torcedor@tmail.com");
        created.setIdTimeCoracao("12345");
        final Campanha campanha = new Campanha();
        campanha.setId("13513");
        campanha.setNome("Camp 1");
        campanha.setIdTime("12345");
        when(repository.findByEmail(created.getEmail())).thenReturn(created);
        when(campanhaService.listAll()).thenReturn(Arrays.asList(campanha));

        final Torcedor newTorcedor = new Torcedor();
        newTorcedor.setId("1910");
        newTorcedor.setNome("Torcedor");
        newTorcedor.setDtNascimento(new Date());
        newTorcedor.setEmail("torcedor@tmail.com");
        newTorcedor.setIdTimeCoracao("12345");
        Collection<Campanha> campanhas = service.criar(newTorcedor);

        assertNotNull(campanhas);
        assertFalse(campanhas.isEmpty());

    }

     @Test
    public void shouldTryToCreateRegisteredTorcedorAndReturnCampanhasEmpty() {
        
        final Campanha campanha = new Campanha();
        campanha.setId("13513");
        campanha.setNome("Camp 1");
        campanha.setIdTime("12345");
        when(campanhaService.listAll()).thenReturn(Arrays.asList(campanha));

        final Torcedor newTorcedor = new Torcedor();
        newTorcedor.setId("1910");
        newTorcedor.setNome("Torcedor");
        newTorcedor.setDtNascimento(new Date());
        newTorcedor.setEmail("torcedor@tmail.com");
        newTorcedor.setIdTimeCoracao("12345");
        Collection<Campanha> campanhas = service.criar(newTorcedor);

        assertNull(campanhas);

    }

    @Test(expected = ValidateException.class)
    public void shouldThrowErrorWhenTryToCreateRegisteredTorcedorWithRegisteredCamapanhas() {

        final Torcedor created = new Torcedor();
        created.setId("1910");
        created.setNome("Torcedor");
        created.setDtNascimento(new Date());
        created.setEmail("torcedor@tmail.com");
        created.setIdTimeCoracao("12345");
        created.setCampanhas(new ArrayList<String>(Arrays.asList("4343", "43253")));
        
        when(repository.findByEmail(created.getEmail())).thenReturn(created);
       
        final Torcedor newTorcedor = new Torcedor();
        newTorcedor.setId("1910");
        newTorcedor.setNome("Torcedor");
        newTorcedor.setDtNascimento(new Date());
        newTorcedor.setEmail("torcedor@tmail.com");
        newTorcedor.setIdTimeCoracao("12345");
        service.criar(newTorcedor);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenTryAssociateAndTorcedorNotRegistered() {

        service.associar("1345", "6789");;
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenTryAssociateAndCampanhaNotRegistered() {

        final Torcedor created = new Torcedor();
        created.setId("1910");
        created.setNome("Torcedor");
        created.setDtNascimento(new Date());
        created.setEmail("torcedor@tmail.com");
        created.setIdTimeCoracao("12345");
        created.setCampanhas(new ArrayList<String>(Arrays.asList("4343", "43253")));
        when(repository.findOne(created.getId())).thenReturn(created);
        service.associar("1910", "6789");;
    }

    @Test()
    public void shouldSucessWhenTryAssociateTorcedorAndCampanha() {

        final Torcedor created = new Torcedor();
        created.setId("1910");
        created.setNome("Torcedor");
        created.setDtNascimento(new Date());
        created.setEmail("torcedor@tmail.com");
        created.setIdTimeCoracao("12345");
        when(repository.findOne(created.getId())).thenReturn(created);
        when(campanhaService.findCampanha("6789")).thenReturn("{id: 6789}");

        service.associar("1910", "6789");
        verify(repository, times(1)).save(created);
    }
}