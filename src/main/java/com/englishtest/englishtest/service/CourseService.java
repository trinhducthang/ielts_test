package com.englishtest.englishtest.service;

import com.englishtest.englishtest.entity.Course;
import com.englishtest.englishtest.entity.User;
import com.englishtest.englishtest.repository.CourseRepository;
import com.englishtest.englishtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<User> getUsersForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseId));

        return course.getUsers(); // Trả về danh sách người dùng của khóa học
    }

    public Course updateCourseUsers(Long courseId, List<Long> userIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<User> allUsers = userRepository.findAllById(userIds);

        // Cập nhật quan hệ từ phía User
        for (User user : allUsers) {
            user.setCourse(course); // Gán khóa ngoại
        }

        // Bỏ liên kết của những user cũ không nằm trong danh sách mới
        List<User> oldUsers = userRepository.findByCourseId(courseId);
        for (User user : oldUsers) {
            if (!userIds.contains(user.getId())) {
                user.setCourse(null);
            }
        }

        userRepository.saveAll(oldUsers);
        userRepository.saveAll(allUsers);

        return course;
    }


    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

}
