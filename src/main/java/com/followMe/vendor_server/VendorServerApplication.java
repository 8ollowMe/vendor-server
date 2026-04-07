package com.followMe.vendor_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.followMe")
@EnableJpaRepositories(basePackages = "com.followMe")
@EntityScan(basePackages = "com.followMe")
public class VendorServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(VendorServerApplication.class, args);
  }
}
