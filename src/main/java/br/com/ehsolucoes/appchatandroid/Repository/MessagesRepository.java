package br.com.ehsolucoes.appchatandroid.Repository;

import br.com.ehsolucoes.appchatandroid.Model.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Integer> {
}
