package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.BadLuckException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.Transfer;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.DepositDto;
import com.team01.web.virtualwallet.models.dto.DummyDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.TransferService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.TransferModelMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/myaccount/deposit")
public class DepositMvcController {

    private static final String DUMMY_END_POINT = "http://localhost:8080/dummy";

    private final UserService userService;
    private final TransferModelMapper transferModelMapper;
    private final TransferService transferService;
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;

    public DepositMvcController(UserService userService, TransferModelMapper transferModelMapper, TransferService transferService, CardService cardService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.transferModelMapper = transferModelMapper;
        this.transferService = transferService;
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping()
    public String showDeposit(Model model) {
        model.addAttribute("deposit", new DepositDto());
        return "deposit";
    }

    @ModelAttribute("user")
    public User populateUser(HttpSession session) {
        try {
            return authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @ModelAttribute("cards")
    public Set<Card> populateCards(HttpSession session) {
        return populateUser(session).getCards();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @PostMapping()
    public String handleDeposit(@Valid @ModelAttribute("deposit") DepositDto dto,
                                BindingResult bindingResult,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Transfer transfer = transferModelMapper.fromDto(dto, user.getWallet());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<DummyDto> entity = new HttpEntity<>(transferModelMapper.transferToDummyDto(transfer), headers);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.exchange(DUMMY_END_POINT, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                transferService.create(transfer);
            }

            return "redirect:/";
        } catch (BadLuckException | IOException e) {
            bindingResult.rejectValue("amount", "auth_error", e.getMessage());
            return "deposit";
        } catch (HttpClientErrorException.Unauthorized e) {
            bindingResult.rejectValue("cardId", "card_error", "Your card is expired!");
            return "deposit";
        } catch (HttpClientErrorException.BadRequest e) {
            bindingResult.rejectValue("amount", "auth_error", "r/therewasanattempt");
            return "deposit";
        }
    }

}
