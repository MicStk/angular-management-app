package at.ac.tuwien.sepm.assignment.individual.entity;

import at.ac.tuwien.sepm.assignment.individual.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.dto.OwnerDto;
import at.ac.tuwien.sepm.assignment.individual.type.Sex;
import java.time.LocalDate;

/**
 * Represents a horse in the persistent data store.
 */
public class Horse {
  private Long id;
  private String name;
  private String description;
  private LocalDate dateOfBirth;
  private Sex sex;
  private Long ownerId;
  private Long motherId;
  private Long fatherId;

  private Horse mother;
  private Horse father;

  public Horse(Long id, String name, String description, LocalDate dateOfBirth, Sex sex, OwnerDto owner, HorseDto mother, HorseDto father) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.ownerId = owner.id();
    this.motherId = mother.id();
    this.fatherId = father.id();
  }

  public Horse() {

  }

  public Long getId() {
    return id;
  }

  public Horse setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Horse setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Horse setDescription(String description) {
    this.description = description;
    return this;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public Horse setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public Sex getSex() {
    return sex;
  }

  public Horse setSex(Sex sex) {
    this.sex = sex;
    return this;
  }


  public Long getOwnerId() {
    return ownerId;
  }

  public Horse setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  public Long getFatherId() {
    return fatherId;
  }

  public Horse setFatherId(Long fatherId) {
    this.fatherId = fatherId;
    return this;
  }

  public Long getMotherId() {
    return motherId;
  }

  public Horse setMotherId(Long motherId) {
    this.motherId = motherId;
    return this;
  }

  public Horse getFather() {
    return father;
  }

  public void setFather(Horse father) {
    this.father = father;
  }

  public void setMother(Horse mother) {
    this.mother = mother;
  }

  public Horse getMother() {
    return mother;
  }

  @Override
  public String toString() {
    return "Horse{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", description='" + description + '\''
        + ", dateOfBirth=" + dateOfBirth
        + ", sex=" + sex
        + ", ownerId=" + ownerId
        + '}';
  }
}
