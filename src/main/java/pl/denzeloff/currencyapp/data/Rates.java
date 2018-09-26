package pl.denzeloff.currencyapp.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rates {

    private BigDecimal bid;

    public Rates() {
    }

    public BigDecimal getBid() {
        return bid;
    }

    @Override
    public String toString() {
        return "Rates{" +
                "bid=" + bid +
                '}';
    }
}
