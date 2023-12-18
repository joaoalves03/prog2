package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.exceptions.AlreadyExistsException;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Getter
public class AnimalCenter {
  private final int id;
  private final int providerID;
  @Setter
  private String address;
  @Setter
  private String city;
  @Setter
  private String phone;
  private final AnimalServiceType serviceType;
  @Setter
  private float servicePrice;
  private final Set<Integer> employees;

  public AnimalCenter(int id, int providerID, String address, String city, String phone, AnimalServiceType serviceType, float servicePrice) {
    this.id = id;
    this.providerID = providerID;
    this.address = address;
    this.city = city;
    this.phone = phone;
    this.serviceType = serviceType;
    this.servicePrice = servicePrice;
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
