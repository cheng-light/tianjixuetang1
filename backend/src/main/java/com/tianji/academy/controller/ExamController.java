package com.tianji.academy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.academy.common.ApiResponse;
import com.tianji.academy.dto.ExamSubmitRequest;
import com.tianji.academy.entity.Exam;
import com.tianji.academy.entity.ExamQuestion;
import com.tianji.academy.entity.ExamRecord;
import com.tianji.academy.mapper.ExamMapper;
import com.tianji.academy.mapper.ExamQuestionMapper;
import com.tianji.academy.mapper.ExamRecordMapper;
import com.tianji.academy.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final ExamMapper examMapper;
    private final ExamQuestionMapper questionMapper;
    private final ExamRecordMapper recordMapper;
    private final CurrentUserService currentUserService;

    public ExamController(ExamMapper examMapper, ExamQuestionMapper questionMapper, ExamRecordMapper recordMapper, CurrentUserService currentUserService) {
        this.examMapper = examMapper;
        this.questionMapper = questionMapper;
        this.recordMapper = recordMapper;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ApiResponse<List<Exam>> list(@RequestParam(required = false) Long courseId) {
        LambdaQueryWrapper<Exam> wrapper = new LambdaQueryWrapper<>();
        if (courseId != null) {
            wrapper.eq(Exam::getCourseId, courseId);
        }
        return ApiResponse.ok(examMapper.selectList(wrapper));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        List<ExamQuestion> questions = questionMapper.selectList(
                new LambdaQueryWrapper<ExamQuestion>().eq(ExamQuestion::getExamId, id).orderByAsc(ExamQuestion::getSortOrder));
        questions.forEach(question -> question.setAnswer(null));
        return ApiResponse.ok(Map.of("exam", examMapper.selectById(id), "questions", questions));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<Exam> create(@RequestBody Exam exam) {
        examMapper.insert(exam);
        return ApiResponse.ok(exam);
    }

    @PostMapping("/{examId}/questions")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ApiResponse<ExamQuestion> createQuestion(@PathVariable Long examId, @RequestBody ExamQuestion question) {
        question.setExamId(examId);
        questionMapper.insert(question);
        return ApiResponse.ok(question);
    }

    @PostMapping("/records")
    public ApiResponse<ExamRecord> submit(@Valid @RequestBody ExamSubmitRequest request) {
        ExamRecord record = new ExamRecord();
        record.setExamId(request.getExamId());
        record.setUserId(currentUserService.userId());
        record.setAnswersJson(request.getAnswersJson());
        record.setScore(0);
        record.setPassed(0);
        recordMapper.insert(record);
        return ApiResponse.ok(record);
    }
}
