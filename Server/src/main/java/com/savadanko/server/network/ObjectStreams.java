package com.savadanko.server.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectStreams {
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public ObjectStreams(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }
}
