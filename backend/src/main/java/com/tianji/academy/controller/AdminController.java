package com.tianji.academy.controller;

import com.tianji.academy.common.ApiResponse;
import com.tianji.academy.entity.Course;
import com.tianji.academy.entity.User;
import com.tianji.academy.mapper.CourseMapper;
import com.tianji.academy.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;

    public AdminController(UserMapper userMapper, CourseMapper courseMapper) {
        this.userMapper = userMapper;
        this.courseMapper = courseMapper;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Map<String, Long>> dashboard() {
        return ApiResponse.ok(Map.of(
                "users", userMapper.selectCount(null),
                "courses", courseMapper.selectCount(null)
        ));
    }

    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        List<User> users = userMapper.selectList(null);
        users.forEach(user -> user.setPassword(null));
        return ApiResponse.ok(users);
    }

    @GetMapping("/courses")
    public ApiResponse<List<Course>> courses() {
        return ApiResponse.ok(courseMapper.selectList(null));
    }
}
