//package com.hms.apigateway.external.service;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient(name = "hms-service", url = "http://localhost:5001")
//public interface ExternalServiecFeignClient {
//	
//	@GetMapping("/hms/login/validate")
//    public boolean validateToken(@RequestParam("token") String token);
//
//}
//
