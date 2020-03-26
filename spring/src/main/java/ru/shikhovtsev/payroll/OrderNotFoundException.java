package ru.shikhovtsev.payroll;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order not found with id = " + id);
    }
}
