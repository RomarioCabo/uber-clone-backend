package br.com.uber.infrastructure.persistence.user;

import br.com.uber.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class UserPersistenceManager {

  private final UserRepository repository;

  @Transactional
  public User saveUser(User user) {
    return repository.saveAndFlush(new UserEntity(user)).toModel();
  }

  public boolean existsEmail(String email) {
    return repository.existsEmail(email);
  }

  public User findUserByEmail(String email) {
    var user = repository.findUserByEmail(email);
    return user == null ? null : user.toModel();
  }
}
