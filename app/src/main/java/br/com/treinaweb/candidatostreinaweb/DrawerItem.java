package br.com.treinaweb.candidatostreinaweb;

/**
 * Created by Edu on 01/02/2016.
 */
public class DrawerItem {
    private String titulo;
    private int icone;

    public DrawerItem() {}

    public DrawerItem(String titulo, int icone) {
        this.titulo = titulo;
        this.icone = icone;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }
}
