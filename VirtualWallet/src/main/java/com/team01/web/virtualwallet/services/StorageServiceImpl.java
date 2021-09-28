package com.team01.web.virtualwallet.services;

import com.team01.web.virtualwallet.exceptions.StorageException;
import com.team01.web.virtualwallet.exceptions.StorageFileNotFoundException;
import com.team01.web.virtualwallet.models.User;
import com.team01.web.virtualwallet.services.contracts.StorageService;
import com.team01.web.virtualwallet.services.contracts.UserService;
import com.team01.web.virtualwallet.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;
    private final UserService userService;
    private final StorageProperties storageProperties;

    @Autowired
    public StorageServiceImpl(StorageProperties properties, UserService userService, StorageProperties storageProperties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.userService = userService;
        this.storageProperties = storageProperties;
    }

    @Override
    public void store(byte[] bytes, String fileName) {
        try {
            if (bytes.length == 0) {
                throw new StorageException("Failed to store empty file.");
            }
            String path = storageProperties.getLocation() + fileName;
            Files.write(Paths.get(path), bytes);

            int lastIndexDot = fileName.lastIndexOf('.');

            String username = fileName.substring(0, lastIndexDot);

            User user = userService.getByUsername(username);
            user.setPhotoName(fileName);
            userService.update(user);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }


    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

//    @Override
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//    }
//
//    @Override
//    public void init() {
//        try {
//            Files.createDirectories(rootLocation);
//        }
//        catch (IOException e) {
//            throw new StorageException("Could not initialize storage", e);
//        }
//    }
}