package model;

import java.time.LocalDate;
import java.time.LocalTime;

import service.Empresa;

/**
 * Representa uma Viagem, associada a 1 Rota, agendada por 1 Empresa
 * e realizada por 1 Embarcação.
 */
public class Viagem {

    /**
     * ID gerado automaticamente e único entre todas as viagens.
     * Nunca é reaproveitado mesmo após cancelamento.
     */
    private static int proximoId = 1;

    private final int id;
    private LocalDate data;
    private LocalTime horarioSaida;
    private LocalTime horarioChegada;

    private final Rota rota;
    private final Empresa empresa;
    private final Embarcacao embarcacao;

    public Viagem(LocalDate data, LocalTime horarioSaida, LocalTime horarioChegada,
                  Rota rota, Empresa empresa, Embarcacao embarcacao) {
        if (data == null) {
            throw new IllegalArgumentException("Data da viagem é obrigatória.");
        }
        if (rota == null || empresa == null || embarcacao == null) {
            throw new IllegalArgumentException("Rota, empresa e embarcação são obrigatórias.");
        }
        validarHorarios(horarioSaida, horarioChegada);

        this.id = proximoId++;
        this.data = data;
        this.horarioSaida = horarioSaida;
        this.horarioChegada = horarioChegada;
        this.rota = rota;
        this.empresa = empresa;
        this.embarcacao = embarcacao;
    }

    private static void validarHorarios(LocalTime saida, LocalTime chegada) {
        if (saida == null || chegada == null) {
            throw new IllegalArgumentException("Horários de saída e chegada são obrigatórios.");
        }
        if (!chegada.isAfter(saida)) {
            throw new IllegalArgumentException("Horário de chegada deve ser posterior ao de saída.");
        }
    }

    public int getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data da viagem é obrigatória.");
        }
        this.data = data;
    }

    public LocalTime getHorarioSaida() {
        return horarioSaida;
    }

    public LocalTime getHorarioChegada() {
        return horarioChegada;
    }

    /**
     * Atualiza saída e chegada juntas, garantindo que a regra
     * "chegada após a saída" nunca seja violada por uma atualização parcial.
     */
    public void atualizarHorario(LocalTime novoHorarioSaida, LocalTime novoHorarioChegada) {
        validarHorarios(novoHorarioSaida, novoHorarioChegada);
        this.horarioSaida = novoHorarioSaida;
        this.horarioChegada = novoHorarioChegada;
    }

    public Rota getRota() {
        return rota;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Embarcacao getEmbarcacao() {
        return embarcacao;
    }

    @Override
    public String toString() {
        return "Viagem{id=" + id + ", data=" + data + ", saida=" + horarioSaida
                + ", chegada=" + horarioChegada + ", rota=" + rota.getNome()
                + ", empresa=" + empresa.getNomeEmpresa() + ", embarcacao=" + embarcacao.getNome() + "}";
    }
}
