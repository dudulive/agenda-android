package br.com.treinaweb.candidatostreinaweb.dao;

/**
 * Created by Edu on 01/02/2016.
 */

public class User {
    private long id;
    private String name;
    private String email;
    private String token;
    private byte[] avatar;
    private byte[] img_perfil;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getImgPerfil() {
        return img_perfil;
    }

    public void setImgPerfil(byte[] img_perfil) {
        this.img_perfil = img_perfil;
    }
}
