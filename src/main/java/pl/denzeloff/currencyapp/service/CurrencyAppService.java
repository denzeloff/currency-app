package pl.denzeloff.currencyapp.service;

import com.jidesoft.utils.BigDecimalMathUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.denzeloff.currencyapp.data.CurrencyJSON;
import pl.denzeloff.currencyapp.data.Rates;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CurrencyAppService {

    private final RestTemplate restTemplate;

    public CurrencyAppService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrencyJSON getCurrencyJsonObj(String code, String startDate, String endDate) {
        return restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/C/" + code + "/" + startDate + "/" + endDate + "/?format=json", CurrencyJSON.class);
    }

    public List<BigDecimal> getListOfBuyingCostCurrency(CurrencyJSON currencyJSON) {
        return currencyJSON.getRates().stream().
                map(Rates::getBid).
                collect(Collectors.toList());
    }

    public BigDecimal calculateAverageValueOfCurrencyPurchase(List<BigDecimal> listOfBuyingCostCurrency) {
        return listOfBuyingCostCurrency.stream().
                reduce(BigDecimal.ZERO, BigDecimal::add).
                divide(BigDecimal.valueOf(listOfBuyingCostCurrency.size()), 4, RoundingMode.CEILING);

    }

    public BigDecimal calculateStandardDeviationOfCurrencyPurchaseCost(List<BigDecimal> listOfBuyingCostCurrency) {
        BigDecimal standardDeviation = BigDecimalMathUtils.stddev(listOfBuyingCostCurrency, false, MathContext.DECIMAL32);
        return standardDeviation.setScale(4, RoundingMode.CEILING);
    }
}
