package pl.denzeloff.currencyapp.UI;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import pl.denzeloff.currencyapp.jsonObj.CurrencyJsonModel;
import pl.denzeloff.currencyapp.jsonObj.Rates;
import pl.denzeloff.currencyapp.model.CurrencyCode;
import pl.denzeloff.currencyapp.service.CurrencyAppService;

import java.time.LocalDate;

@SpringUI
@Theme("valo")
public class CurrencyAppUI extends UI {
    private CurrencyAppService currencyAppService;

    @Autowired
    public CurrencyAppUI(CurrencyAppService currencyAppService) {
        this.currencyAppService = currencyAppService;
    }


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Button getMethodButton = new Button("GET");
        DateField startDate = new DateField("Start Date", LocalDate.now());
        DateField endDate = new DateField("End Date", LocalDate.now());

        ComboBox<CurrencyCode> listOfCurrencyCode = new ComboBox<>("Choose currency code");
        listOfCurrencyCode.setEmptySelectionAllowed(false);
        listOfCurrencyCode.setItems(CurrencyCode.values());

        VerticalLayout currencyCodeLayout = new VerticalLayout(listOfCurrencyCode);
        currencyCodeLayout.setSpacing(true);
        currencyCodeLayout.setMargin(true);

        HorizontalLayout dateLayout = new HorizontalLayout(startDate, endDate);
        dateLayout.setMargin(true);
        dateLayout.setSpacing(true);

        VerticalLayout mainLayout = new VerticalLayout(currencyCodeLayout, dateLayout, getMethodButton);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        setContent(mainLayout);
        getMethodButton.addClickListener(e -> {
            CurrencyJsonModel currencyJsonModel = currencyAppService.getCurrencyJsonObj(startDate.getValue().toString(), endDate.getValue().toString(), listOfCurrencyCode.getValue().toString());
            System.out.println(currencyAppService.listOfBuyingCostCurrency(currencyJsonModel));
        });
    }
}
