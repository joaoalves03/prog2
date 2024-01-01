package prog.projeto.repositories;

import java.io.*;
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

  public void delete(long id) {
    entities.remove(id);
  }

  public void save() throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

    objectOutputStream.writeObject(entities);
    objectOutputStream.flush();
    objectOutputStream.close();
  }

  public void read() throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(fileName);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

    entities = (Map<Integer, T>) objectInputStream.readObject();
    objectInputStream.close();
  }

  public abstract int getNextId();
  public abstract int getId(T entity);
}
