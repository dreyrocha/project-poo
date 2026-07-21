package service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import model.*;

/**
 * Representa uma Empresa de transporte.
 * Uma Empresa: agenda 0..n Viagens, possui 0..n Embarcações e pode cadastrar Locais.
 */
public class Empresa implements Autenticavel {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    /**
     * ID gerado automaticamente e único entre todas as empresas.
     * Nunca é reaproveitado, mesmo após remoção ou exclusão.
     */
    private static int proximoId = 1;

    private final int id;
    private String nomeEmpresa;
    private String senhaHash;
    private String contato;
    private String email;

    private boolean logada;
    private final HistoricoDeAcessos historico;

    private final List<Viagem> viagens;
    private final List<Embarcacao> embarcacoes;
    private final List<Local> locais;

    /**
     * Cria uma empresa com e-mail obrigatório. O e-mail funciona como chave de login.
     */
    public Empresa(String nomeEmpresa, String senha, String contato, String email) {
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório.");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
        if (!emailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido ou obrigatório para empresa.");
        }
        this.id = proximoId++;
        this.nomeEmpresa = nomeEmpresa;
        this.senhaHash = hash(senha);
        this.contato = contato;
        this.email = email;
        this.logada = false;
        this.historico = new HistoricoDeAcessos();
        this.viagens = new ArrayList<>();
        this.embarcacoes = new ArrayList<>();
        this.locais = new ArrayList<>();
    }

    /**
     * Validação de formato de e-mail centralizada aqui (regra de domínio),
     * em vez de ficar espalhada na camada de UI.
     */
    public static boolean emailValido(String email) {
        return email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches();
    }

    private static String hash(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash indisponível.", e);
        }
    }

    // ---------- Métodos do diagrama ----------

    @Override
    public boolean login(String senha) {
        if (senha != null && this.senhaHash.equals(hash(senha))) {
            this.logada = true;
            historico.registrar("login");
            System.out.println("Empresa " + nomeEmpresa + " autenticada com sucesso.");
            return true;
        }
        historico.registrar("tentativa de login falhou");
        System.out.println("Senha incorreta para a empresa " + nomeEmpresa + ".");
        return false;
    }

    @Override
    public void logout() {
        this.logada = false;
        historico.registrar("logout");
        System.out.println("Empresa " + nomeEmpresa + " deslogada.");
    }

    @Override
    public boolean isLogada() {
        return logada;
    }

    public void atualizarContato(String novoContato) {
        this.contato = novoContato;
        System.out.println("Contato atualizado para: " + novoContato);
    }

    public boolean cadastrarEmbarcacao(Embarcacao embarcacao) {
        return cadastrarEmbarcacao(embarcacao, true);
    }

    public boolean cadastrarEmbarcacao(Embarcacao embarcacao, boolean verbose) {
        if (embarcacao == null) {
            return false;
        }
        embarcacoes.add(embarcacao);
        if (verbose) {
            // Uso polimórfico: descricao() já sabe se é Lancha ou Balsa,
            // sem precisar de instanceof/cast aqui.
            System.out.println("Embarcação cadastrada: " + embarcacao.descricao());
        }
        return true;
    }

    public boolean agendarViagem(Viagem viagem) {
        return agendarViagem(viagem, true);
    }

    public boolean agendarViagem(Viagem viagem, boolean verbose) {
        if (viagem == null) {
            return false;
        }
        viagens.add(viagem);
        if (verbose) {
            System.out.println("Viagem agendada: " + formatarViagem(viagem));
        }
        return true;
    }

    public void cancelarViagem(Viagem viagem) {
        if (viagens.remove(viagem)) {
            System.out.println("Viagem cancelada: id=" + viagem.getId());
        } else {
            System.out.println("Viagem não encontrada para cancelamento.");
        }
    }

    /**
     * Delega a validação de horário para a própria Viagem, que garante
     * atomicamente que chegada é sempre posterior à saída.
     */
    public void atualizarHorarioViagem(Viagem viagem, LocalTime novaSaida, LocalTime novaChegada) {
        viagem.atualizarHorario(novaSaida, novaChegada);
        System.out.println("Horário da viagem " + viagem.getId() + " atualizado para "
                + novaSaida + " - " + novaChegada);
    }

    public boolean cadastrarLocal(Local local) {
        return cadastrarLocal(local, true);
    }

    public boolean cadastrarLocal(Local local, boolean verbose) {
        if (local == null) {
            return false;
        }
        locais.add(local);
        if (verbose) {
            System.out.println("Local cadastrado: " + local.getNome());
        }
        return true;
    }

    // ---------- Getters e Setters ----------

    public int getId() {
        return id;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa não pode ser vazio.");
        }
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getContato() {
        return contato;
    }

    /**
     * Define uma nova senha, já armazenando o hash (nunca o texto puro).
     */
    public void setSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }
        this.senhaHash = hash(senha);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!emailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email;
    }

    /**
     * Retorna uma cópia do histórico de acessos (login/logout).
     */
    public List<String> getHistoricoDeAcessos() {
        return historico.getRegistros();
    }

    /**
     * Devolve uma cópia da lista, para que quem chamar não consiga
     * adicionar/remover viagens sem passar por agendarViagem/cancelarViagem.
     */
    public List<Viagem> getViagens() {
        return List.copyOf(viagens);
    }

    public List<Embarcacao> getEmbarcacoes() {
        return List.copyOf(embarcacoes);
    }

    public List<Local> getLocais() {
        return List.copyOf(locais);
    }

    private String formatarData(LocalDate data) {
        return data.format(DATE_FORMATTER);
    }

    private String formatarViagem(Viagem viagem) {
        return "ID " + viagem.getId() + " | " + formatarData(viagem.getData()) + " | "
                + viagem.getHorarioSaida() + " - " + viagem.getHorarioChegada()
                + " | Rota " + viagem.getRota().getNome()
                + " | Embarcação " + viagem.getEmbarcacao().getNome();
    }

    @Override
    public String toString() {
        return "Empresa{id=" + id + ", nomeEmpresa='" + nomeEmpresa + "', contato='" + contato + "'}";
    }
}
