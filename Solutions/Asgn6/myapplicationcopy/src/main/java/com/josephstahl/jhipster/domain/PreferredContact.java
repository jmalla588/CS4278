package com.josephstahl.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PreferredContact.
 */
@Entity
@Table(name = "preferred_contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PreferredContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name_of_choice")
    private String nameOfChoice;

    @OneToMany(mappedBy = "preferredContact")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Student> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfChoice() {
        return nameOfChoice;
    }

    public PreferredContact nameOfChoice(String nameOfChoice) {
        this.nameOfChoice = nameOfChoice;
        return this;
    }

    public void setNameOfChoice(String nameOfChoice) {
        this.nameOfChoice = nameOfChoice;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public PreferredContact students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public PreferredContact addStudents(Student student) {
        students.add(student);
        student.setPreferredContact(this);
        return this;
    }

    public PreferredContact removeStudents(Student student) {
        students.remove(student);
        student.setPreferredContact(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PreferredContact preferredContact = (PreferredContact) o;
        if(preferredContact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, preferredContact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PreferredContact{" +
            "id=" + id +
            ", nameOfChoice='" + nameOfChoice + "'" +
            '}';
    }
}
