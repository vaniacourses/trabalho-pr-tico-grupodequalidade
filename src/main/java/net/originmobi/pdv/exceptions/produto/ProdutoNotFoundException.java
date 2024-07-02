package net.originmobi.pdv.exceptions.produto;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(String message) {
        super(message);
    }
}