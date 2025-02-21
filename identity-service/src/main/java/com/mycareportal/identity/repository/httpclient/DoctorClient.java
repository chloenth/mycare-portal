package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycareportal.identity.dto.request.DoctorCreationRequest;
import com.mycareportal.identity.dto.response.DoctorResponse;
import com.mycareportal.identity.dto.response.api.ApiResponse;

@FeignClient(name = "doctor-service", url = "${app.services.doctor}")
public interface DoctorClient {
	@PostMapping(value="/internal/users",produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<DoctorResponse> createDoctor(@RequestBody DoctorCreationRequest request);
}
