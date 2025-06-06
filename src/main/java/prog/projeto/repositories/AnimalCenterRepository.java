package prog.projeto.repositories;

import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalCenterRepository extends Repository<AnimalCenter> {
  private static AnimalCenterRepository instance;

  private AnimalCenterRepository(){fileName = "animalCenters.dat";}

  public static AnimalCenterRepository getInstance() {
    if(instance == null) instance = new AnimalCenterRepository();
    return instance;
  }

  public List<User> getEmployees(AnimalCenter animalCenter) {
    UserRepository userRepository = UserRepository.getInstance();

    return new ArrayList<>(userRepository.getByType(UserType.Staff));
  }

  public Collection<AnimalCenter> getEntities() { return this.entities.values(); }

  public List<AnimalCenter> getByProvider(int providerID) {
    return entities.values().stream().filter(
        animalCenter -> animalCenter.getProviderID() == providerID
    ).collect(Collectors.toList());
  }

  @Override
  public int getId(AnimalCenter entity) {
    return entity.getId();
  }
}
