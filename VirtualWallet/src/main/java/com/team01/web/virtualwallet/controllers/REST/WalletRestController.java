package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Wallet;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {

    private final WalletService walletService;

    @Autowired
    public WalletRestController(WalletService walletService) {
        this.walletService = walletService;
    }


    @GetMapping()
    public List<Wallet> getAll() {
        return walletService.getAll();
    }

    @GetMapping("/{id}")
    public Wallet getById(@PathVariable int id) {
        try {
            return walletService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
