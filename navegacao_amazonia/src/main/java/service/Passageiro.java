package service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import model.*;

/**
 * Representa um Passageiro do sistema.
 * O Passageiro busca e filtra viagens, e consulta detalhes de embarcações
 * e contato das empresas (associações de dependência/uso com Viagem,
 * Embarcacao e Empresa).
 */
public class Passageiro {

    public Passageiro() {
    }

    /**
     * Busca todas as viagens disponíveis dentre as viagens oferecidas.
     */
    public List<Viagem> buscarViagem(List<Viagem> viagensDisponiveis) {
        System.out.println("Buscando viagens disponíveis...");
        return viagensDisponiveis;
    }

    /**
     * Filtra viagens por data e/ou por ID de rota.
     * Um único método com parâmetros opcionais (nulos) substitui os antigos
     * "filtrar" e "filtrarPorRota", que duplicavam a mesma lógica de filtragem.
     *
     * @param data   data exata a filtrar, ou null para ignorar esse critério.
     * @param idRota id da rota a filtrar, ou null para ignorar esse critério.
     */
    public List<Viagem> filtrar(List<Viagem> viagens, LocalDate data, Integer idRota) {
        System.out.println("Filtrando viagens...");
        return viagens.stream()
                .filter(v -> data == null || v.getData().equals(data))
                .filter(v -> idRota == null || v.getRota().getId() == idRota)
                .collect(Collectors.toList());
    }

    public void visualizarDetalhesEmbarcacao(Embarcacao embarcacao) {
        System.out.println("Detalhes da embarcação:");
        // Uso polimórfico de descricao(): a própria subclasse (Lancha/Balsa)
        // sabe como se descrever, sem precisar de instanceof/cast aqui.
        System.out.println("- " + embarcacao.descricao());
    }

    public void visualizarContatoEmpresa(Empresa empresa) {
        System.out.println("Visualizando contato da empresa " + empresa.getNomeEmpresa()
                + ": " + empresa.getContato());
    }

    @Override
    public String toString() {
        return "Passageiro{}";
    }
}
