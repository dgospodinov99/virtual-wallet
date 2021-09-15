package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Service
public class DummyServiceImpl implements DummyService {

    @Autowired
    public DummyServiceImpl() {
    }

    @Override
    public boolean depositMoney(LocalDate expDate, double amount) {
        if (expDate.isBefore(LocalDate.now())){
            return false;
        }
        double random = Math.random()*100;
        if(random > 50){
            return true;
        }else {
            return false;
        }
    }
}
