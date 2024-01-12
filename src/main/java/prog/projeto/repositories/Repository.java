package prog.projeto.repositories;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Repository<T> {
  protected String fileName;
  protected Map<Integer, T> entities = new HashMap<>();

  public int length() { return entities.size(); }

  public void add(T entity) {
    entities.put(getId(entity), entity);
  }

  public T findById(int id) {
    return entities.get(id);
  }

  public void update(T entity) {
    entities.put(getId(entity), entity);
  }

  public void delete(int id) {
    entities.remove(id);
  }

  public void save() throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

    objectOutputStream.writeObject(entities);
    objectOutputStream.flush();
    objectOutputStream.close();
  }

  /**
   * Tries to read file defined in the class into the
   * entities variable.<br>
   * If the file doesn't exist, continue with an empty list.<br>
   * It throws any other error to outside the function.
   */
  public void read() throws IOException, ClassNotFoundException {
    if(!new File(fileName).isFile()) {
      return;
    }

    FileInputStream fileInputStream = new FileInputStream(fileName);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

    entities = (Map<Integer, T>) objectInputStream.readObject();
    objectInputStream.close();
  }

  public int getNextId() {
    if(entities.isEmpty()) return 0;

    return Collections.max(entities.keySet()) + 1;
  }

  public abstract int getId(T entity);
}
