package net.originmobi.pdv.exceptions.produto;

public class InsufficientStockControlException extends RuntimeException {
    public InsufficientStockControlException(String message) {
        super(message);
    }
}
