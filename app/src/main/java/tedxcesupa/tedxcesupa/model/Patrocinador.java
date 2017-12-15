package tedxcesupa.tedxcesupa.model;

/**
 * Created by fernando on 14/12/2017.
 */

public class Patrocinador {
    String titulo;
    String url;
    String imagem;

    public Patrocinador(String titulo, String url, String imagem) {
        this.titulo = titulo;
        this.url = url;
        this.imagem = imagem;
    }

    public Patrocinador() {
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public String getImagem() {
        return imagem;
    }
}
