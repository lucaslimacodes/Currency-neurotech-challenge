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

@Component
public class DBPopulator implements ApplicationListener<ApplicationReadyEvent> {

    // Date from which the database will be filled with
    //'MM-dd-yyyy'
    private final String startDate = "'01-01-2010'";

    @Autowired
    private CambioRepository cambioRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        // obtaining today`s date to be the most recent data fetched from the api
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String endDate = sdf.format(today);
        endDate = "'" + endDate + "'";

        // Performing the request to the external api
        RestTemplate template = new RestTemplate();
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("olinda.bcb.gov.br")
                .path("/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarPeriodo(dataInicial=@dataInicial," +
                        "dataFinalCotacao=@dataFinalCotacao)")
                .queryParam("@dataInicial", startDate)
                .queryParam("@dataFinalCotacao", endDate)
                .queryParam("$format", "json")
                .build()
                .toUriString();

        CotacaoResponse response = template.getForObject(url, CotacaoResponse.class);

        // saving in database
        saveAllInstancesFromResponse(response);

    }

    private void saveAllInstancesFromResponse(CotacaoResponse response) {
        List<Cambio> cambioList = new ArrayList<>();
        for (Cotacao cotacao : response.getCotacoes()) {
            Cambio cambio = new Cambio();

            // Since each value from response is USD based(1 USD = X BRL), it is necessary to invert to get the BRL base value
            cambio.setCotacaoCompra(1.0f / cotacao.getCotacaoCompra());
            cambio.setCotacaoVenda(1.0f / cotacao.getCotacaoVenda());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = cotacao.getDataCotacao();
            strDate = strDate.substring(0, 11);

            // setup date in Cambio object
            Date date = null;
            try {
                date = sdf.parse(strDate);
                cambio.setDataCambio(date);
                cambioRepository.save(cambio);

            } catch (Exception e) {
                System.out.println("Date convertion failed");
                System.exit(1);
            }
        }
    }


}
