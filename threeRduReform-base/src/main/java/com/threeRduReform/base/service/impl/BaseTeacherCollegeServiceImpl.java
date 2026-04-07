package com.threeRduReform.base.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseTeacherCollegeMapper;
import com.threeRduReform.base.domain.BaseTeacherCollege;
import com.threeRduReform.base.service.IBaseTeacherCollegeService;

/**
 * 教师与院校关联Service接口实现
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseTeacherCollegeServiceImpl implements IBaseTeacherCollegeService 
{
    @Autowired
    private BaseTeacherCollegeMapper baseTeacherCollegeMapper;

    /**
     * 查询教师关联的院校列表
     * 
     * @param teacherId 教师ID
     * @return 院校列表
     */
    @Override
    public List<Map<String, Object>> selectCollegeListByTeacherId(Long teacherId)
    {
        return baseTeacherCollegeMapper.selectCollegeListByTeacherId(teacherId);
    }

    /**
     * 查询院校关联的教师列表
     * 
     * @param collegeId 院校ID
     * @return 教师列表
     */
    @Override
    public List<Map<String, Object>> selectTeacherListByCollegeId(Long collegeId)
    {
        return baseTeacherCollegeMapper.selectTeacherListByCollegeId(collegeId);
    }

    /**
     * 添加教师与院校关联
     * 
     * @param teacherCollege 关联信息
     * @return 结果
     */
    @Override
    public int insertTeacherCollege(BaseTeacherCollege teacherCollege)
    {
        return baseTeacherCollegeMapper.insertTeacherCollege(teacherCollege);
    }

    /**
     * 删除教师与院校关联
     * 
     * @param teacherId 教师ID
     * @return 结果
     */
    @Override
    public int deleteTeacherCollegeByTeacherId(Long teacherId)
    {
        return baseTeacherCollegeMapper.deleteTeacherCollegeByTeacherId(teacherId);
    }

    /**
     * 批量删除教师与院校关联
     * 
     * @param teacherIds 教师ID集合
     * @return 结果
     */
    @Override
    public int deleteTeacherCollegeByTeacherIds(Long[] teacherIds)
    {
        return baseTeacherCollegeMapper.deleteTeacherCollegeByTeacherIds(teacherIds);
    }

    /**
     * 根据院校ID删除关联
     * 
     * @param collegeId 院校ID
     * @return 结果
     */
    @Override
    public int deleteTeacherCollegeByCollegeId(Long collegeId)
    {
        return baseTeacherCollegeMapper.deleteTeacherCollegeByCollegeId(collegeId);
    }
}
