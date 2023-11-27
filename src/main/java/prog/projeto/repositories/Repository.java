package prog.projeto.repositories;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Repository<T> {
  protected String fileName;
  protected Map<Long, T> entities = new HashMap<>();

  public void add(T entity) {
    entities.put(getId(entity), entity);
  }

  public T findById(long id) {
    return entities.get(id);
  }

  public void update(T entity) {
    long id = getId(entity);
    entities.put(id, entity);
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

    entities = (Map<Long, T>) objectInputStream.readObject();
    objectInputStream.close();
  }

  public abstract long getNextId();
  public abstract long getId(T entity);
}
