package br.com.alura.school.service;

import br.com.alura.school.controller.request.EnrollCourseRequest;
import br.com.alura.school.controller.response.CourseResponse;
import br.com.alura.school.controller.response.ReportResponse;
import br.com.alura.school.model.Course;
import br.com.alura.school.model.Enrollment;
import br.com.alura.school.model.User;
import br.com.alura.school.repository.CourseRepository;
import br.com.alura.school.repository.EnrollmentRepository;
import br.com.alura.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public List<CourseResponse> findAll() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(CourseResponse::new)
                .collect(Collectors.toList());
    }

    public Course findByCode(String code) {
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
    }

    public Boolean enroll(String couseCode, EnrollCourseRequest enrollCourseRequest) {
        User user = userRepository.findByUsername(enrollCourseRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with name %s not found", enrollCourseRequest.getUsername())));
        Course course = courseRepository.findByCode(couseCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", couseCode)));

        boolean isEnrolled = enrollmentRepository.findAllByUserId(user.getId()).stream()
                .anyMatch(enrollment -> enrollment.getCourseId().equals(course.getId()));

        if (!isEnrolled) {
            enrollmentRepository.save(new Enrollment(user.getId(), course.getId(), LocalDate.now()));
            User savedUser = userRepository.findById(user.getId()).get();
            savedUser.setNumberOfEnrollments(savedUser.getNumberOfEnrollments() + 1);
            userRepository.save(savedUser);
        }

        return isEnrolled;
    }

    public List<ReportResponse> report() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        List<ReportResponse> response = new ArrayList<>();

        if (enrollments.isEmpty()) {
            return response;
        }

        List<Long> usersWithEnrollments = enrollments.stream()
                .map(Enrollment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        usersWithEnrollments.forEach(userId -> {
            Optional<User> user = userRepository.findById(userId);
            user.ifPresent(value -> response.add(new ReportResponse(value.getEmail(), value.getNumberOfEnrollments())));
        });

        response.sort(Comparator.comparing(ReportResponse::getNumberOfEnrollments));
        return response;
    }
}
