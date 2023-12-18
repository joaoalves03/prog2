package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.exceptions.AlreadyExistsException;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;

public class Company implements Serializable {
  @Getter
  int id;
  @Getter @Setter
  String name;
  @Getter @Setter
  String address;
  @Getter @Setter
  String city;
  @Getter @Setter
  String phone;
  @Getter @Setter
  CompanySpecialty specialty;
  @Getter
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
