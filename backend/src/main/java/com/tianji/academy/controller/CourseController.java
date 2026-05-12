package com.tianji.academy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.academy.common.ApiResponse;
import com.tianji.academy.entity.Course;
import com.tianji.academy.entity.CourseChapter;
import com.tianji.academy.mapper.CourseChapterMapper;
import com.tianji.academy.mapper.CourseMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseMapper courseMapper;
    private final CourseChapterMapper chapterMapper;

    public CourseController(CourseMapper courseMapper, CourseChapterMapper chapterMapper) {
        this.courseMapper = courseMapper;
        this.chapterMapper = chapterMapper;
    }

    @GetMapping
    public ApiResponse<List<Course>> list() {
        return ApiResponse.ok(courseMapper.selectList(new LambdaQueryWrapper<Course>().orderByDesc(Course::getCreatedAt)));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        Course course = courseMapper.selectById(id);
        List<CourseChapter> chapters = chapterMapper.selectList(
                new LambdaQueryWrapper<CourseChapter>().eq(CourseChapter::getCourseId, id).orderByAsc(CourseChapter::getSortOrder));
        return ApiResponse.ok(Map.of("course", course, "chapters", chapters));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Course> create(@RequestBody Course course) {
        courseMapper.insert(course);
        return ApiResponse.ok(course);
    }

    @PostMapping("/{courseId}/chapters")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<CourseChapter> addChapter(@PathVariable Long courseId, @RequestBody CourseChapter chapter) {
        chapter.setCourseId(courseId);
        chapterMapper.insert(chapter);
        return ApiResponse.ok(chapter);
    }
}
