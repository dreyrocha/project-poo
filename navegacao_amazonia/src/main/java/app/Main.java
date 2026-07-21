package app;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import model.*;
import service.*;

/**
 * Classe de demonstração do sistema com interface interativa via terminal.
 * Permite escolher entre Empresa e Passageiro e delega toda a I/O de terminal
 * para ConsoleUI para manter Main focada em fluxo de negócio.
 *
 * As regras de validação (datas, e-mail, horários, capacidade, origem/destino
 * diferentes etc.) agora vivem nas próprias classes de domínio (model/service).
 * Main apenas captura as exceções de validação (IllegalArgumentException) e
 * exibe a mensagem correspondente ao usuário.
 */
public class Main {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args) {
        Local portoA = new Local("Porto de Belém");
        Local portoB = new Local("Porto de Soure");

        Rota rota1 = new Rota("Belém -> Soure", portoA, portoB);
        List<Rota> rotas = new ArrayList<>();
        rotas.add(rota1);

        List<Empresa> empresas = new ArrayList<>();
        Empresa empresaDemo = new Empresa("Navegação Amazônia", "senha123", "contato@navamazonia.com", "contato@navamazonia.com");
        empresas.add(empresaDemo);

        empresaDemo.cadastrarLocal(portoA, false);
        empresaDemo.cadastrarLocal(portoB, false);

        Lancha lancha = new Lancha("Lancha Veloz", 20, 45.5);
        Balsa balsa = new Balsa("Balsa Grande", 200, 15);

        empresaDemo.cadastrarEmbarcacao(lancha, false);
        empresaDemo.cadastrarEmbarcacao(balsa, false);

        Viagem viagem1 = new Viagem(LocalDate.of(2026, 7, 20),
                LocalTime.of(8, 0), LocalTime.of(11, 30), rota1, empresaDemo, lancha);

        Viagem viagem2 = new Viagem(LocalDate.of(2026, 7, 21),
                LocalTime.of(9, 0), LocalTime.of(14, 0), rota1, empresaDemo, balsa);

        empresaDemo.agendarViagem(viagem1, false);
        empresaDemo.agendarViagem(viagem2, false);

        System.out.println(ConsoleUI.destacar("Bem-vindo ao sistema de embarque da Navegação Amazônia."));
        runInterface(empresas, rotas);
    }

    private static void runInterface(List<Empresa> empresas, List<Rota> rotas) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ConsoleUI.mostrarCabecalho("Tela inicial");
            System.out.println("1 - Empresa");
            System.out.println("2 - Passageiro");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = ConsoleUI.lerInt(scanner);
            switch (opcao) {
                case 1:
                    menuEmpresa(empresas, rotas, scanner);
                    break;
                case 2:
                    menuPassageiro(scanner, empresas, rotas);
                    break;
                case 0:
                    System.out.println("Encerrando o sistema. Até logo!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida. Digite 1, 2 ou 0.");
            }
        }
    }

    private static void menuEmpresa(List<Empresa> empresas, List<Rota> rotas, Scanner scanner) {
        while (true) {
            ConsoleUI.mostrarCabecalho("Empresa");
            System.out.println("1 - Login");
            System.out.println("2 - Cadastrar nova empresa");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = ConsoleUI.lerInt(scanner);
            switch (opcao) {
                case 1:
                    Empresa empresaLogada = loginEmpresa(empresas, scanner);
                    if (empresaLogada != null) {
                        menuOperacoesEmpresa(empresaLogada, empresas, rotas, scanner);
                        return;
                    }
                    break;
                case 2:
                    cadastrarNovaEmpresa(empresas, scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Digite 1, 2 ou 0.");
            }
        }
    }

    // Menu de operações para as empresas

    private static void menuOperacoesEmpresa(Empresa empresa, List<Empresa> empresas, List<Rota> rotas, Scanner scanner) {
        while (empresa.isLogada()) {
            ConsoleUI.mostrarCabecalho("Menu Empresa");
            System.out.println("1 - Cadastrar local");
            System.out.println("2 - Cadastrar embarcação");
            System.out.println("3 - Cadastrar rota");
            System.out.println("4 - Agendar viagem");
            System.out.println("5 - Cancelar viagem");
            System.out.println("6 - Atualizar horário de viagem");
            System.out.println("7 - Listar viagens");
            System.out.println("8 - Listar embarcações");
            System.out.println("9 - Ver histórico de acessos");
            System.out.println("10 - Logout");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = ConsoleUI.lerInt(scanner);
            switch (opcao) {
                case 1:
                    cadastrarLocal(empresa, scanner);
                    break;
                case 2:
                    cadastrarEmbarcacao(empresa, scanner);
                    break;
                case 3:
                    cadastrarRota(empresa, rotas, scanner);
                    break;
                case 4:
                    agendarViagem(empresa, empresas, rotas, scanner);
                    break;
                case 5:
                    cancelarViagem(empresa, scanner);
                    break;
                case 6:
                    atualizarHorarioViagem(empresa, scanner);
                    break;
                case 7:
                    ConsoleUI.mostrarViagens(empresa.getViagens());
                    break;
                case 8:
                    ConsoleUI.mostrarEmbarcacoes(empresa.getEmbarcacoes());
                    break;
                case 9:
                    ConsoleUI.mostrarHistorico(empresa.getHistoricoDeAcessos());
                    break;
                case 10:
                    empresa.logout();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Digite um número de 0 a 10.");
            }
        }
    }

    private static void menuPassageiro(Scanner scanner, List<Empresa> empresas, List<Rota> rotas) {
        Passageiro passageiro = new Passageiro();

        while (true) {
            ConsoleUI.mostrarCabecalho("Menu Passageiro");
            System.out.println("1 - Ver viagens disponíveis");
            System.out.println("2 - Filtrar viagens por data");
            System.out.println("3 - Filtrar viagens por rota");
            System.out.println("4 - Ver detalhes da embarcação de uma viagem");
            System.out.println("5 - Ver contato da empresa");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = ConsoleUI.lerInt(scanner);
            List<Viagem> viagensDisponiveis = obterTodasViagens(empresas);
            switch (opcao) {
                case 1:
                    ConsoleUI.mostrarViagens(passageiro.buscarViagem(viagensDisponiveis));
                    break;
                case 2:
                    filtrarPorData(scanner, passageiro, viagensDisponiveis);
                    break;
                case 3:
                    filtrarPorRota(scanner, passageiro, viagensDisponiveis, rotas);
                    break;
                case 4:
                    ConsoleUI.mostrarViagens(viagensDisponiveis);
                    Integer idViagem = ConsoleUI.lerIntComCancelamento(scanner, "Digite o ID da viagem para ver detalhes (ou 0 para cancelar): ");
                    if (idViagem == null) {
                        System.out.println("Operação cancelada.");
                        break;
                    }
                    Viagem viagem = encontrarViagemPorId(viagensDisponiveis, idViagem);
                    if (viagem != null) {
                        passageiro.visualizarDetalhesEmbarcacao(viagem.getEmbarcacao());
                    } else {
                        System.out.println("Viagem não encontrada. Escolha um ID da lista.");
                    }
                    break;
                case 5:
                    if (empresas.isEmpty()) {
                        System.out.println("Nenhuma empresa cadastrada.");
                    } else {
                        ConsoleUI.mostrarEmpresas(empresas);
                        Integer idEmpresa = ConsoleUI.lerIntComCancelamento(scanner, "Digite o ID da empresa para ver o contato (ou 0 para cancelar): ");
                        if (idEmpresa == null) {
                            System.out.println("Operação cancelada.");
                            break;
                        }
                        Empresa empresaSelecionada = encontrarEmpresaPorId(empresas, idEmpresa);
                        if (empresaSelecionada != null) {
                            passageiro.visualizarContatoEmpresa(empresaSelecionada);
                        } else {
                            System.out.println("Empresa não encontrada. Escolha um ID da lista.");
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida. Digite um número de 0 a 5.");
            }
        }
    }

    private static Empresa loginEmpresa(List<Empresa> empresas, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Login da Empresa");
        System.out.println("Digite 0 para voltar.");
        String email = ConsoleUI.lerTextoComCancelamento(scanner, "E-mail da empresa: ");
        if (email == null) {
            System.out.println("Operação cancelada.");
            return null;
        }

        String senha = ConsoleUI.lerTextoComCancelamento(scanner, "Senha da empresa: ");
        if (senha == null) {
            System.out.println("Operação cancelada.");
            return null;
        }

        // Observação: em um terminal real, usar System.console().readPassword() seria mais seguro para senhas.
        for (Empresa empresa : empresas) {
            if (empresa.getEmail() != null && empresa.getEmail().equalsIgnoreCase(email)) {
                if (empresa.login(senha)) {
                    return empresa;
                }
                return null;
            }
        }

        System.out.println("E-mail não encontrado. Verifique o e-mail digitado e tente novamente.");
        return null;
    }

    private static void cadastrarNovaEmpresa(List<Empresa> empresas, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Cadastro de Empresa");

        String nomeEmpresa = ConsoleUI.lerTextoComCancelamento(scanner, "Nome da empresa (ou 0 para cancelar): ");
        if (nomeEmpresa == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        for (Empresa empresa : empresas) {
            if (empresa.getNomeEmpresa().equalsIgnoreCase(nomeEmpresa)) {
                System.out.println("Já existe uma empresa com esse nome. Escolha outro nome.");
                return;
            }
        }

        String email;
        while (true) {
            email = ConsoleUI.lerTextoComCancelamento(scanner, "E-mail da empresa: ");
            if (email == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            if (!Empresa.emailValido(email)) {
                System.out.println("E-mail inválido. Use o formato texto@texto.dominio.");
                continue;
            }
            boolean emailExistente = false;
            for (Empresa empresa : empresas) {
                if (empresa.getEmail().equalsIgnoreCase(email)) {
                    emailExistente = true;
                    break;
                }
            }
            if (emailExistente) {
                System.out.println("Já existe uma empresa com esse e-mail. Escolha outro e-mail.");
                continue;
            }
            break;
        }

        String senha = ConsoleUI.lerTextoComCancelamento(scanner, "Senha: ");
        if (senha == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        String contato = ConsoleUI.lerTextoComCancelamento(scanner, "Contato: ");
        if (contato == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            Empresa novaEmpresa = new Empresa(nomeEmpresa, senha, contato, email);
            empresas.add(novaEmpresa);
            System.out.println("Empresa cadastrada com sucesso! ID: " + novaEmpresa.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar a empresa: " + e.getMessage());
        }
    }

    private static void cadastrarRota(Empresa empresa, List<Rota> rotas, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Cadastro de Rota");
        if (empresa.getLocais().size() < 2) {
            System.out.println("Cadastre pelo menos dois locais antes de criar uma rota.");
            return;
        }

        String nome = ConsoleUI.lerTextoComCancelamento(scanner, "Nome da rota (ou 0 para cancelar): ");
        if (nome == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        ConsoleUI.mostrarLocais(empresa.getLocais());
        Local origem = null;
        while (origem == null) {
            Integer idOrigem = ConsoleUI.lerIntComCancelamento(scanner, "ID do local de origem (ou 0 para cancelar): ");
            if (idOrigem == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            origem = encontrarLocalPorId(empresa.getLocais(), idOrigem);
            if (origem == null) {
                System.out.println("Local de origem inválido. Escolha um ID da lista.");
            }
        }

        Local destino = null;
        while (destino == null) {
            Integer idDestino = ConsoleUI.lerIntComCancelamento(scanner, "ID do local de destino (ou 0 para cancelar): ");
            if (idDestino == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            destino = encontrarLocalPorId(empresa.getLocais(), idDestino);
            if (destino == null) {
                System.out.println("Local de destino inválido. Escolha um ID da lista.");
            }
        }

        // A regra "origem != destino" agora é validada dentro do próprio Rota.
        try {
            Rota novaRota = new Rota(nome, origem, destino);
            rotas.add(novaRota);
            System.out.println("Rota cadastrada com sucesso! ID: " + novaRota.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar a rota: " + e.getMessage());
        }
    }

    private static void cadastrarLocal(Empresa empresa, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Cadastro de Local");
        String nome = ConsoleUI.lerTextoComCancelamento(scanner, "Nome do local (ou 0 para cancelar): ");
        if (nome == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        try {
            Local novoLocal = new Local(nome);
            if (empresa.cadastrarLocal(novoLocal)) {
                System.out.println("Local cadastrado com sucesso! ID: " + novoLocal.getId());
            } else {
                System.out.println("Falha ao cadastrar local. Tente novamente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar o local: " + e.getMessage());
        }
    }

    private static void cadastrarEmbarcacao(Empresa empresa, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Cadastro de Embarcação");
        System.out.println("1 - Lancha");
        System.out.println("2 - Balsa");
        Integer tipo = ConsoleUI.lerIntComCancelamento(scanner, "Tipo de embarcação (ou 0 para cancelar): ");
        if (tipo == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        while (tipo != 1 && tipo != 2) {
            System.out.println("Tipo inválido. Digite 1 para Lancha ou 2 para Balsa.");
            tipo = ConsoleUI.lerIntComCancelamento(scanner, "Tipo de embarcação (ou 0 para cancelar): ");
            if (tipo == null) {
                System.out.println("Operação cancelada.");
                return;
            }
        }

        String nome = ConsoleUI.lerTextoComCancelamento(scanner, "Nome da embarcação (ou 0 para cancelar): ");
        if (nome == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        Integer capacidade = ConsoleUI.lerIntComCancelamento(scanner, "Capacidade de passageiros (ou 0 para cancelar): ");
        if (capacidade == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            Embarcacao embarcacao;
            if (tipo == 1) {
                Double velocidade = ConsoleUI.lerDoubleComCancelamento(scanner, "Velocidade máxima (ou 0 para cancelar): ");
                if (velocidade == null) {
                    System.out.println("Operação cancelada.");
                    return;
                }
                embarcacao = new Lancha(nome, capacidade, velocidade);
            } else {
                Integer capacidadeVeiculos = ConsoleUI.lerIntComCancelamento(scanner, "Capacidade de veículos (ou 0 para cancelar): ");
                if (capacidadeVeiculos == null) {
                    System.out.println("Operação cancelada.");
                    return;
                }
                embarcacao = new Balsa(nome, capacidade, capacidadeVeiculos);
            }

            if (empresa.cadastrarEmbarcacao(embarcacao)) {
                System.out.println("Embarcação cadastrada com sucesso! ID: " + embarcacao.getId());
            } else {
                System.out.println("Falha ao cadastrar embarcação. Tente novamente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar a embarcação: " + e.getMessage());
        }
    }

    private static void agendarViagem(Empresa empresa, List<Empresa> empresas, List<Rota> rotas, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Agendar Viagem");
        if (empresa.getEmbarcacoes().isEmpty()) {
            System.out.println("Nenhuma embarcação cadastrada. Cadastre uma embarcação antes.");
            return;
        }
        if (rotas.isEmpty()) {
            System.out.println("Nenhuma rota cadastrada. Cadastre uma rota antes.");
            return;
        }

        LocalDate data = null;
        while (data == null) {
            String dataStr = ConsoleUI.lerTextoComCancelamento(scanner, "Data (DD-MM-AAAA, ou 0 para cancelar): ");
            if (dataStr == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            try {
                data = LocalDate.parse(dataStr, DATE_FORMATTER);
            } catch (Exception e) {
                System.out.println("Data inválida. Use o formato DD-MM-AAAA, como 20-07-2026.");
            }
        }

        LocalTime saida = null;
        while (saida == null) {
            String saidaStr = ConsoleUI.lerTextoComCancelamento(scanner, "Horário de saída (HH:MM, ou 0 para cancelar): ");
            if (saidaStr == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            try {
                saida = LocalTime.parse(saidaStr);
            } catch (Exception e) {
                System.out.println("Hora de saída inválida. Use o formato HH:MM.");
            }
        }

        LocalTime chegada = null;
        while (chegada == null) {
            String chegadaStr = ConsoleUI.lerTextoComCancelamento(scanner, "Horário de chegada (HH:MM, ou 0 para cancelar): ");
            if (chegadaStr == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            try {
                chegada = LocalTime.parse(chegadaStr);
            } catch (Exception e) {
                System.out.println("Hora de chegada inválida. Use o formato HH:MM.");
            }
        }

        ConsoleUI.mostrarRotas(rotas);
        Rota rota = null;
        while (rota == null) {
            Integer idRota = ConsoleUI.lerIntComCancelamento(scanner, "Escolha o ID da rota (ou 0 para cancelar): ");
            if (idRota == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            rota = encontrarRotaPorId(rotas, idRota);
            if (rota == null) {
                System.out.println("ID de rota inválido. Escolha um ID da lista.");
            }
        }

        ConsoleUI.mostrarEmbarcacoes(empresa.getEmbarcacoes());
        Embarcacao embarcacao = null;
        while (embarcacao == null) {
            Integer idEmbarcacao = ConsoleUI.lerIntComCancelamento(scanner, "Escolha o ID da embarcação (ou 0 para cancelar): ");
            if (idEmbarcacao == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            embarcacao = encontrarEmbarcacaoPorId(empresa.getEmbarcacoes(), idEmbarcacao);
            if (embarcacao == null) {
                System.out.println("ID de embarcação inválido. Escolha um ID da lista.");
            }
        }

        // A validação "chegada após a saída" agora é feita dentro do construtor de Viagem.
        try {
            Viagem viagem = new Viagem(data, saida, chegada, rota, empresa, embarcacao);
            if (empresa.agendarViagem(viagem)) {
                System.out.println("Viagem agendada com sucesso! ID: " + viagem.getId());
            } else {
                System.out.println("Falha ao agendar viagem. Tente novamente.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível agendar a viagem: " + e.getMessage());
        }
    }

    private static void cancelarViagem(Empresa empresa, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Cancelar Viagem");
        ConsoleUI.mostrarViagens(empresa.getViagens());
        Integer id = ConsoleUI.lerIntComCancelamento(scanner, "Digite o ID da viagem para cancelar (ou 0 para cancelar): ");
        if (id == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        Viagem viagem = encontrarViagemPorId(empresa.getViagens(), id);
        if (viagem != null) {
            empresa.cancelarViagem(viagem);
        } else {
            System.out.println("Viagem não encontrada. Escolha um ID da lista.");
        }
    }

    private static void atualizarHorarioViagem(Empresa empresa, Scanner scanner) {
        ConsoleUI.mostrarCabecalho("Atualizar Horário");
        ConsoleUI.mostrarViagens(empresa.getViagens());
        Integer id = ConsoleUI.lerIntComCancelamento(scanner, "Digite o ID da viagem para atualizar (ou 0 para cancelar): ");
        if (id == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        Viagem viagem = encontrarViagemPorId(empresa.getViagens(), id);
        if (viagem == null) {
            System.out.println("Viagem não encontrada. Escolha um ID da lista.");
            return;
        }

        LocalTime saida = null;
        while (saida == null) {
            String saidaStr = ConsoleUI.lerTextoComCancelamento(scanner, "Novo horário de saída (HH:MM, ou 0 para cancelar): ");
            if (saidaStr == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            try {
                saida = LocalTime.parse(saidaStr);
            } catch (Exception e) {
                System.out.println("Hora de saída inválida. Use o formato HH:MM.");
            }
        }

        LocalTime chegada = null;
        while (chegada == null) {
            String chegadaStr = ConsoleUI.lerTextoComCancelamento(scanner, "Novo horário de chegada (HH:MM, ou 0 para cancelar): ");
            if (chegadaStr == null) {
                System.out.println("Operação cancelada.");
                return;
            }
            try {
                chegada = LocalTime.parse(chegadaStr);
            } catch (Exception e) {
                System.out.println("Hora de chegada inválida. Use o formato HH:MM.");
            }
        }

        // Validação "chegada após a saída" é feita dentro de Viagem.atualizarHorario.
        try {
            empresa.atualizarHorarioViagem(viagem, saida, chegada);
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível atualizar o horário: " + e.getMessage());
        }
    }

    private static void filtrarPorRota(Scanner scanner, Passageiro passageiro, List<Viagem> viagensDisponiveis, List<Rota> rotas) {
        if (rotas.isEmpty()) {
            System.out.println("Nenhuma rota cadastrada.");
            return;
        }
        ConsoleUI.mostrarRotas(rotas);
        Integer idRota = ConsoleUI.lerIntComCancelamento(scanner, "Digite o ID da rota para filtrar (ou 0 para cancelar): ");
        if (idRota == null) {
            System.out.println("Operação cancelada.");
            return;
        }
        Rota rotaSelecionada = encontrarRotaPorId(rotas, idRota);
        if (rotaSelecionada == null) {
            System.out.println("Rota não encontrada. Escolha um ID da lista.");
            return;
        }
        System.out.println(ConsoleUI.destacar("Rota selecionada: " + rotaSelecionada.getNome()));
        ConsoleUI.mostrarViagens(passageiro.filtrar(viagensDisponiveis, null, idRota));
    }

    private static void filtrarPorData(Scanner scanner, Passageiro passageiro, List<Viagem> viagensDisponiveis) {
        List<LocalDate> datas = obterDatasDisponiveis(viagensDisponiveis);
        if (datas.isEmpty()) {
            System.out.println("Nenhuma data disponível para filtro.");
            return;
        }
        System.out.println(ConsoleUI.destacar("Datas disponíveis"));
        for (int i = 0; i < datas.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + formatarData(datas.get(i)));
        }

        Integer opcaoData = ConsoleUI.lerIntComCancelamento(scanner, "Digite o número da data para filtrar (ou 0 para ignorar): ");
        if (opcaoData == null) {
            System.out.println("Operação cancelada.");
            return;
        }

        List<Viagem> result = new ArrayList<>(viagensDisponiveis);
        if (opcaoData != 0) {
            if (opcaoData < 1 || opcaoData > datas.size()) {
                System.out.println("Seleção inválida. Escolha um número da lista.");
                return;
            }
            LocalDate dataSelecionada = datas.get(opcaoData - 1);
            result = passageiro.filtrar(result, dataSelecionada, null);
            System.out.println(ConsoleUI.destacar("Data selecionada: " + formatarData(dataSelecionada)));
        }

        ConsoleUI.mostrarViagens(result);
    }

    private static List<LocalDate> obterDatasDisponiveis(List<Viagem> viagens) {
        List<LocalDate> datas = new ArrayList<>();
        for (Viagem viagem : viagens) {
            if (!datas.contains(viagem.getData())) {
                datas.add(viagem.getData());
            }
        }
        datas.sort(Comparator.naturalOrder());
        return datas;
    }

    private static List<Viagem> obterTodasViagens(List<Empresa> empresas) {
        List<Viagem> viagens = new ArrayList<>();
        for (Empresa empresa : empresas) {
            viagens.addAll(empresa.getViagens());
        }
        return viagens;
    }

    private static Viagem encontrarViagemPorId(List<Viagem> viagens, int id) {
        for (Viagem viagem : viagens) {
            if (viagem.getId() == id) {
                return viagem;
            }
        }
        return null;
    }

    private static Embarcacao encontrarEmbarcacaoPorId(List<Embarcacao> embarcacoes, int id) {
        for (Embarcacao embarcacao : embarcacoes) {
            if (embarcacao.getId() == id) {
                return embarcacao;
            }
        }
        return null;
    }

    private static Local encontrarLocalPorId(List<Local> locais, int id) {
        for (Local local : locais) {
            if (local.getId() == id) {
                return local;
            }
        }
        return null;
    }

    private static Rota encontrarRotaPorId(List<Rota> rotas, int id) {
        for (Rota rota : rotas) {
            if (rota.getId() == id) {
                return rota;
            }
        }
        return null;
    }

    private static Empresa encontrarEmpresaPorId(List<Empresa> empresas, int id) {
        for (Empresa empresa : empresas) {
            if (empresa.getId() == id) {
                return empresa;
            }
        }
        return null;
    }

    private static String formatarData(LocalDate data) {
        return data.format(DATE_FORMATTER);
    }
}
