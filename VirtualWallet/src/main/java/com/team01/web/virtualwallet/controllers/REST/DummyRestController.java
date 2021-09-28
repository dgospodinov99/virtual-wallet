package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.services.contracts.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> depositMoney(@RequestBody DummyDto dto){
        try {
            if (dummyService.depositMoney(stringToLocalDate(dto.getExpirationDate()), dto.getAmount())) {
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (BadLuckException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }

    private LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth localDate = YearMonth.parse(date,formatter);
        return localDate.atEndOfMonth();
    }
}
