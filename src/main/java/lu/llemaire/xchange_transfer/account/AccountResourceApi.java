package lu.llemaire.xchange_transfer.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lu.llemaire.xchange_transfer.exceptions.AlreadyExistsException;
import lu.llemaire.xchange_transfer.exceptions.CurrencyServiceUnavailable;
import lu.llemaire.xchange_transfer.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public interface AccountResourceApi {

    @Operation(summary = "Get all accounts", description = "Retrieve a list of all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)))
    })
    @GetMapping
    ResponseEntity<List<Account>> findAll();

    @Operation(summary = "Get account by ID", description = "Retrieve an account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved account",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<Account> findById(@PathVariable @Parameter(description = "ID of the account to be retrieved") final Long id) throws NotFoundException;

    @Operation(summary = "Create a new account", description = "Create a new account with the given details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created account",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "409", description = "Account already exists"),
            @ApiResponse(responseCode = "502", description = "Currency service unavailable")
    })
    @PostMapping
    ResponseEntity<Account> create(
            @RequestParam @NotNull @Parameter(description = "ID of the new account") final Long id,
            @RequestParam @NotNull @Size(min = 3, max = 3) @Parameter(description = "Currency code of the new account") final String currency,
            @RequestParam @NotNull @DecimalMin(value = "0.0") @Parameter(description = "Initial balance of the new account") final BigDecimal balance) throws AlreadyExistsException, CurrencyServiceUnavailable;

    @Operation(summary = "Update an existing account", description = "Update the details of an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated account", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "409", description = "Account already exists"),
            @ApiResponse(responseCode = "502", description = "Currency service unavailable")
    })
    @PutMapping("/{id}")
    ResponseEntity<Account> update(
            @PathVariable @NotNull @Parameter(description = "ID of the account to be updated") final Long id,
            @RequestParam @NotNull @Size(min = 3, max = 3) @Parameter(description = "New currency code of the account") final String currency,
            @RequestParam @NotNull @DecimalMin(value = "0.0") @Parameter(description = "New balance of the account") final BigDecimal balance) throws AlreadyExistsException, CurrencyServiceUnavailable;

    @Operation(summary = "Delete an account", description = "Delete an account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted account")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable @Parameter(description = "ID of the account to be deleted") final Long id);
}