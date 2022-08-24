package br.com.uber.util;

import br.com.uber.domain.user.TypeUser;
import br.com.uber.domain.user.User;

public class DomainMockUtil {

  public static User buildUser(String email, String password, TypeUser typeUser) {

    return User.builder()
        .name("Mock Name")
        .lastName("Mock Last Name")
        .email(email)
        .password(password)
        .typeUser(typeUser)
        .build();
  }
}
