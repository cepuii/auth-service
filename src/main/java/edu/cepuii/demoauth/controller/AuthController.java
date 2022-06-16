package edu.cepuii.demoauth.controller;

import edu.cepuii.demoauth.model.ErrorResponse;
import edu.cepuii.demoauth.model.TokenResponse;
import edu.cepuii.demoauth.model.User;
import edu.cepuii.demoauth.service.ClientService;
import edu.cepuii.demoauth.service.TockenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  
  private final ClientService clientService;
  private final TockenService tockenService;
  
  @PostMapping
  public ResponseEntity<String> register(@RequestBody User user) {
    clientService.register(user.getClientId(), user.getClientSecret());
    return ResponseEntity.ok("Registered");
  }
  
  @PostMapping("/token")
  public TokenResponse getToken(@RequestBody User user) {
    clientService.checkCredentials(user.getClientId(), user.getClientSecret());
    return new TokenResponse(tockenService.generateToken(user.getClientId()));
  }
  
  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<ErrorResponse> handleUserRegistrationException(RuntimeException exception) {
    return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
  }
}
