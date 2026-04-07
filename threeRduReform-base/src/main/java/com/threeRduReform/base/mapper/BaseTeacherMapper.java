package com.threeRduReform.base.mapper;

import java.util.List;
import com.threeRduReform.base.domain.BaseTeacher;

/**
 * 教师信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface BaseTeacherMapper 
{
    /**
     * 查询教师信息
     * 
     * @param teacherId 教师信息主键
     * @return 教师信息
     */
    public BaseTeacher selectBaseTeacherByTeacherId(Long teacherId);

    /**
     * 查询教师信息列表
     * 
     * @param baseTeacher 教师信息
     * @return 教师信息集合
     */
    public List<BaseTeacher> selectBaseTeacherList(BaseTeacher baseTeacher);

    /**
     * 新增教师信息
     * 
     * @param baseTeacher 教师信息
     * @return 结果
     */
    public int insertBaseTeacher(BaseTeacher baseTeacher);

    /**
     * 修改教师信息
     * 
     * @param baseTeacher 教师信息
     * @return 结果
     */
    public int updateBaseTeacher(BaseTeacher baseTeacher);

    /**
     * 删除教师信息
     * 
     * @param teacherId 教师信息主键
     * @return 结果
     */
    public int deleteBaseTeacherByTeacherId(Long teacherId);

    /**
     * 批量删除教师信息
     * 
     * @param teacherIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseTeacherByTeacherIds(Long[] teacherIds);
}
