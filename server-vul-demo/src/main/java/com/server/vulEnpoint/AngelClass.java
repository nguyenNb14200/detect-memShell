package com.server.vulEnpoint;

public class AngelClass extends ClassLoader {
    private final byte[] classBytes;

    public AngelClass( byte[] classBytes) {
        this.classBytes = classBytes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
