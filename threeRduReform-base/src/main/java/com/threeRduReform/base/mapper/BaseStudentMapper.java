package com.threeRduReform.base.mapper;

import java.util.List;
import com.threeRduReform.base.domain.BaseStudent;

/**
 * 学生信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface BaseStudentMapper 
{
    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    public BaseStudent selectBaseStudentByStudentId(Long studentId);

    /**
     * 查询学生信息列表
     * 
     * @param baseStudent 学生信息
     * @return 学生信息集合
     */
    public List<BaseStudent> selectBaseStudentList(BaseStudent baseStudent);

    /**
     * 新增学生信息
     * 
     * @param baseStudent 学生信息
     * @return 结果
     */
    public int insertBaseStudent(BaseStudent baseStudent);

    /**
     * 修改学生信息
     * 
     * @param baseStudent 学生信息
     * @return 结果
     */
    public int updateBaseStudent(BaseStudent baseStudent);

    /**
     * 删除学生信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    public int deleteBaseStudentByStudentId(Long studentId);

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseStudentByStudentIds(Long[] studentIds);
}
