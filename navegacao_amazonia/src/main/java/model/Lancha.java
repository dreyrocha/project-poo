package model;

/**
 * Especialização de Embarcação: Lancha.
 */
public class Lancha extends Embarcacao {

    private double velocidadeMax;

    public Lancha(String nome, int capacidade, double velocidadeMax) {
        super(nome, capacidade);
        if (velocidadeMax <= 0) {
            throw new IllegalArgumentException("Velocidade máxima deve ser maior que zero.");
        }
        this.velocidadeMax = velocidadeMax;
    }

    public double getVelocidadeMax() {
        return velocidadeMax;
    }

    public void setVelocidadeMax(double velocidadeMax) {
        if (velocidadeMax <= 0) {
            throw new IllegalArgumentException("Velocidade máxima deve ser maior que zero.");
        }
        this.velocidadeMax = velocidadeMax;
    }

@Override
    public String descricao() {
        return getNome() + " (Lancha) - capacidade: " + getCapacidade()
                + " passageiros, velocidade máxima: " + velocidadeMax + " km/h";
    }
}