package br.com.ehsolucoes.appchatandroid.Repository;

import br.com.ehsolucoes.appchatandroid.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Transactional(readOnly = true)
    Users findByEmail(String email);

    Users findByGcmtoken(String gcm);

}
