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

  public List<User> getByStatus(boolean active) throws NoSuchElementException {
    return entities.values().stream().filter(user -> user.getStatus() == active).toList();
  }

  public User findByEmail(String email) throws NoSuchElementException {
    return entities.values().stream().filter(
        x -> x.getEmail().equals(email)
    ).findFirst().orElseThrow();
  }

  public User findByCC(String cc) throws NoSuchElementException {
    return entities.values().stream().filter(
            x -> x.getCc().equals(cc)
    ).findFirst().orElseThrow();
  }

  public User findByNIF(String nif) throws NoSuchElementException {
    return entities.values().stream().filter(
            x -> x.getNif().equals(nif)
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
