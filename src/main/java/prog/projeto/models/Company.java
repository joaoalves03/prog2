package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.exceptions.AlreadyExistsException;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;

@Getter
public class Company implements Serializable {
  int id;
  @Setter
  String name;
  @Setter
  String address;
  @Setter
  String city;
  @Setter
  String phone;
  @Setter
  CompanySpecialty specialty;
  Set<Integer> employees;

  public void addEmployee(int id) throws AlreadyExistsException {
    if(!employees.add(id)) throw new AlreadyExistsException();
  }

  public void removeEmployee(int id) throws NoSuchElementException {
    if (!employees.remove(id)) throw new NoSuchElementException();
  }

  public int getNumberOfEmployees() {
    return employees.size();
  }
}
