package com.trinhducthang.identityservice.service.impl;



import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.trinhducthang.identityservice.dto.response.AuthenticationResponse;
import com.trinhducthang.identityservice.dto.response.IntrospectResponse;
import com.trinhducthang.identityservice.model.User;
import com.trinhducthang.identityservice.repository.UserRepository;
import com.trinhducthang.identityservice.request.AuthenticationRequest;
import com.trinhducthang.identityservice.request.IntrospectRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository usersRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SINGER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verify = signedJWT.verify(jwsVerifier);

        String str;

        if(verify){
            str = "OK";
        }
        else{
            str = "NOT";
        }

        return IntrospectResponse.builder()
                .valid(
                        verify && expityTime.after(new Date()))
                .message(str)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("user not found"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(),user.getPassword());

        if(!authenticated)
            throw new RuntimeException("KHONG HOP LE!");

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();


    }



    private String generateToken(User users){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(users.getUsername())
                .issuer("ducthang")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",users.getRole())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes()));
            return jwsObject.serialize();
        }
        catch (JOSEException e){
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

}