package com.example.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Entity<ID> implements Serializable {
    protected ID id;
    @Serial
    private static final long serialVersionUID = 7331115341259248461L;


    public ID getId() {
        return this.id;
    }

    public void setID(ID id) {
        this.id = id;
    }


}

