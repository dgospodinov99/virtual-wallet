package com.team01.web.virtualwallet;

import com.team01.web.virtualwallet.services.contracts.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ConfigurationPropertiesScan
public class VirtualWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualWalletApplication.class, args);

    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
