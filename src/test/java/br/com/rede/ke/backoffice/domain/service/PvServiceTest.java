package br.com.rede.ke.backoffice.domain.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.rede.ke.backoffice.conciliation.domain.entity.Pv;
import br.com.rede.ke.backoffice.conciliation.domain.repository.PvRepository;
import br.com.rede.ke.backoffice.conciliation.domain.service.PvService;

@RunWith(MockitoJUnitRunner.class)
public class PvServiceTest {
    
    @InjectMocks
    private PvService pvService;
    
    @Mock
    private PvRepository pvRepository;
    
    @Test
    public void testValidPv(){
        Pv pv = new Pv();
        pv.setCode("1000201314");
        
        assertThat(pvService.isValidPv(pv), equalTo(true));
    }
    
    @Test
    public void testPvInvalidLength(){
        Pv pv = new Pv();
        pv.setCode("100020131419817181781781718");
        
        assertThat(pvService.isValidPv(pv), equalTo(false));
    }
    
    @Test
    public void testInvalidCharactersPv(){
        Pv pv = new Pv();
        pv.setCode("ABC5367");
        
        assertThat(pvService.isValidPv(pv), equalTo(false));
    }
    
    @Test
    public void testInvalidPvBatch(){
        Pv pv = new Pv();
        pv.setCode("ABC5367");
        
        assertThat(pvService.isValidPv(pv), equalTo(false));
    }
}
