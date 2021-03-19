package br.com.ehsolucoes.appchatandroid.Resources;

import br.com.ehsolucoes.appchatandroid.DTO.ClienteLogin;
import br.com.ehsolucoes.appchatandroid.DTO.MensagemEnvioDTO;
import br.com.ehsolucoes.appchatandroid.DTO.MensagensDTO;
import br.com.ehsolucoes.appchatandroid.Model.Messages;
import br.com.ehsolucoes.appchatandroid.Model.Users;
import br.com.ehsolucoes.appchatandroid.Service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
@Api(value = "Chat")
public class ChatResource {

    @Autowired
    private ChatService service;
    private final int USER_CREATED_SUCESSFULLY = 0;
    private final int USER_CREATE_FAILED = 1;
    private final int USER_ALREADY_EXISTED = 2;

    @ApiOperation(value = "Mostrar o objeto Users registrado pelo ID")
    @RequestMapping(value = "/registrar/{id}", method = RequestMethod.GET)
    public ResponseEntity<Users> registrarId(@PathVariable Integer id){
        Users obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Mostrar o objeto Users registro pelo Token")
    @RequestMapping(value = "/registrarToken/{gcmtoken}", method = RequestMethod.GET)
    public ResponseEntity<Users> registrarToken(@PathVariable String gcmtoken){
        Users obj = service.getUserGcm(gcmtoken);
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Registrar novo usuário")
    @RequestMapping(value = "/registrar", method = RequestMethod.POST)
    public ResponseEntity<Users>registrar(@RequestBody ClienteLogin obj){
        Users users = new Users();
        users.setName(obj.getNome());
        users.setEmail(obj.getEmail());
        users.setId(null);
        users.setGcmtoken("");
        Integer resposta = service.createUser(users);
        if (resposta == USER_CREATED_SUCESSFULLY) {
            users = service.getUser(obj.getEmail());
            /*URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("registrar/{id}")
                    .buildAndExpand(users.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();*/
            return ResponseEntity.ok().body(users);
        } else {
            if(resposta == USER_CREATE_FAILED) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                users = service.getUser(obj.getEmail());
                /*URI uri = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("registrar/{id}")
                        .buildAndExpand(users.getId())
                        .toUri();
                return ResponseEntity.created(uri).build();*/
                return ResponseEntity.ok().body(users);
            }
        }
    }

    @RequestMapping(value = "/enviar", method = RequestMethod.POST)
    public ResponseEntity<Void> enviar(@RequestBody MensagemEnvioDTO obj){
        Boolean userExists = service.isUserExistsId(obj.getId());

        if(userExists){
            if(service.addMessage(obj.getId(), obj.getMensagem())){
                return ResponseEntity.ok().build();
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/mensagens", method = RequestMethod.GET)
    public ResponseEntity<List<MensagensDTO>> mensagens(){
        List<MensagensDTO> list = service.getMessages();
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Listagem de contatos")
    @RequestMapping(value = "/contatos", method = RequestMethod.GET)
    public ResponseEntity<List<Users>> usuarios(){
        List<Users> list = service.getUsers();
        return ResponseEntity.ok().body(list);
    }

}
