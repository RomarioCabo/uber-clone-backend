package br.com.uber.infrastructure.persistence.user;

import br.com.uber.domain.user.TypeUser;
import br.com.uber.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_app", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "type", nullable = false)
  @Convert(converter = EnumTypeUserAttributeConverter.class)
  private TypeUser typeUser;

  public UserEntity(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.password = encoderPassword(user.getPassword());
    this.typeUser = user.getTypeUser();
  }

  public User toModel() {
    return User.builder()
        .id(this.id)
        .name(this.name)
        .lastName(this.lastName)
        .email(this.email)
        .password(this.password)
        .typeUser(this.typeUser)
        .build();
  }

  private String encoderPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.encode(password);
  }
}
