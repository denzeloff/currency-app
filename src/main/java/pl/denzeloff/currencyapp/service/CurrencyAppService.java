package pl.denzeloff.currencyapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.denzeloff.currencyapp.jsonObj.CurrencyJsonModel;

@Service
public class CurrencyAppService {
    public CurrencyJsonModel getCurrencyJsonObj(String startDate, String endDate, String code) {
        RestTemplate restTemplate = new RestTemplate();
        CurrencyJsonModel currencyJsonModel = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/" + code + "/" + startDate + "/" + endDate + "/?format=json", CurrencyJsonModel.class);
        return currencyJsonModel;
    }
}
