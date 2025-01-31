package com.neurotech.currencyAPI.DBPopulator;

import com.neurotech.currencyAPI.Repository.CambioRepository;
import com.neurotech.currencyAPI.model.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DBPopulator implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private CambioRepository cambioRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event){
        RestTemplate template = new RestTemplate();
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("olinda.bcb.gov.br")
                .path("/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial,dataFinalCotacao=@dataFinalCotacao)")
                .queryParam("@dataInicial", "'01-01-2022'")
                .queryParam("@dataFinalCotacao", "'01-01-2023'")
                .queryParam("$format", "json")
                .build()
                .toUriString();

        CotacaoResponse response = template.getForObject(url, CotacaoResponse.class);

        saveAllInstancesFromResponse(response);

    }

    private void saveAllInstancesFromResponse(CotacaoResponse response){
        List<Cambio> cambioList = new ArrayList<>();
        for(Cotacao cotacao : response.getCotacoes()){
            Cambio cambio = new Cambio();
            cambio.setCotacaoCompra(1.0f/cotacao.getCotacaoCompra());
            cambio.setCotacaoVenda(1.0f/cotacao.getCotacaoVenda());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = cotacao.getDataCotacao();
            strDate = strDate.substring(0,11);

            Date date = null;
            try{
                date = sdf.parse(strDate);
                cambio.setDataCambio(date);
                cambioRepository.save(cambio);

            }
            catch (Exception e){
                System.out.println("Date convertion failed");
                System.exit(1);
            }
        }
    }



}
