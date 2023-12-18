package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.exceptions.AlreadyExistsException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Getter
public class Company implements Serializable {
  private final int id;
  @Setter
  private String name;
  @Setter
  private String address;
  @Setter
  private String city;
  @Setter
  private String phone;
  @Setter
  private CompanySpecialty specialty;
  private final Set<Integer> employees;

  public Company(int id, String name, String address, String city, String phone, CompanySpecialty specialty) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.city = city;
    this.phone = phone;
    this.specialty = specialty;
    this.employees = new HashSet<>();
  }

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
