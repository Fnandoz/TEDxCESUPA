/*
 * Copyright (c) 2017. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

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
