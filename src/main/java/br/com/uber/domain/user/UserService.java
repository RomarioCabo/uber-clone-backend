package br.com.uber.domain.user;

public interface UserService {

  User authenticateUser(UserAuthenticate authenticate);

  User saveUser(User user);
}
