package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/dummy")
public class DummyRestController {

    private final DummyService dummyService;

    @Autowired
    public DummyRestController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @PostMapping
    public void depositMoney(@RequestBody DummyDto dto){
        try {
            if (dummyService.depositMoney(stringToLocalDate(dto.getExpirationDate()), dto.getAmount())) {
                throw new ResponseStatusException(HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } catch (BadLuckException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    private LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth localDate = YearMonth.parse(date,formatter);
        return localDate.atEndOfMonth();
    }
}
