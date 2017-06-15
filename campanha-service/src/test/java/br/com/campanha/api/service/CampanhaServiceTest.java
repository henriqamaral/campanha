package br.com.campanha.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import br.com.campanha.api.Application;
import br.com.campanha.api.model.Campanha;
import br.com.campanha.api.repository.CampanhaRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Calendar;
import java.util.Date;

/**
 * CampanhaServiceTest
 */
public class CampanhaServiceTest {

    @InjectMocks
    CampanhaService service;
    @Mock
    CampanhaRepository repository;

    @Before
	public void setup() {
		initMocks(this);
	}

    @Test
	public void shouldFindById() {

		final Campanha c = new Campanha();
		c.setFim(getFimDate());
        c.setInicio(new Date());
        c.setNome("Camp 1");
        c.setIdTime("123456");

		when(service.recuperar(c.getId())).thenReturn(c);
		Campanha found = service.recuperar(c.getId());

		assertEquals(c, found);
	}

    @Test()
	public void shouldBeNullWhenIdIsNull() {
		final Campanha c = service.recuperar(null);
        assertNull(c);
	}

    @Test()
	public void shouldBeNullWhenIdIsEmpty() {
		final Campanha c = service.recuperar(null);
        assertNull(c);
	}

    
    @Test()
    public void shouldUpdateOtherCampanhaWhenDateFimIsSame() {
        final Campanha c = new Campanha();
		c.setFim(getFimDate());
        c.setInicio(new Date());
        c.setNome("Camp 1");
        c.setIdTime("123456");
        c.setId("IdOne");
        
        final Campanha c2 = new Campanha();
		c2.setFim(getFimDate());
        c2.setInicio(new Date());
        c2.setNome("Camp 2");
        c2.setIdTime("123456");
        
        when(service.recuperar("IdOne")).thenReturn(getCampanhaDateFimAdd());
        service.criar(c);
        service.criar(c2);
        final Campanha changed =  service.recuperar(c.getId());
        assertEquals(c.getNome(), changed.getNome());
        assertEquals(changed.getFim(), getFimDatePlusOne());
        verify(repository, times(1)).save(c);
        verify(repository, times(1)).save(c2);
    }

    private Date getFimDate() {
        Calendar c = Calendar.getInstance();
        c.add(10, Calendar.DAY_OF_MONTH);
        return c.getTime();
    }
    private Date getFimDatePlusOne() {
        Calendar c = Calendar.getInstance();
        c.add(11, Calendar.DAY_OF_MONTH);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Campanha getCampanhaDateFimAdd() {
        Campanha camp = new Campanha();
        camp.setFim(getFimDatePlusOne());
        camp.setInicio(new Date());
        camp.setNome("Camp 1");
        camp.setIdTime("123456");
        camp.setId("IdOne");
        return camp;
    }
    
}