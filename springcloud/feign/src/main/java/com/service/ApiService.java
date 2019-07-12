package com.service;

import com.service.Impl.ApiServiceError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "eurekaclient",fallback = ApiServiceError.class)
public interface ApiService {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index();
}
