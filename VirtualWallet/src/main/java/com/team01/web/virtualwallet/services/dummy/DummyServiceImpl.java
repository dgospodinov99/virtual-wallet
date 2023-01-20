package com.team01.web.virtualwallet.services.dummy;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DummyServiceImpl implements DummyService {

    @Autowired
    public DummyServiceImpl() {
    }

    @Override
    public boolean depositMoney(LocalDate expDate, double amount) {
        if (expDate.isBefore(LocalDate.now())){
            throw new BadLuckException("Expired card");
        }
        double random = Math.random()*100;
//        return random > 50;
        return true;
    }

    @Override
    public boolean withdrawMoney(LocalDate expDate, double amount) {
        if (expDate.isBefore(LocalDate.now())){
            throw new BadLuckException("Expired card");
        }
        double random = Math.random()*100;
        return random > 50;
    }
}
