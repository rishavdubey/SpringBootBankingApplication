package com.example.transactions.external;

import com.example.transactions.configuration.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.transactions.model.external.Account;
import com.example.transactions.model.response.Response;

@FeignClient(name = "account-service", configuration = FeignClientConfiguration.class)
public interface AccountService {

    /**
     * Retrieves an account by account number.
     *
     * @param accountNumber The account number.
     * @return The account matching the account number.
     */
    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);

    /**
     * Update an account with the given account number.
     *
     * @param accountNumber The account number of the account to be updated.
     * @param account The updated account information.
     * @return The response entity containing the response.
     */
    @PutMapping("/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}
