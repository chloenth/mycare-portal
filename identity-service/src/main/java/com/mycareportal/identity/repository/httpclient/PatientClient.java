package com.mycareportal.identity.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mycareportal.identity.dto.ApiResponse;
import com.mycareportal.identity.dto.request.PatientCreationRequest;
import com.mycareportal.identity.dto.response.PatientResponse;

@FeignClient(name = "patient-service", url = "${app.services.patient}")
public interface PatientClient {
	@PostMapping(value="/internal/users",produces = MediaType.APPLICATION_JSON_VALUE)
	ApiResponse<PatientResponse> createPatient(@RequestBody PatientCreationRequest request);
}
