package br.com.ehsolucoes.appchatandroid.DTO;

import java.io.Serializable;

public class MensagemEnvioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String mensagem;

    public MensagemEnvioDTO(){}

    public MensagemEnvioDTO(Integer id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
