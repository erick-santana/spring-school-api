package br.com.alura.school.controller;

import br.com.alura.school.controller.request.EnrollCourseRequest;
import br.com.alura.school.controller.request.NewCourseRequest;
import br.com.alura.school.controller.response.CourseResponse;
import br.com.alura.school.controller.response.ReportResponse;
import br.com.alura.school.model.Course;
import br.com.alura.school.repository.CourseRepository;
import br.com.alura.school.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> allCourses() {
        List<CourseResponse> response = courseService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{code}")
    public ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseService.findByCode(code);
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    public ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/courses/{courseCode}/enroll")
    public ResponseEntity<String> enroll(@PathVariable("courseCode") String courseCode, @RequestBody @Valid EnrollCourseRequest enrollCourseRequest) {
        boolean isEnrolled = courseService.enroll(courseCode, enrollCourseRequest);

        return isEnrolled
                ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ja encontra-se matriculado neste curso!")
                : ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/courses/enroll/report")
    public ResponseEntity<List<ReportResponse>> report() {
        List<ReportResponse> response = courseService.report();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }
}
