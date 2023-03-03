package com.example.demo1.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("javademo")
public interface IJavaDemoClient {
    @RequestMapping(value = "/user/introduce", consumes = "application/json", produces = "application/json", method = POST)
    Object getUserIntroduce();
}
