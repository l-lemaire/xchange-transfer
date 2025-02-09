package lu.llemaire.xchange_transfer.transaction;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.InsufficientBalanceException;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public interface TransactionResourceApi {

    @Operation(summary = "Create a new transaction", description = "Create a new transaction with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created transaction"),
            @ApiResponse(responseCode = "400", description = "Insufficient balance"),
            @ApiResponse(responseCode = "400", description = "Account not found"),
            @ApiResponse(responseCode = "502", description = "Currency service unavailable")

    })
    @PostMapping
    void create(@RequestParam @Parameter(description = "ID of the account initiating the transfer") @NotNull final Long sourceAccountId,
                @RequestParam @Parameter(description = "ID of the account receiving the transfer") @NotNull final Long targetAccountId,
                @RequestParam @Parameter(description = "The amount transferred in the initiator currency") @NotNull @DecimalMin(value = "0.0") final BigDecimal amount) throws InsufficientBalanceException, NotFoundException, CurrencyServiceUnavailable;
}
