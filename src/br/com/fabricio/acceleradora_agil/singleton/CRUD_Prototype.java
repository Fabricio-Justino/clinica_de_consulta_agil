package br.com.fabricio.acceleradora_agil.singleton;

import java.io.*;
import java.util.Map;

public enum CRUD_Prototype {
    ISTANCE;

    private final File mainFile;

    CRUD_Prototype() {
        this.mainFile = new File("./config/");
        initFile();
    }

    private void initFile() {
        if (!this.mainFile.exists()) {
            if (!this.mainFile.mkdir()) {
                System.err.println("CINFIGURATION NOT DONE");
            }
        }
    }

    public <T extends Serializable> void save(T object) {
        File file = new File(this.mainFile.getPath() + "/data_object_%s.bin".formatted(object.getClass().getSimpleName()));
        try (FileOutputStream out = new FileOutputStream(file)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
                outputStream.writeObject(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Serializable> void save(T object, String filename) {
        File file = new File(this.mainFile.getPath() + "/" + filename);
        try (FileOutputStream out = new FileOutputStream(file)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(out)) {
                outputStream.writeObject(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Serializable> T load(String filename) {
        File file = new File(this.mainFile.getPath() + "/" + filename);
        T retriver = null;

        if (file.exists()) {
            try (var in = new FileInputStream(file)) {
                try (var outputStream = new ObjectInputStream(in)) {
                   retriver = (T) outputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return retriver;
    }
}
