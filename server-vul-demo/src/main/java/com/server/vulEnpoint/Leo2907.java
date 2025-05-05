package com.server.vulEnpoint;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class Leo2907 implements Serializable{
    private static final long serialVersionUID = 1L;
    private final byte[] classBytes;
    private String name;

    public Leo2907(byte[] classBytes, String name) {
        this.classBytes = classBytes;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {}

    private void evil_fun() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        AngelClass loader = new AngelClass(this.classBytes);
        // Load class từ file
        Class<?> loadedClass = loader.loadClass(this.name);

        // Tạo instance và gọi phương thức
        Object obj = loadedClass.getDeclaredConstructor().newInstance();
        System.out.println("Đã tạo đối tượng từ class: " + obj.getClass().getName());
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Đọc dữ liệu mặc định
        in.defaultReadObject();

        evil_fun();

    }
}
