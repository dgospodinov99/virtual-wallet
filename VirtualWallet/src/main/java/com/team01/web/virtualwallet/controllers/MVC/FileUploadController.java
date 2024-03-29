package com.team01.web.virtualwallet.controllers.MVC;

import com.team01.web.virtualwallet.exceptions.StorageException;
import com.team01.web.virtualwallet.exceptions.StorageFileNotFoundException;
import com.team01.web.virtualwallet.services.contracts.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) throws IOException {
        try {
            String extension = "." + file.getOriginalFilename().split("\\.")[1];
            String filename = session.getAttribute("currentUser") + extension;
            byte[] bytes = file.getBytes();

            storageService.store(bytes, filename);
            session.setAttribute("currentUserImage", filename);
        } catch (StorageException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/myaccount";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
