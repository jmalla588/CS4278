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
 * A Dormitory.
 */
@Entity
@Table(name = "dormitory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dormitory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dorm_name")
    private String dormName;

    @OneToMany(mappedBy = "dormitory")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Student> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDormName() {
        return dormName;
    }

    public Dormitory dormName(String dormName) {
        this.dormName = dormName;
        return this;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Dormitory students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public Dormitory addStudents(Student student) {
        students.add(student);
        student.setDormitory(this);
        return this;
    }

    public Dormitory removeStudents(Student student) {
        students.remove(student);
        student.setDormitory(null);
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
        Dormitory dormitory = (Dormitory) o;
        if(dormitory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dormitory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dormitory{" +
            "id=" + id +
            ", dormName='" + dormName + "'" +
            '}';
    }
}
