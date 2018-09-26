package pl.denzeloff.currencyapp.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyJSON {

    private List<Rates> rates;

    public CurrencyJSON() {
    }

    public List<Rates> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "CurrencyJSON{" +
                "rates=" + rates +
                '}';
    }
}
