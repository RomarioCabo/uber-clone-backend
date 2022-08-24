package br.com.uber.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

  @Transactional(readOnly = true)
  @Query(value = "select count(ue.id) > 0 from UserEntity ue where ue.email = :email")
  boolean existsEmail(@Param("email") String email);

  @Transactional(readOnly = true)
  @Query(value = "select ue from UserEntity ue where ue.email = :email")
  UserEntity findUserByEmail(@Param("email") String email);
}
