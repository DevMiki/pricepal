package com.codercollie.error;

public class ItemNotFoundException extends RuntimeException{
    private final Long id;

    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
