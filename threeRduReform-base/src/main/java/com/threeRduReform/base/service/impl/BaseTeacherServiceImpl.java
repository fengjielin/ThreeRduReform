package com.threeRduReform.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseTeacherMapper;
import com.threeRduReform.base.domain.BaseTeacher;
import com.threeRduReform.base.service.IBaseTeacherService;

/**
 * 教师信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseTeacherServiceImpl implements IBaseTeacherService 
{
    @Autowired
    private BaseTeacherMapper baseTeacherMapper;

    /**
     * 查询教师信息
     * 
     * @param teacherId 教师信息主键
     * @return 教师信息
     */
    @Override
    public BaseTeacher selectBaseTeacherByTeacherId(Long teacherId)
    {
        return baseTeacherMapper.selectBaseTeacherByTeacherId(teacherId);
    }

    /**
     * 查询教师信息列表
     * 
     * @param baseTeacher 教师信息
     * @return 教师信息
     */
    @Override
    public List<BaseTeacher> selectBaseTeacherList(BaseTeacher baseTeacher)
    {
        return baseTeacherMapper.selectBaseTeacherList(baseTeacher);
    }

    /**
     * 新增教师信息
     * 
     * @param baseTeacher 教师信息
     * @return 结果
     */
    @Override
    public int insertBaseTeacher(BaseTeacher baseTeacher)
    {
        return baseTeacherMapper.insertBaseTeacher(baseTeacher);
    }

    /**
     * 修改教师信息
     * 
     * @param baseTeacher 教师信息
     * @return 结果
     */
    @Override
    public int updateBaseTeacher(BaseTeacher baseTeacher)
    {
        return baseTeacherMapper.updateBaseTeacher(baseTeacher);
    }

    /**
     * 批量删除教师信息
     * 
     * @param teacherIds 需要删除的教师信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseTeacherByTeacherIds(Long[] teacherIds)
    {
        return baseTeacherMapper.deleteBaseTeacherByTeacherIds(teacherIds);
    }

    /**
     * 删除教师信息信息
     * 
     * @param teacherId 教师信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseTeacherByTeacherId(Long teacherId)
    {
        return baseTeacherMapper.deleteBaseTeacherByTeacherId(teacherId);
    }
}
