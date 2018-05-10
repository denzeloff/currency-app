package pl.denzeloff.currencyapp.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import pl.denzeloff.currencyapp.jsonObject.CurrencyJSON;
import pl.denzeloff.currencyapp.model.CurrencyCode;
import pl.denzeloff.currencyapp.service.CurrencyAppService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import static java.time.temporal.ChronoUnit.DAYS;

@SpringUI
@Theme("valo")
public class CurrencyAppUI extends UI {
    private CurrencyAppService currencyAppService;
    private DateField startDate = new DateField("Start Date", LocalDate.now());
    private DateField endDate = new DateField("End Date", LocalDate.now());
    private Button getMethodButton = new Button("GET");
    private ComboBox<CurrencyCode> listOfCurrencyCode = new ComboBox<>("Choose currency code");
    private Label averageLabel = new Label();
    private Label standardDeviationLabel = new Label();


    @Autowired
    public CurrencyAppUI(CurrencyAppService currencyAppService) {
        this.currencyAppService = currencyAppService;
    }

    public CurrencyAppUI() {
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        averageLabel.setVisible(false);
        averageLabel.setCaption("Average");
        standardDeviationLabel.setVisible(false);
        standardDeviationLabel.setCaption("Standard deviation");

        listOfCurrencyCode.setEmptySelectionAllowed(false);
        listOfCurrencyCode.setItems(CurrencyCode.values());
        listOfCurrencyCode.setSelectedItem(CurrencyCode.valueOf("USD"));

        VerticalLayout currencyCodeLayout = new VerticalLayout(listOfCurrencyCode);
        currencyCodeLayout.setSpacing(true);
        currencyCodeLayout.setMargin(true);

        HorizontalLayout dateLayout = new HorizontalLayout(startDate, endDate);
        dateLayout.setMargin(true);
        dateLayout.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(currencyCodeLayout, dateLayout, getMethodButton, averageLabel, standardDeviationLabel);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        setContent(mainLayout);

        startDate.setRangeStart(LocalDate.of(2001, 12, 31));
        startDate.setRangeEnd(LocalDate.now());
        endDate.setRangeStart(LocalDate.of(2001, 12, 31));
        endDate.setRangeEnd(LocalDate.now());

        startDate.addValueChangeListener(e -> {
            startDate.setRangeEnd(endDate.getValue());
            endDate.setRangeStart(startDate.getValue());
            long daysBetween = DAYS.between(startDate.getValue(), endDate.getValue());
            if (daysBetween > 367) {
                Notification.show("Days range must be smaller then 367 days", Notification.Type.WARNING_MESSAGE);
            }

        });
        endDate.addValueChangeListener(e -> {
            endDate.setRangeStart(startDate.getValue());
            startDate.setRangeEnd(endDate.getValue());
            long daysBetween = DAYS.between(startDate.getValue(), endDate.getValue());
            if (daysBetween > 367) {
                Notification.show("Days range must be smaller then 367 days", Notification.Type.WARNING_MESSAGE);
            }
        });

        getMethodButton.addClickListener(e -> {
            try {
                CurrencyJSON currencyJSON = currencyAppService.getCurrencyJsonObj(getListOfCurrencyCode(), getStartDate(), getEndDate());
                List<BigDecimal> listOfBuyingCostCurrency = currencyAppService.getListOfBuyingCostCurrency(currencyJSON);
                BigDecimal averageValueOfBuyingCostCurrency = currencyAppService.calculateAverageValueOfCurrencyPurchase(listOfBuyingCostCurrency);
                BigDecimal standardDeviationOfBuyingCostCurrency = currencyAppService.calculateStandardDeviationOfCurrencyPurchaseCost(listOfBuyingCostCurrency);
                averageLabel.setValue(averageValueOfBuyingCostCurrency.toString());
                averageLabel.setVisible(true);
                standardDeviationLabel.setValue(standardDeviationOfBuyingCostCurrency.toString());
                standardDeviationLabel.setVisible(true);
            } catch (HttpClientErrorException e1) {
                if (HttpStatus.NOT_FOUND.equals(e1.getStatusCode()))
                    Notification.show("Sorry, but we don't have data for selected range", Notification.Type.WARNING_MESSAGE);
                else if (HttpStatus.BAD_REQUEST.equals(e1.getStatusCode()))
                    Notification.show("Please choose correct range", Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private String getStartDate() {
        return startDate.getValue().toString();
    }

    private String getEndDate() {
        return endDate.getValue().toString();
    }

    private String getListOfCurrencyCode() {
        return listOfCurrencyCode.getValue().toString();
    }
}
