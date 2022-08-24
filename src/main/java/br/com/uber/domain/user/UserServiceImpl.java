package br.com.uber.domain.user;

import br.com.uber.domain.provider.PersistenceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final PersistenceProvider provider;

  @Override
  public User authenticateUser(UserAuthenticate authenticate) {
    var user = provider.findUserByEmail(authenticate.getEmail());

    if (user == null) {
      throw new BusinessRuleException("E-mail informado não encotrado!");
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    if (!encoder.matches(authenticate.getPassword(), user.getPassword())) {
      throw new BusinessRuleException("Usuário inválido!");
    }

    return user;
  }

  @Override
  public User saveUser(User user) {
    if (provider.existsEmail(user.getEmail())) {
      throw new BusinessRuleException("E-mail ja cadastrado em nossa base de dados!");
    }

    return provider.saveUser(user);
  }
}
