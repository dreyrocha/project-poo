package app;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import model.Embarcacao;
import model.Local;
import model.Rota;
import model.Viagem;
import service.Empresa;

/**
 * Responsabilidade única: ler entrada do usuário e exibir dados no terminal.
 * Essa classe separa a lógica de I/O da lógica de negócio de Main.
 */
public class ConsoleUI {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String RESET = "\u001B[0m";
    private static final String CYAN_BOLD = "\u001B[1;36m";

    public static int lerInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Digite um número válido: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static Integer lerIntComCancelamento(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase("0") || entrada.equalsIgnoreCase("cancelar")) {
                return null;
            }
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro ou 0 para cancelar.");
            }
        }
    }

    public static Double lerDoubleComCancelamento(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase("0") || entrada.equalsIgnoreCase("cancelar")) {
                return null;
            }
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número decimal ou 0 para cancelar.");
            }
        }
    }

    public static String lerTextoComCancelamento(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.equalsIgnoreCase("0") || entrada.equalsIgnoreCase("cancelar")) {
            return null;
        }
        return entrada;
    }

    public static void mostrarViagens(List<Viagem> viagens) {
        if (viagens.isEmpty()) {
            System.out.println("Nenhuma viagem encontrada.");
            return;
        }
        System.out.println(destacar("Viagens disponíveis"));
        int index = 1;
        for (Viagem viagem : viagens) {
            System.out.println();
            System.out.println("[" + index++ + "] " + destacar("ID " + viagem.getId()));
            System.out.println("   Data.......: " + formatarData(viagem.getData()));
            System.out.println("   Saída......: " + viagem.getHorarioSaida());
            System.out.println("   Chegada....: " + viagem.getHorarioChegada());
            System.out.println("   Rota.......: " + destacar(viagem.getRota().getNome()));
            System.out.println("   Embarcação.: " + viagem.getEmbarcacao().getNome());
            System.out.println("   Empresa....: " + viagem.getEmpresa().getNomeEmpresa());
        }
        System.out.println();
    }

    public static void mostrarEmbarcacoes(List<Embarcacao> embarcacoes) {
        if (embarcacoes.isEmpty()) {
            System.out.println("Nenhuma embarcação cadastrada.");
            return;
        }
        System.out.println(destacar("Embarcações disponíveis"));
        for (Embarcacao embarcacao : embarcacoes) {
            // Uso polimórfico: descricao() já traz os dados específicos
            // de Lancha ou Balsa, sem checagem de tipo aqui.
            System.out.println("[ID " + embarcacao.getId() + "] " + embarcacao.descricao());
        }
    }

    public static void mostrarEmpresas(List<Empresa> empresas) {
        System.out.println(destacar("Empresas disponíveis"));
        for (Empresa empresa : empresas) {
            System.out.println("[ID " + empresa.getId() + "] " + empresa.getNomeEmpresa() + " - " + empresa.getEmail());
        }
    }

    public static void mostrarLocais(List<Local> locais) {
        System.out.println(destacar("Locais disponíveis"));
        for (Local local : locais) {
            System.out.println("[ID " + local.getId() + "] " + local.getNome());
        }
    }

    public static void mostrarRotas(List<Rota> rotas) {
        if (rotas.isEmpty()) {
            System.out.println("Nenhuma rota cadastrada.");
            return;
        }
        System.out.println(destacar("Rotas disponíveis"));
        for (Rota rota : rotas) {
            System.out.println("[ID " + rota.getId() + "] " + destacar(rota.getNome())
                    + " (" + rota.getOrigem().getNome() + " → " + rota.getDestino().getNome() + ")");
        }
    }

    public static void mostrarHistorico(List<String> registros) {
        if (registros.isEmpty()) {
            System.out.println("Nenhum acesso registrado ainda.");
            return;
        }
        System.out.println(destacar("Histórico de acessos"));
        for (String registro : registros) {
            System.out.println("- " + registro);
        }
    }

    public static void mostrarCabecalho(String titulo) {
        System.out.println();
        System.out.println(destacar("== " + titulo + " =="));
    }

    public static String destacar(String texto) {
        return CYAN_BOLD + texto + RESET;
    }

    private static String formatarData(LocalDate data) {
        return data.format(DATE_FORMATTER);
    }
}
