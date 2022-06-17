package edu.cepuii.demoauth.service;

import edu.cepuii.demoauth.dao.ClientEntity;
import edu.cepuii.demoauth.dao.ClientRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultClientService implements ClientService {
  
  private final ClientRepository repository;
  
  @Override
  public void register(String clientId, String clientSecret) {
    if (repository.existsById(clientId)) {
      throw new IllegalArgumentException(
          "Client with id: " + clientId + " already registered");
    }
    String hash = BCrypt.hashpw(clientSecret, BCrypt.gensalt());
    repository.save(new ClientEntity(clientId, hash));
  }
  
  @Override
  public void checkCredentials(String clientId, String clientSecret) {
    Optional<ClientEntity> optionalClientEntity = repository.findById(clientId);
    ClientEntity clientEntity = optionalClientEntity
        .orElseThrow(
            () -> new IllegalArgumentException("Client with id:" + clientId + " not found"));
    if (!BCrypt.checkpw(clientSecret, clientEntity.getHash())) {
      throw new IllegalArgumentException("Secret is incorrect");
    }
  }
  
}
