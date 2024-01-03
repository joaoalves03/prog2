package prog.projeto.repositories;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;

import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class UserRepository extends Repository<User> {
  private static UserRepository instance;
  @Setter private User selectedUser;

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
  public int getId(User entity) {
    return entity.getId();
  }

  public List<User> getAllUsers() {
    return entities.values().stream().toList();
  }
}
