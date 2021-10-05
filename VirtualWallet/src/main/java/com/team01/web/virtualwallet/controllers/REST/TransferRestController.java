package com.team01.web.virtualwallet.controllers.REST;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.controllers.GlobalExceptionHandler;
import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.models.dto.TransferDto;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import io.swagger.annotations.ApiOperation;
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

@ApiOperation(value = "/api/transfers", tags = "Transfers Controller")
@RestController
@RequestMapping("/api/transfers")
public class TransferRestController {

    private static final String DUMMY_END_POINT = "http://localhost:8080/dummy";
    private final TransferService transferService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final TransferModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransferRestController(TransferService transferService,
                                  GlobalExceptionHandler globalExceptionHandler,
                                  TransferModelMapper modelMapper,
                                  AuthenticationHelper authenticationHelper) {
        this.transferService = transferService;
        this.globalExceptionHandler = globalExceptionHandler;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ApiOperation(value = "Get All Transfers", response = Iterable.class)
    @GetMapping
    public List<TransferDto> getAll(@RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return transferService.getAll().stream()
                .map(modelMapper::toDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get Transfer by ID", response = TransferDto.class)
    @GetMapping("/{id}")
    public TransferDto getById(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        authenticationHelper.tryGetAdmin(headers);
        return modelMapper.toDto(transferService.getById(id));
    }

    @ApiOperation(value = "Create a Transfer", response = TransferDto.class)
    @PostMapping
    public TransferDto create(@Valid @RequestBody TransferDto dto, BindingResult result, @RequestHeader HttpHeaders header) {
        globalExceptionHandler.checkValidFields(result);
        User user = authenticationHelper.tryGetUser(header);
        try {
            Transfer transfer = modelMapper.fromDto(dto);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<DummyDto> entity = new HttpEntity<>(modelMapper.transferToDummyDto(transfer), headers);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.exchange(DUMMY_END_POINT, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                transferService.create(transfer, user);
            }
            return modelMapper.toDto(transfer);

        } catch (UnsupportedOperationException | IOException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BadLuckException("Unlucky Card");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new UnauthorizedOperationException("Expired card");
        }
    }
}
