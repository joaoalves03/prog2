package prog.projeto.repositories;

import prog.projeto.models.users.User;
import prog.projeto.models.users.UserType;

import java.util.List;
import java.util.NoSuchElementException;

public class UserRepository extends Repository<User> {
  private static UserRepository instance;

  private UserRepository(){fileName = "users.dat";}

  public static UserRepository getInstance() {
    if(instance == null) instance = new UserRepository();
    return instance;
  }

  public List<User> getByType(UserType type) {
    return entities.values().stream().filter(user -> user.getType() == type).toList();
  }

  public User findByEmail(String email) throws NoSuchElementException {
    return entities.values().stream().filter(
        x -> x.getEmail().equals(email)
    ).findFirst().orElseThrow();
  }

  @Override
  public long getNextId() {
    //TODO
    return 0;
  }

  @Override
  public long getId(User entity) {
    return entity.getId();
  }
}
