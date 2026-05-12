package com.tianji.academy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.academy.common.ApiResponse;
import com.tianji.academy.dto.ProgressRequest;
import com.tianji.academy.entity.LearningProgress;
import com.tianji.academy.mapper.LearningProgressMapper;
import com.tianji.academy.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/learning")
public class LearningController {
    private final LearningProgressMapper progressMapper;
    private final CurrentUserService currentUserService;

    public LearningController(LearningProgressMapper progressMapper, CurrentUserService currentUserService) {
        this.progressMapper = progressMapper;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/progress")
    public ApiResponse<List<LearningProgress>> myProgress(@RequestParam(required = false) Long courseId) {
        Long userId = currentUserService.userId();
        LambdaQueryWrapper<LearningProgress> wrapper = new LambdaQueryWrapper<LearningProgress>()
                .eq(LearningProgress::getUserId, userId);
        if (courseId != null) {
            wrapper.eq(LearningProgress::getCourseId, courseId);
        }
        return ApiResponse.ok(progressMapper.selectList(wrapper));
    }

    @PostMapping("/progress")
    public ApiResponse<LearningProgress> saveProgress(@Valid @RequestBody ProgressRequest request) {
        Long userId = currentUserService.userId();
        LearningProgress progress = progressMapper.selectOne(new LambdaQueryWrapper<LearningProgress>()
                .eq(LearningProgress::getUserId, userId)
                .eq(LearningProgress::getChapterId, request.getChapterId()));
        if (progress == null) {
            progress = new LearningProgress();
            progress.setUserId(userId);
            progress.setCourseId(request.getCourseId());
            progress.setChapterId(request.getChapterId());
            progress.setLearnedSeconds(request.getLearnedSeconds());
            progress.setCompleted(request.getCompleted());
            progressMapper.insert(progress);
        } else {
            progress.setLearnedSeconds(request.getLearnedSeconds());
            progress.setCompleted(request.getCompleted());
            progressMapper.updateById(progress);
        }
        return ApiResponse.ok(progress);
    }
}
