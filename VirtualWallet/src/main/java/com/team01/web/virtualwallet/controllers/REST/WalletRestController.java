package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiOperation(value = "/api/wallets", tags = "Wallet Controller")
@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {

    private final WalletService walletService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public WalletRestController(WalletService walletService, AuthenticationHelper authenticationHelper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
    }

    @ApiOperation(value = "Get All Wallets", response = Iterable.class)
    @GetMapping()
    public List<Wallet> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return walletService.getAll();
    }

    @ApiOperation(value = "Get Wallet by ID", response = Wallet.class)
    @GetMapping("/{id}")
    public Wallet getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return walletService.getById(id);
    }
}
