package de.idealo.sso.codingchallenge.restclient.impl.openexchange;

import de.idealo.sso.codingchallenge.exceptions.RestClientException;
import de.idealo.sso.codingchallenge.restclient.RestClient;
import de.idealo.sso.codingchallenge.common.CurrencyEnum;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Calendar;

import static java.math.BigDecimal.ROUND_FLOOR;


@Service("OpenExchange")
public class OpenExchangeRestClient implements RestClient {
    private final static String URL_LAST = "https://openexchangerates.org/api/latest.json?";
    private final static String URL_HISTORY = "https://openexchangerates.org/api/historical/";
    private final static String API_ID = "6d31dc71a6ee49d0ada7cff0181b3628";
    private final static String API_ID_PRM = "app_id=";
    public static final String ERROR_WHILE_GATHERING_INFORMATION = "Error while gathering information from OpenExchange services";

    public BigDecimal getCurrentExchangeRates(CurrencyEnum currencyFrom, CurrencyEnum currencyTo) {
        return getExchangeRates(currencyFrom, currencyTo, getCurrentRates());
    }

    public BigDecimal getHistoricalExchangeRates(CurrencyEnum currencyFrom, CurrencyEnum currencyTo, Calendar calendar) {
        return getExchangeRates(currencyFrom, currencyTo, getHistoricalRates(calendar));
    }

    private BigDecimal getExchangeRates(CurrencyEnum currencyFrom, CurrencyEnum currencyTo, OpenExchangeRatesContainer rates) {
        BigDecimal currencyFromRate = getRates(currencyFrom, rates);
        BigDecimal currencyToRate = getRates(currencyTo, rates);
        return currencyToRate.divide(currencyFromRate, 20, ROUND_FLOOR);
    }

    public OpenExchangeRatesContainer getCurrentRates() {
        return getEntity(URL_LAST, API_ID, OpenExchangeRatesContainer.class);
    }

    public OpenExchangeRatesContainer getHistoricalRates(Calendar date) {
        return getEntity(URL_HISTORY + date.get(Calendar.YEAR) + "-" +
                        getStringWithLeftPadZero(date.get(Calendar.MONTH) + 1) + "-" +
                        getStringWithLeftPadZero(date.get(Calendar.DAY_OF_MONTH)) + ".json?"
                , API_ID, OpenExchangeRatesContainer.class);
    }

    private String getStringWithLeftPadZero(int number) {
        return String.format("%02d", number);
    }

    private static BigDecimal getRates(CurrencyEnum currency, OpenExchangeRatesContainer rates) {
        if (rates.getRates() == null || rates.getRates().get(currency.getCode()) == null) {
            throw new RestClientException(ERROR_WHILE_GATHERING_INFORMATION +
                    (rates.getError() != null ? ", code:" + rates.getStatus() + ", error:" +
                            rates.getError() + " " + rates.getDescription() : ""));
        } else {
            String rate = rates.getRates().get(currency.getCode());
            return new BigDecimal(rate);
        }
    }

    private static <T> T getEntity(String url, String api, Class<T> entityClass) {
        Response response = null;
        try {
            Client client = ClientBuilder.newBuilder().build();
            response = client.target(url + API_ID_PRM + api).request().get();
            return response.readEntity(entityClass);
        } catch (Exception exp) {
            throw new RestClientException(ERROR_WHILE_GATHERING_INFORMATION + ", error: " +
                    (response != null? response.getStatusInfo(): exp.getMessage()));
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }


}
