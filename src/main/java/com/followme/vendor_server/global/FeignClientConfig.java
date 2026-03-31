package com.followMe.vendor_server.global;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/** TODO: 헤더정보 추가, 전파 */
@EnableFeignClients("com.followMe.vendor_server")
@Configuration
public class FeignClientConfig {}
