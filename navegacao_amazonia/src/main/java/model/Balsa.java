package model;

/**
 * Especialização de Embarcação: Balsa.
 */
public class Balsa extends Embarcacao {

    private int capacidadeVeiculos;

    public Balsa(String nome, int capacidade, int capacidadeVeiculos) {
        super(nome, capacidade);
        if (capacidadeVeiculos <= 0) {
            throw new IllegalArgumentException("Capacidade de veículos deve ser maior que zero.");
        }
        this.capacidadeVeiculos = capacidadeVeiculos;
    }

    public int getCapacidadeVeiculos() {
        return capacidadeVeiculos;
    }

    public void setCapacidadeVeiculos(int capacidadeVeiculos) {
        if (capacidadeVeiculos <= 0) {
            throw new IllegalArgumentException("Capacidade de veículos deve ser maior que zero.");
        }
        this.capacidadeVeiculos = capacidadeVeiculos;
    }

@Override
    public String descricao() {
        return getNome() + " (Balsa) - capacidade: " + getCapacidade()
                + " passageiros, " + capacidadeVeiculos + " veículos";
    }
}
