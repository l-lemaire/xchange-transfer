package lu.llemaire.xchange_transfer.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lu.llemaire.xchange_transfer.account.AccountService;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class FXRatesAPIService {

    private static final String API_BASE_URL = "https://api.fxratesapi.com/latest";
    private static final String BASE_PARAM = "base";
    private static final String CURRENCIES_PARAM = "currencies";
    private static final String AMOUNT_PARAM = "amount";
    private static final String SUCCESS_PROPERTY = "success";

    /**
     * Method that query FXRatesAPI to know if the currency exists
     *
     * <p>In case of success, the returned object contains a property success set to true</p>
     * <p>Check the README for more information about FXRatesAPI</p>
     *
     * @param currency The currency to check
     * @return True if the currency is valid, false otherwise
     * @throws CurrencyServiceUnavailable Http request failed
     */
    public boolean checkCurrency(final String currency) throws CurrencyServiceUnavailable {
        log.debug("Checking currency");

        //https://api.fxratesapi.com/latest?currencies=EUR&base=EUR
        try {
            return Boolean.TRUE.equals(WebClient.create(API_BASE_URL)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam(BASE_PARAM, currency)
                            .queryParam(CURRENCIES_PARAM, currency)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(Map.class)
                    .blockOptional()
                    .orElseThrow(CurrencyServiceUnavailable::new)
                    .get(SUCCESS_PROPERTY));
        } catch (Exception e) {
            throw new CurrencyServiceUnavailable(e.getMessage());
        }
    }

    public BigDecimal getConvertedAmount(final String fromCurrency, final String toCurrency, final BigDecimal amount) throws CurrencyServiceUnavailable {
        log.debug("Getting converted amount");

        //https://api.fxratesapi.com/latest?currencies=EUR&base=EUR
        try {
            Map<?, ?> response = WebClient.create(API_BASE_URL)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam(BASE_PARAM, fromCurrency)
                            .queryParam(CURRENCIES_PARAM, toCurrency)
                            .queryParam(AMOUNT_PARAM, amount)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(Map.class)
                    .blockOptional()
                    .orElseThrow(CurrencyServiceUnavailable::new);

            if (response.get(SUCCESS_PROPERTY) == null || !(Boolean) response.get(SUCCESS_PROPERTY)) {
                throw new CurrencyServiceUnavailable("Currency conversion failed");
            }

            return amount.multiply(BigDecimal.valueOf((Double) ((Map<?, ?>) response.get("rates")).get(toCurrency)));
        } catch (Exception e) {
            throw new CurrencyServiceUnavailable(e.getMessage());
        }
    }
}
