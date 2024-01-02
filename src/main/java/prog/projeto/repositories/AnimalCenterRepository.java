package prog.projeto.repositories;

import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;

import java.util.ArrayList;
import java.util.List;

public class AnimalCenterRepository extends Repository<AnimalCenter> {
  private static AnimalCenterRepository instance;

  private AnimalCenterRepository(){fileName = "animalCenters.dat";}

  public static AnimalCenterRepository getInstance() {
    if(instance == null) instance = new AnimalCenterRepository();
    return instance;
  }

  public List<User> getEmployees(AnimalCenter animalCenter) {
    UserRepository userRepository = UserRepository.getInstance();
    List<User> employees = new ArrayList<>();

    employees.addAll(userRepository.getByType(UserType.Secretary));
    employees.addAll(userRepository.getByType(UserType.Assistant));
    employees.addAll(userRepository.getByType(UserType.Educator));
    employees.addAll(userRepository.getByType(UserType.Veterinarian));

    return employees;
  }

  @Override
  public int getId(AnimalCenter entity) {
    return 0;
  }
}
