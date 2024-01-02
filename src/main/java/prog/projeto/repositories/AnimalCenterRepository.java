package prog.projeto.repositories;

import prog.projeto.models.AnimalCenter;

public class AnimalCenterRepository extends Repository<AnimalCenter> {
  private static AnimalCenterRepository instance;

  private AnimalCenterRepository(){fileName = "animalCenters.dat";}

  public static AnimalCenterRepository getInstance() {
    if(instance == null) instance = new AnimalCenterRepository();
    return instance;
  }

  @Override
  public int getId(AnimalCenter entity) {
    return 0;
  }
}
