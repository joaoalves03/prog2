package prog.projeto.repositories;

import prog.projeto.models.Service;

public class ServiceRepository extends Repository<Service> {
  private static ServiceRepository instance;

  private ServiceRepository(){fileName = "services.dat";}

  private static ServiceRepository getInstance() {
    if(instance == null) instance = new ServiceRepository();
    return instance;
  }

  @Override
  public int getNextId() {
    if(entities.isEmpty()) return 0;

    return getId(entities.get(entities.size()-1)) + 1;
  }

  @Override
  public int getId(Service entity) {
    return entity.getId();
  }
}
