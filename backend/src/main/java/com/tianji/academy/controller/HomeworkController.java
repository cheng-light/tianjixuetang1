package com.tianji.academy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.academy.common.ApiResponse;
import com.tianji.academy.dto.HomeworkSubmitRequest;
import com.tianji.academy.entity.Homework;
import com.tianji.academy.entity.HomeworkSubmission;
import com.tianji.academy.mapper.HomeworkMapper;
import com.tianji.academy.mapper.HomeworkSubmissionMapper;
import com.tianji.academy.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {
    private final HomeworkMapper homeworkMapper;
    private final HomeworkSubmissionMapper submissionMapper;
    private final CurrentUserService currentUserService;

    public HomeworkController(HomeworkMapper homeworkMapper, HomeworkSubmissionMapper submissionMapper, CurrentUserService currentUserService) {
        this.homeworkMapper = homeworkMapper;
        this.submissionMapper = submissionMapper;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ApiResponse<List<Homework>> list(@RequestParam Long courseId) {
        return ApiResponse.ok(homeworkMapper.selectList(new LambdaQueryWrapper<Homework>().eq(Homework::getCourseId, courseId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Homework> create(@RequestBody Homework homework) {
        homework.setCreatedBy(currentUserService.userId());
        homeworkMapper.insert(homework);
        return ApiResponse.ok(homework);
    }

    @PostMapping("/submissions")
    public ApiResponse<HomeworkSubmission> submit(@Valid @RequestBody HomeworkSubmitRequest request) {
        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setHomeworkId(request.getHomeworkId());
        submission.setUserId(currentUserService.userId());
        submission.setAnswer(request.getAnswer());
        submission.setAttachmentUrl(request.getAttachmentUrl());
        submissionMapper.insert(submission);
        return ApiResponse.ok(submission);
    }

    @GetMapping("/submissions")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<List<HomeworkSubmission>> submissions(@RequestParam Long homeworkId) {
        return ApiResponse.ok(submissionMapper.selectList(
                new LambdaQueryWrapper<HomeworkSubmission>().eq(HomeworkSubmission::getHomeworkId, homeworkId)));
    }
}
