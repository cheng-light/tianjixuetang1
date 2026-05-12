package com.tianji.academy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.academy.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    @Select("""
            SELECT r.code FROM roles r
            INNER JOIN user_roles ur ON ur.role_id = r.id
            WHERE ur.user_id = #{userId}
            """)
    List<String> selectRoleCodes(Long userId);

    @Insert("""
            INSERT INTO user_roles(user_id, role_id)
            SELECT #{userId}, id FROM roles WHERE code = 'STUDENT'
            """)
    int insertDefaultStudentRole(Long userId);
}
