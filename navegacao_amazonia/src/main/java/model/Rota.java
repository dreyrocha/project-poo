package model;

/**
 * Representa uma Rota, que liga um Local de origem a um Local de destino.
 * Cada Rota possui exatamente 1 origem e 1 destino (associações com Local).
 * Um mesmo Local pode ser origem/destino de 1..n Rotas.
 */
public class Rota {

    /**
     * ID gerado automaticamente e único entre todas as rotas.
     * Nunca é reaproveitado mesmo após remoção.
     */
    private static int proximoId = 1;

    private final int id;
    private String nome;
    private Local origem;
    private Local destino;

    public Rota(String nome, Local origem, Local destino) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da rota não pode ser vazio.");
        }
        if (origem == null || destino == null) {
            throw new IllegalArgumentException("Origem e destino são obrigatórios.");
        }
        if (origem.getId() == destino.getId()) {
            throw new IllegalArgumentException("Origem e destino devem ser locais diferentes.");
        }
        this.id = proximoId++;
        this.nome = nome;
        this.origem = origem;
        this.destino = destino;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da rota não pode ser vazio.");
        }
        this.nome = nome;
    }

    public Local getOrigem() {
        return origem;
    }

    public Local getDestino() {
        return destino;
    }

    /**
     * Atualiza origem e destino juntos, garantindo que a regra
     * "origem diferente de destino" nunca seja violada.
     */
    public void atualizarTrajeto(Local novaOrigem, Local novoDestino) {
        if (novaOrigem == null || novoDestino == null) {
            throw new IllegalArgumentException("Origem e destino são obrigatórios.");
        }
        if (novaOrigem.getId() == novoDestino.getId()) {
            throw new IllegalArgumentException("Origem e destino devem ser locais diferentes.");
        }
        this.origem = novaOrigem;
        this.destino = novoDestino;
    }

    @Override
    public String toString() {
        return "Rota{id=" + id + ", nome='" + nome + "', origem=" + origem.getNome()
                + ", destino=" + destino.getNome() + "}";
    }
}
