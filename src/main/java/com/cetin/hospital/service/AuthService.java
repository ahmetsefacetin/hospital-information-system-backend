package com.cetin.hospital.service;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.model.Token;
import com.cetin.hospital.repository.TokenRepository;
import com.cetin.hospital.request.DoctorRequest;
import com.cetin.hospital.request.LoginRequest;
import com.cetin.hospital.request.PatientRequest;
import com.cetin.hospital.request.RefreshTokenRequest;
import com.cetin.hospital.response.AuthResponse;
import com.cetin.hospital.security.JwtTokenProvider;
import com.cetin.hospital.security.JwtUserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public AuthService(PatientService patientService, DoctorService doctorService, JwtTokenProvider jwtTokenProvider,
                       AuthenticationManager authenticationManager, TokenRepository tokenRepository,
                       UserDetailsServiceImpl userDetailsService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        if(doAuthenticate(loginRequest)){
            return createToken("user is successfully logged in.", loginRequest.getTC());
        }
        return new ResponseEntity<>(AuthResponse.builder().
                message("Invalid TC or password!!!").build(), HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<AuthResponse> registerPatient(PatientRequest patientRequest) {
        if(patientService.getPatientByTC(patientRequest.getTC()) == null){
            Patient patient = patientService.createPatient(patientRequest);
            LoginRequest loginRequest = LoginRequest.builder()
                    .TC(patientRequest.getTC())
                    .password(patientRequest.getPassword())
                    .build();

            if(doAuthenticate(loginRequest)){
                return createToken("patient is successfully registered.", patient.getTC());
            }
            else{
                return new ResponseEntity<>(AuthResponse.builder().
                        message("authentication is failed after registration").build(), HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(AuthResponse.builder().
                    message("there is already a patient with this TC.").build(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AuthResponse> registerDoctor(DoctorRequest doctorRequest) {
        if(doctorService.getDoctorByTC(doctorRequest.getTC()) == null){
            Doctor doctor = doctorService.createDoctor(doctorRequest);
            LoginRequest loginRequest = LoginRequest.builder()
                    .TC(doctorRequest.getTC())
                    .password(doctorRequest.getPassword())
                    .build();
            JwtUserDetails userDetails = userDetailsService.loadUserByUsername(doctorRequest.getTC());
            System.out.println(userDetails.getUsername() + ", " + userDetails.getPassword());

            if(doAuthenticate(loginRequest)){
                return createToken("doctor is successfully registered.", doctor.getTC());
            }
            else{
                return new ResponseEntity<>(AuthResponse.builder().
                        message("authentication is failed after registration").build(), HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(AuthResponse.builder().
                    message("there is already an doctor with this TC.").build(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<AuthResponse> refresh(RefreshTokenRequest refreshTokenRequest) {
        if(refreshTokenRequest.getUserId() == null) new ResponseEntity<>(AuthResponse.builder().
                message("userId cannot be null.").build(), HttpStatus.BAD_REQUEST);

        Token token = tokenRepository.findByUserId(refreshTokenRequest.getUserId());
        if(token == null){
            return new ResponseEntity<>(AuthResponse.builder().
                    message("there is not a refresh token of this user.").build(), HttpStatus.BAD_REQUEST);
        }

        JwtUserDetails userDetails = userDetailsService.loadUserByUserId(refreshTokenRequest.getUserId());

        if(token.getRefreshToken().equals(refreshTokenRequest.getRefreshToken()) &&
                jwtTokenProvider.validateToken(token.getRefreshToken(), userDetails)){
            return createToken("Token is successfully refreshed.", userDetails.getUsername());
        }
        else{
            return new ResponseEntity<>(AuthResponse.builder().
                    message("Refresh token is not valid.").build(), HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity<AuthResponse> createToken(String message, String TC){

        JwtUserDetails userDetails = userDetailsService.loadUserByUsername(TC);

        String accessToken = jwtTokenProvider.generateToken(userDetails.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        Token token = tokenRepository.findByUserId(userDetails.getId());

        if(token == null){
            token = Token.builder()
                    .refreshToken(refreshToken)
                    .userId(userDetails.getId()).
                    build();
        }
        else{
            token.setRefreshToken(refreshToken);
        }

        tokenRepository.save(token);
        return new ResponseEntity<>(AuthResponse.builder().
                accessToken("Bearer " + accessToken).
                refreshToken(token.getRefreshToken()).
                userId(userDetails.getId()).
                message(message).build(), HttpStatus.CREATED);
    }

    private Boolean doAuthenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getTC(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (AuthenticationException e) {
            logger.error("Exception during authentication: ", e);
            return false;
        }
    }
}
