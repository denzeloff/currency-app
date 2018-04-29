package pl.denzeloff.currencyapp.model;

public enum CurrencyCode {
    USD("USD"), EUR("EUR"), CHF("CHF"), GBP("GBP");
    private final String code;

    CurrencyCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
