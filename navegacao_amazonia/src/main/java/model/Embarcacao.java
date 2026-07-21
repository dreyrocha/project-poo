package model;

/**
 * Classe abstrata que representa uma embarcação.
 * Especializa-se em Lancha e Balsa e usa polimorfismo para permitir
 * que cada subclasse descreva suas características específicas.
 */
public abstract class Embarcacao {

    /**
     * ID gerado automaticamente e único entre todas as embarcações.
     * Nunca é reaproveitado mesmo após remoção.
     */
    private static int proximoId = 1;

    private final int id;
    private String nome;
    private int capacidade;

    public Embarcacao(String nome, int capacidade) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da embarcação não pode ser vazio.");
        }
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero.");
        }
        this.id = proximoId++;
        this.nome = nome;
        this.capacidade = capacidade;
    }

    /**
     * ID é definido apenas na criação e nunca pode ser alterado depois,
     * garantindo que o identificador permaneça único e estável.
     */
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da embarcação não pode ser vazio.");
        }
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser maior que zero.");
        }
        this.capacidade = capacidade;
    }

    /**
     * Cada subclasse descreve suas características específicas.
     * Usado de forma polimórfica em toda a aplicação (UI, serviços etc.)
     * para evitar checagens de tipo (instanceof) fora da hierarquia.
     */
    public abstract String descricao();

    @Override
    public String toString() {
        return descricao();
    }
}
