package prog.projeto.repositories;

import prog.projeto.models.Service;

import java.util.Collection;

public class ServiceRepository extends Repository<Service> {
  private static ServiceRepository instance;

  private ServiceRepository(){fileName = "services.dat";}

  public static ServiceRepository getInstance() {
    if(instance == null) instance = new ServiceRepository();
    return instance;
  }

  public Collection<Service> getEntities() { return this.entities.values(); }

  @Override
  public int getId(Service entity) {
    return entity.getId();
  }
}
