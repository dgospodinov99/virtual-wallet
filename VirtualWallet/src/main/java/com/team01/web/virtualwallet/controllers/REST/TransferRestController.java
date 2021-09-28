package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transfers")
public class TransferRestController {

    private static final String DUMMY_END_POINT = "http://localhost:8080/dummy";

    private final TransferService transferService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final TransferModelMapper modelMapper;

    @Autowired
    public TransferRestController(TransferService transferService, GlobalExceptionHandler globalExceptionHandler, TransferModelMapper modelMapper) {
        this.transferService = transferService;
        this.globalExceptionHandler = globalExceptionHandler;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public List<TransferDto> getAll() {
        return transferService.getAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TransferDto getById(@PathVariable int id) {
        try {
            return modelMapper.toDto(transferService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public TransferDto create(@Valid @RequestBody TransferDto dto, BindingResult result) {
        globalExceptionHandler.checkValidFields(result);
        try {
            Transfer transfer = modelMapper.fromDto(dto);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<DummyDto> entity = new HttpEntity<>(modelMapper.transferToDummyDto(transfer), headers);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.exchange(DUMMY_END_POINT, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                transferService.create(transfer);
            }
            return modelMapper.toDto(transfer);

        } catch (UnsupportedOperationException | IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unlucky Card");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired card");
        }
    }
}
