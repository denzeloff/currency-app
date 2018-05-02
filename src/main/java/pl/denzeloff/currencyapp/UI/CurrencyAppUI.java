package pl.denzeloff.currencyapp.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.denzeloff.currencyapp.model.CurrencyCode;
import pl.denzeloff.currencyapp.service.CurrencyAppService;

import java.time.LocalDate;

@SpringUI
@Theme("valo")
public class CurrencyAppUI extends UI {
    private CurrencyAppService currencyAppService;
    private DateField startDate = new DateField("Start Date", LocalDate.now());
    private DateField endDate = new DateField("End Date", LocalDate.now());
    private Button getMethodButton = new Button("GET");
    private ComboBox<CurrencyCode> listOfCurrencyCode = new ComboBox<>("Choose currency code");
    private Label averageLabel = new Label();


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

        listOfCurrencyCode.setEmptySelectionAllowed(false);
        listOfCurrencyCode.setItems(CurrencyCode.values());
        listOfCurrencyCode.setSelectedItem(CurrencyCode.valueOf("USD"));

        VerticalLayout currencyCodeLayout = new VerticalLayout(listOfCurrencyCode);
        currencyCodeLayout.setSpacing(true);
        currencyCodeLayout.setMargin(true);

        HorizontalLayout dateLayout = new HorizontalLayout(startDate, endDate);
        dateLayout.setMargin(true);
        dateLayout.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(currencyCodeLayout, dateLayout, getMethodButton, averageLabel);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        setContent(mainLayout);


        getMethodButton.addClickListener(e -> {
            averageLabel.setValue(currencyAppService.averageCurrencyCost(currencyAppService.listOfBuyingCostCurrency(currencyAppService.getCurrencyJsonObj(getListOfCurrencyCode(), getStartDate(), getEndDate()))).toString());
            averageLabel.setVisible(true);
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
