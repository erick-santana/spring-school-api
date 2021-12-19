package br.com.alura.school.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDate createdAt;

    public Enrollment(Long userId, Long courseId, LocalDate createdAt) {
        this.userId = userId;
        this.courseId = courseId;
        this.createdAt = createdAt;
    }
}
