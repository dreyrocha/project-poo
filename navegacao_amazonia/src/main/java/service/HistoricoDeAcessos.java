package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDeAcessos {
    private final List<String> registros = new ArrayList<>();

    /**
     * Registra eventos de login/logout apenas dentro do pacote service.
     * O método é package-private porque somente Empresa deve acionar esse histórico.
     */
    void registrar(String evento) {
        registros.add(LocalDateTime.now() + " - " + evento);
    }

    public List<String> getRegistros() {
        return List.copyOf(registros);
    }
}
