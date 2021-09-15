package com.team01.web.virtualwallet.services.contracts;

import com.team01.web.virtualwallet.models.dto.DummyDto;

import java.time.LocalDate;

public interface DummyService {

    boolean depositMoney(LocalDate expDate, double amount);
}
