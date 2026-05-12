package com.tianji.academy.dto;

import jakarta.validation.constraints.NotNull;

public class ProgressRequest {
    @NotNull(message = "课程不能为空")
    private Long courseId;

    @NotNull(message = "章节不能为空")
    private Long chapterId;

    private Integer learnedSeconds = 0;
    private Integer completed = 0;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getLearnedSeconds() {
        return learnedSeconds;
    }

    public void setLearnedSeconds(Integer learnedSeconds) {
        this.learnedSeconds = learnedSeconds;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }
}
