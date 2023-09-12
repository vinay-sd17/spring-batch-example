package com.javatechie.spring.batch.client;

import com.javatechie.spring.batch.entity.RuleDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "jplaceholder", url = "https://aa6b2fdb-a9af-44d8-b885-1555ec8f43ce.mock.pstmn.io/")
public interface RuleDetailsFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/rule", produces = "application/json")
    RuleDetails getRule();
}