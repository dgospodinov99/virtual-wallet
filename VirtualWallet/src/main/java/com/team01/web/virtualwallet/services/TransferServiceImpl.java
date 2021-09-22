package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.repositories.contracts.TransferRepository;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final WalletService walletService;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository, WalletService walletService) {
        this.transferRepository = transferRepository;
        this.walletService = walletService;
    }

    @Override
    public List<Transfer> getAll() {
        return transferRepository.getAll();
    }

    @Override
    public List<Transfer> getUserTransfers(User user) {
        return transferRepository.getWalletTransfers(user.getWallet());
    }

    @Override
    public List<Transfer> getUserLatestTransfers(User user) {
        return transferRepository.getLatestWalletTransfers(user.getWallet());
    }

    @Override
    public Transfer getById(int id) {
        return transferRepository.getById(id);
    }

    @Override
    public void create(Transfer transfer) throws IOException {
        String cardNumber = transfer.getCard().getCardNumber();
        String cardCheck = transfer.getCard().getCheckNumber();
        String cardExpDate = transfer.getCard().getExpirationDate().toString();
        String amount = String.valueOf(transfer.getAmount());

        URL url = new URL("http://localhost:8080/dummy");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = String.format
                ("{\"cardNumber\": \"%s\"," +
                " \"cardCheck\": \"%s\"," +
                " \"expirationDate\": \"%s\"," +
                " \"amount\": %s}",cardNumber,cardCheck,cardExpDate,amount);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int statusCode = con.getResponseCode();

        if(statusCode == 200){
            transferRepository.create(transfer);
            walletService.deposit(transfer.getWallet(),transfer.getAmount());
        } else if (statusCode == 400){
            throw new BadLuckException("Unlucky card");
        } else {
            throw new BadLuckException("Expired card");
        }
    }

}
