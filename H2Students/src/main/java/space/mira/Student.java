package space.mira;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthDate;
    private String grp; //group

    public Student(String firstName, String lastName, String patronymic, Date birthDate, String group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.grp = group;
    }

    @Override
    public String toString() {
        return id + ") " + firstName + " " + lastName + " " + patronymic +
                ", родился: " + birthDate + ", студент группы: " + grp;
    }
}
