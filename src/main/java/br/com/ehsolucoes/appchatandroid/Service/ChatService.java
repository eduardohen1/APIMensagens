package br.com.ehsolucoes.appchatandroid.Service;

import br.com.ehsolucoes.appchatandroid.DTO.ClienteLogin;
import br.com.ehsolucoes.appchatandroid.DTO.MensagensDTO;
import br.com.ehsolucoes.appchatandroid.Model.Messages;
import br.com.ehsolucoes.appchatandroid.Model.Users;
import br.com.ehsolucoes.appchatandroid.Repository.MessagesRepository;
import br.com.ehsolucoes.appchatandroid.Repository.UsersRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final int USER_CREATED_SUCESSFULLY = 0;
    private final int USER_CREATE_FAILED = 1;
    private final int USER_ALREADY_EXISTED = 2;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private MessagesRepository messagesRepository;

    public Users find(Integer id){
        Optional<Users> users = usersRepository.findById(id);
        return users.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado!","Tipo: " + Users.class.getName()));
    }

    private boolean isUserExists(String email){
        Users users = usersRepository.findByEmail(email);
        if(users != null)
            return true;
        else
            return false;
    }

    public boolean isUserExistsId(Integer id){
        try{
            Optional<Users> users = usersRepository.findById(id);
            if(users.get().getId() != null)
                return true;
            else
                return false;
        }catch (Exception ex){
            return  false;
        }

    }

    @Transactional
    public int createUser(Users obj){
        if(!isUserExists(obj.getEmail())) {
            obj.setId(null);
            obj.setGcmtoken(stringHexa(gerarHash(obj.getEmail(),"MD5")));
            obj = usersRepository.save(obj);
            if(obj.getId() != null)
                return USER_CREATED_SUCESSFULLY;
            else
                return USER_CREATE_FAILED;
        }else{
            return USER_ALREADY_EXISTED;
        }
    }

    public Users getUser(String email){
        return usersRepository.findByEmail(email);
    }

    public Users getUserGcm(String gcm){
        return usersRepository.findByGcmtoken(gcm);
    }

    public boolean storeGCMToken(Integer id, String token){
        Users newUsers = find(id);
        newUsers.setGcmtoken(token);
        newUsers = usersRepository.save(newUsers);
        if(newUsers.getGcmtoken().equals(token))
            return true;
        else
            return false;
    }

    public boolean addMessage(Integer id, String message){
        Messages newMessage = new Messages(message, id);
        newMessage = messagesRepository.save(newMessage);
        if(newMessage.getId() != null)
            return true;
        else
            return false;
    }

    public List<Users> getUsers(){
        List<Users> users = new ArrayList<>();
        users = usersRepository.findAll();
        return users;
    }

    public List<MensagensDTO> getMessages(){
        List<MensagensDTO> mensagensDTO = new ArrayList<>();
        List<Messages> messages = messagesRepository.findAll();
        for(Messages m : messages){
            Users users = find(m.getUsers_id());
            MensagensDTO mensagensDTO1 = new MensagensDTO();
            mensagensDTO1.setId(m.getId());
            mensagensDTO1.setMessage(m.getMessage());
            mensagensDTO1.setName(users.getName());
            mensagensDTO1.setSentat(m.getSentat());
            mensagensDTO1.setUsers_id(m.getUsers_id());
            mensagensDTO.add(mensagensDTO1);
        }
        return mensagensDTO;
    }

    public static byte[] gerarHash(String frase, String algoritmo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0) s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }
}
