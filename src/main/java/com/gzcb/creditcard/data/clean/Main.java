package com.gzcb.creditcard.data.clean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * @author tangliu
 */
@SpringBootApplication
public class Main {

    private static final String ENGINE_PATH = System.getProperty("worker.base", new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent());

    public static void main(String[] args) throws Exception {

        System.setProperty("worker.base", ENGINE_PATH);
        System.out.println("worker.base = " + ENGINE_PATH);

        SpringApplication.run(Main.class, args);
    }
}
