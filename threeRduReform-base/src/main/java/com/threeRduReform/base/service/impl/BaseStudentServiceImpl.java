package com.threeRduReform.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseStudentMapper;
import com.threeRduReform.base.domain.BaseStudent;
import com.threeRduReform.base.service.IBaseStudentService;

/**
 * 学生信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseStudentServiceImpl implements IBaseStudentService 
{
    @Autowired
    private BaseStudentMapper baseStudentMapper;

    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    @Override
    public BaseStudent selectBaseStudentByStudentId(Long studentId)
    {
        return baseStudentMapper.selectBaseStudentByStudentId(studentId);
    }

    /**
     * 查询学生信息列表
     * 
     * @param baseStudent 学生信息
     * @return 学生信息
     */
    @Override
    public List<BaseStudent> selectBaseStudentList(BaseStudent baseStudent)
    {
        return baseStudentMapper.selectBaseStudentList(baseStudent);
    }

    /**
     * 新增学生信息
     * 
     * @param baseStudent 学生信息
     * @return 结果
     */
    @Override
    public int insertBaseStudent(BaseStudent baseStudent)
    {
        return baseStudentMapper.insertBaseStudent(baseStudent);
    }

    /**
     * 修改学生信息
     * 
     * @param baseStudent 学生信息
     * @return 结果
     */
    @Override
    public int updateBaseStudent(BaseStudent baseStudent)
    {
        return baseStudentMapper.updateBaseStudent(baseStudent);
    }

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的学生信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseStudentByStudentIds(Long[] studentIds)
    {
        return baseStudentMapper.deleteBaseStudentByStudentIds(studentIds);
    }

    /**
     * 删除学生信息信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseStudentByStudentId(Long studentId)
    {
        return baseStudentMapper.deleteBaseStudentByStudentId(studentId);
    }
}
