package com.trinhducthang.identityservice.controller;



import com.nimbusds.jose.JOSEException;
import com.trinhducthang.identityservice.dto.response.ApiResponse;
import com.trinhducthang.identityservice.dto.response.AuthenticationResponse;
import com.trinhducthang.identityservice.dto.response.IntrospectResponse;
import com.trinhducthang.identityservice.request.AuthenticationRequest;
import com.trinhducthang.identityservice.request.IntrospectRequest;
import com.trinhducthang.identityservice.service.impl.AuthenticationService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;


    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            // Call the authentication service to authenticate the request
            var result = authenticationService.authenticate(request);

            // Build ApiResponse with result
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Authentication successful")
                    .result(result)
                    .build();
        } catch (RuntimeException e) {
            // Handle any runtime exceptions
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Authentication failed: " + e.getMessage())
                    .build();
        }
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        try {
            var result = authenticationService.introspect(request);
            return ApiResponse.<IntrospectResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(result)
                    .build();
        }
        catch (RuntimeException e){

            return ApiResponse.<IntrospectResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }


}
