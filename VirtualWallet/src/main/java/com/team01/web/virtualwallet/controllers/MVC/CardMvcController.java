package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.controllers.AuthenticationHelper;
import com.team01.web.virtualwallet.exceptions.AuthenticationFailureException;
import com.team01.web.virtualwallet.exceptions.DuplicateEntityException;
import com.team01.web.virtualwallet.exceptions.EntityNotFoundException;
import com.team01.web.virtualwallet.exceptions.UnauthorizedOperationException;
import com.team01.web.virtualwallet.models.Card;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.models.dto.CardDto;
import com.team01.web.virtualwallet.models.dto.CreateCardDto;
import com.team01.web.virtualwallet.services.contracts.CardService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.services.utils.CardModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("myaccount/cards")
public class CardMvcController {

    private final UserService userService;
    private final CardService cardService;
    private final AuthenticationHelper authenticationHelper;
    private final CardModelMapper modelMapper;

    @Autowired
    public CardMvcController(UserService userService, CardService cardService, AuthenticationHelper authenticationHelper, CardModelMapper modelMapper) {
        this.userService = userService;
        this.cardService = cardService;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            authenticationHelper.tryGetAdmin(session);
            return true;
        } catch (UnauthorizedOperationException e) {
            return false;
        }
    }

    @ModelAttribute("cards")
    public List<CardDto> populateUserCards(HttpSession session) {
        return authenticationHelper.tryGetUser(session).getCards()
                .stream()
                .map(card -> modelMapper.toDto(card))
                .collect(Collectors.toList());
    }

    @GetMapping()
    public String showCards(HttpSession session, Model model) {
        authenticationHelper.tryGetUser(session);
        return "cards";
    }

    @GetMapping("/new")
    public String showNewCardPage(Model model, HttpSession session) {
        authenticationHelper.tryGetUser(session);
        model.addAttribute("card", new CreateCardDto());
        return "card-new";
    }

    @PostMapping("/new")
    public String createCard(
            @Valid @ModelAttribute("card") CreateCardDto dto,
            BindingResult errors,
            Model model,
            HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);

        if (errors.hasErrors()) {
            return "card-new";
        }

        try {
            Card card = modelMapper.fromCreateDto(dto, user);
            cardService.create(card);

            Set<Card> newCardSet = user.getCards();
            newCardSet.add(card);
            user.setCards(newCardSet);
            userService.update(user);

            return "redirect:/myaccount/cards";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("cardNumber", "duplicate_card", e.getMessage());
            return "card-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error404";
        } catch (DateTimeException e) {
            errors.rejectValue("expirationDate", "expirationDate_error", "Please enter a valid date!");
            return "card-new";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditCardPage(@PathVariable int id, Model model, HttpSession session) {

        try {
            User user = authenticationHelper.tryGetUser(session);
        } catch (UnauthorizedOperationException | AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "error401";
        }

        try {
            Card card = cardService.getById(id);
            CreateCardDto dto = modelMapper.toCreateDto(card);
            model.addAttribute("cardId", id);
            model.addAttribute("card", dto);
            return "card-update";
        } catch (EntityNotFoundException | AuthenticationFailureException e) {
            model.addAttribute("errors", e.getMessage());
            return "error404";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCard(@PathVariable int id,
                             @Valid @ModelAttribute("card") CreateCardDto dto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);

        if (errors.hasErrors()) {
            return "card-update";
        }

        try {

            Card card = modelMapper.fromCreateDto(dto, id);
            cardService.update(card, user);

            return "redirect:/myaccount/cards";
        } catch (DateTimeParseException e) {
            errors.rejectValue("expirationDate", "invalid", "Invalid Date!");
            return "card-update";
        } catch (UnauthorizedOperationException e) {
            return "error401";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("cardNumber", "duplicate", e.getMessage());
            return "card-update";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCard(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            cardService.delete(id, user);

            return "redirect:/myaccount/cards";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error404";
        } catch (UnauthorizedOperationException e) {
            return "error401";
        }
    }
}
