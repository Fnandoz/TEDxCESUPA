package tedxcesupa.tedxcesupa;

/**
 * Created by fernando on 19/11/2017.
 */

public class Palestrante {
    int foto;
    String nome;
    String descricao;
    int estrelas;

    public Palestrante(int foto, String nome) {
        this.foto = foto;
        this.nome = nome;
    }

    public Palestrante(int foto, String nome, String descricao, int estrelas) {
        this.foto = foto;
        this.nome = nome;
        this.descricao = descricao;
        this.estrelas = estrelas;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(int estrelas) {
        this.estrelas = estrelas;
    }
}
