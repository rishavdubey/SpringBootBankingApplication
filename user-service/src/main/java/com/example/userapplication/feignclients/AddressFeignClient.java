package com.example.userapplication.feignclients;

import com.example.userapplication.response.AddressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "api-gateway")
public interface AddressFeignClient {

	@GetMapping("/address-service/api/address/{id}")
	public AddressResponse getById(@PathVariable long id);
	
}
