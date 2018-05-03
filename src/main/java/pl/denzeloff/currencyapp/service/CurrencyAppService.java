package pl.denzeloff.currencyapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.denzeloff.currencyapp.jsonObj.CurrencyJsonModel;
import pl.denzeloff.currencyapp.jsonObj.Rates;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyAppService {

    public CurrencyJsonModel getCurrencyJsonObj(String code, String startDate, String endDate) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/" + code + "/" + startDate + "/" + endDate + "/?format=json", CurrencyJsonModel.class);
    }

    public List<BigDecimal> listOfBuyingCostCurrency(CurrencyJsonModel currencyJsonModel) {
        Rates rates[] = currencyJsonModel.getRates();
        List<BigDecimal> listOfCurrBuyingCost = new ArrayList<>();
        for (int i = 0; i < rates.length; i++) {
            Rates rateObj = rates[i];
            listOfCurrBuyingCost.add(rateObj.getBid());
        }
        return listOfCurrBuyingCost;
    }

    public BigDecimal averageCurrencyCost(List<BigDecimal> listOfBuyingCostCurrency) {
        BigDecimal average;
        BigDecimal length = new BigDecimal(listOfBuyingCostCurrency.size());
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < listOfBuyingCostCurrency.size(); i++) {
            sum = sum.add(listOfBuyingCostCurrency.get(i));
        }
        average = sum.divide(length, 4, RoundingMode.CEILING);
        return average;
    }


}
