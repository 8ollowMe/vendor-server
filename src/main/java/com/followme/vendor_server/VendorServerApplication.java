package com.followme.vendor_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class VendorServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(VendorServerApplication.class, args);
  }
}
