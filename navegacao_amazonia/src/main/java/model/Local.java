package model;

/**
 * Representa um local (porto, terminal, cidade, etc.) que pode ser
 * origem ou destino de uma Rota.
 */
public class Local {

    /**
     * ID gerado automaticamente e único entre todos os locais.
     * Nunca é reaproveitado mesmo após remoção.
     */
    private static int proximoId = 1;

    private final int id;
    private String nome;

    public Local(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do local não pode ser vazio.");
        }
        this.id = proximoId++;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do local não pode ser vazio.");
        }
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Local{id=" + id + ", nome='" + nome + "'}";
    }
}
