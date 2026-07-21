package test;

import java.time.LocalDate;
import java.time.LocalTime;

import model.Lancha;
import model.Local;
import model.Rota;
import model.Viagem;
import service.Empresa;

public class ViagemIdTest {
    public static void main(String[] args) {
        Empresa empresa1 = new Empresa("Empresa A", "senha", "contato", "empresaA@teste.com");
        Empresa empresa2 = new Empresa("Empresa B", "senha", "contato", "empresaB@teste.com");

        Rota rota = new Rota("Belém -> Soure", new Local("Belém"), new Local("Soure"));
        Lancha embarcacao = new Lancha("Lancha", 20, 45.5);

        Viagem viagem1 = new Viagem(LocalDate.of(2026, 7, 20), LocalTime.of(8, 0), LocalTime.of(10, 0), rota, empresa1, embarcacao);
        Viagem viagem2 = new Viagem(LocalDate.of(2026, 7, 21), LocalTime.of(9, 0), LocalTime.of(11, 0), rota, empresa2, embarcacao);

        empresa1.agendarViagem(viagem1, false);
        empresa2.agendarViagem(viagem2, false);

        if (viagem1.getId() <= 0) {
            throw new AssertionError("ID da primeira viagem deve ser maior que zero.");
        }
        if (viagem1.getId() == viagem2.getId()) {
            throw new AssertionError("IDs das viagens devem ser únicos globalmente.");
        }
        if (viagem2.getId() != viagem1.getId() + 1) {
            throw new AssertionError("Esperava o contador de IDs incrementar sequencialmente. viagem1=" + viagem1.getId() + ", viagem2=" + viagem2.getId());
        }

        System.out.println("Teste OK: viagem1.id=" + viagem1.getId() + ", viagem2.id=" + viagem2.getId());
    }
}
