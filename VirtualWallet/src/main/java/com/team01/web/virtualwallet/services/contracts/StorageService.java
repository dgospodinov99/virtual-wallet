package com.team01.web.virtualwallet.services.contracts;


import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void store(byte[] bytes, String fileName);

    Path load(String filename);

    Resource loadAsResource(String filename);

}