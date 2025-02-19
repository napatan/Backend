package com.taekwondogym.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.content.fs.config.EnableFilesystemStores;

import java.io.File;

@Configuration
@EnableFilesystemStores
public class SpringContentConfig {

    @Bean
    public File filesystemRoot() {
        return new File("src/main/resources/uploads");
    }
}
