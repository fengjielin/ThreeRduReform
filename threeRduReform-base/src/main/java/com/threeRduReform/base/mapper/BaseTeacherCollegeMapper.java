package com.threeRduReform.base.mapper;

import java.util.List;
import java.util.Map;
import com.threeRduReform.base.domain.BaseTeacherCollege;

/**
 * 教师与院校关联Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface BaseTeacherCollegeMapper 
{
    /**
     * 查询教师关联的院校列表
     * 
     * @param teacherId 教师ID
     * @return 院校列表
     */
    public List<Map<String, Object>> selectCollegeListByTeacherId(Long teacherId);

    /**
     * 查询院校关联的教师列表
     * 
     * @param collegeId 院校ID
     * @return 教师列表
     */
    public List<Map<String, Object>> selectTeacherListByCollegeId(Long collegeId);

    /**
     * 添加教师与院校关联
     * 
     * @param teacherCollege 关联信息
     * @return 结果
     */
    public int insertTeacherCollege(BaseTeacherCollege teacherCollege);

    /**
     * 删除教师与院校关联
     * 
     * @param teacherId 教师ID
     * @return 结果
     */
    public int deleteTeacherCollegeByTeacherId(Long teacherId);

    /**
     * 批量删除教师与院校关联
     * 
     * @param teacherIds 教师ID集合
     * @return 结果
     */
    public int deleteTeacherCollegeByTeacherIds(Long[] teacherIds);

    /**
     * 根据院校ID删除关联
     * 
     * @param collegeId 院校ID
     * @return 结果
     */
    public int deleteTeacherCollegeByCollegeId(Long collegeId);
}
