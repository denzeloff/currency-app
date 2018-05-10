package pl.denzeloff.currencyapp.service;

import com.jidesoft.utils.BigDecimalMathUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.denzeloff.currencyapp.jsonObject.CurrencyJSON;
import pl.denzeloff.currencyapp.jsonObject.Rates;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyAppService {

    public CurrencyJSON getCurrencyJsonObj(String code, String startDate, String endDate) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/" + code + "/" + startDate + "/" + endDate + "/?format=json", CurrencyJSON.class);
    }

    public List<BigDecimal> getListOfBuyingCostCurrency(CurrencyJSON currencyJSON) {
        Rates rates[] = currencyJSON.getRates();
        List<BigDecimal> listOfCurrBuyingCost = new ArrayList<>();
        for (int i = 0; i < rates.length; i++) {
            Rates rateObj = rates[i];
            listOfCurrBuyingCost.add(rateObj.getBid());
        }
        return listOfCurrBuyingCost;
    }

    public BigDecimal calculateAverageValueOfCurrencyPurchase(List<BigDecimal> listOfBuyingCostCurrency) {
        BigDecimal average;
        BigDecimal length = new BigDecimal(listOfBuyingCostCurrency.size());
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < listOfBuyingCostCurrency.size(); i++) {
            sum = sum.add(listOfBuyingCostCurrency.get(i));
        }
        average = sum.divide(length, 4, RoundingMode.CEILING);
        return average;
    }

    public BigDecimal calculateStandardDeviationOfCurrencyPurchaseCost(List<BigDecimal> listOfBuyingCostCurrency) {
        BigDecimal standardDeviation = BigDecimalMathUtils.stddev(listOfBuyingCostCurrency, false, MathContext.UNLIMITED);
        return standardDeviation.setScale(4, RoundingMode.CEILING);
    }


}
