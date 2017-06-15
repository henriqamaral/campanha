package br.com.time.api.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import br.com.time.api.model.Time;
import br.com.time.api.repository.TimeRepository;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

/**
 * TimeServiceTest
 */
public class TimeServiceTest {

    @InjectMocks
    TimeService service;
    @Mock
    TimeRepository repository;

    @Before
	public void setup() {
		initMocks(this);
	}

    @Test
	public void shouldCreateTime() {

		final Time t = new Time();
		t.setNome("Time 1");

		service.criar(t);
		verify(repository, times(1)).save(t);
	}
}       