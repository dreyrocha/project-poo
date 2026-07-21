package service;

public interface Autenticavel {
    boolean login(String senha);
    void logout();
    boolean isLogada();
}
