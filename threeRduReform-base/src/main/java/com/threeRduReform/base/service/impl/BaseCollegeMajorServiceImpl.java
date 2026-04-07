package com.threeRduReform.base.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseCollegeMajorMapper;
import com.threeRduReform.base.domain.BaseCollegeMajor;
import com.threeRduReform.base.service.IBaseCollegeMajorService;

/**
 * 院校与专业关联Service接口实现
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseCollegeMajorServiceImpl implements IBaseCollegeMajorService 
{
    @Autowired
    private BaseCollegeMajorMapper baseCollegeMajorMapper;

    /**
     * 查询专业关联的院校列表
     * 
     * @param majorId 专业ID
     * @return 院校列表
     */
    @Override
    public List<Map<String, Object>> selectCollegeListByMajorId(Long majorId)
    {
        return baseCollegeMajorMapper.selectCollegeListByMajorId(majorId);
    }

    /**
     * 查询院校关联的专业列表
     * 
     * @param collegeId 院校ID
     * @return 专业列表
     */
    @Override
    public List<Map<String, Object>> selectMajorListByCollegeId(Long collegeId)
    {
        return baseCollegeMajorMapper.selectMajorListByCollegeId(collegeId);
    }

    /**
     * 添加院校与专业关联
     * 
     * @param collegeMajor 关联信息
     * @return 结果
     */
    @Override
    public int insertCollegeMajor(BaseCollegeMajor collegeMajor)
    {
        return baseCollegeMajorMapper.insertCollegeMajor(collegeMajor);
    }

    /**
     * 删除院校与专业关联
     * 
     * @param majorId 专业ID
     * @return 结果
     */
    @Override
    public int deleteCollegeMajorByMajorId(Long majorId)
    {
        return baseCollegeMajorMapper.deleteCollegeMajorByMajorId(majorId);
    }

    /**
     * 批量删除院校与专业关联
     * 
     * @param majorIds 专业ID集合
     * @return 结果
     */
    @Override
    public int deleteCollegeMajorByMajorIds(Long[] majorIds)
    {
        return baseCollegeMajorMapper.deleteCollegeMajorByMajorIds(majorIds);
    }

    /**
     * 根据院校ID删除关联
     * 
     * @param collegeId 院校ID
     * @return 结果
     */
    @Override
    public int deleteCollegeMajorByCollegeId(Long collegeId)
    {
        return baseCollegeMajorMapper.deleteCollegeMajorByCollegeId(collegeId);
    }
}
