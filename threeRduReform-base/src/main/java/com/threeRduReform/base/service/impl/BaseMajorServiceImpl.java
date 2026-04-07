package com.threeRduReform.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseMajorMapper;
import com.threeRduReform.base.mapper.BaseCollegeMajorMapper;
import com.threeRduReform.base.domain.BaseMajor;
import com.threeRduReform.base.domain.BaseCollegeMajor;
import com.threeRduReform.base.service.IBaseMajorService;

/**
 * 专业信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseMajorServiceImpl implements IBaseMajorService 
{
    @Autowired
    private BaseMajorMapper baseMajorMapper;

    @Autowired
    private BaseCollegeMajorMapper baseCollegeMajorMapper;

    /**
     * 查询专业信息
     * 
     * @param majorId 专业信息主键
     * @return 专业信息
     */
    @Override
    public BaseMajor selectBaseMajorByMajorId(Long majorId)
    {
        return baseMajorMapper.selectBaseMajorByMajorId(majorId);
    }

    /**
     * 查询专业信息列表
     * 
     * @param baseMajor 专业信息
     * @return 专业信息
     */
    @Override
    public List<BaseMajor> selectBaseMajorList(BaseMajor baseMajor)
    {
        return baseMajorMapper.selectBaseMajorList(baseMajor);
    }

    /**
     * 新增专业信息
     * 
     * @param baseMajor 专业信息
     * @return 结果
     */
    @Override
    public int insertBaseMajor(BaseMajor baseMajor)
    {
        int result = baseMajorMapper.insertBaseMajor(baseMajor);
        // 维护专业与院校关联关系
        if (result > 0 && baseMajor.getCollegeId() != null) {
            BaseCollegeMajor relation = new BaseCollegeMajor();
            relation.setMajorId(baseMajor.getMajorId());
            relation.setCollegeId(baseMajor.getCollegeId());
            baseCollegeMajorMapper.insertCollegeMajor(relation);
        }
        return result;
    }

    /**
     * 修改专业信息
     * 
     * @param baseMajor 专业信息
     * @return 结果
     */
    @Override
    public int updateBaseMajor(BaseMajor baseMajor)
    {
        int result = baseMajorMapper.updateBaseMajor(baseMajor);
        // 维护专业与院校关联关系
        if (result > 0 && baseMajor.getCollegeId() != null) {
            // 先删除旧关联
            baseCollegeMajorMapper.deleteCollegeMajorByMajorId(baseMajor.getMajorId());
            // 再插入新关联
            BaseCollegeMajor relation = new BaseCollegeMajor();
            relation.setMajorId(baseMajor.getMajorId());
            relation.setCollegeId(baseMajor.getCollegeId());
            baseCollegeMajorMapper.insertCollegeMajor(relation);
        }
        return result;
    }

    /**
     * 批量删除专业信息
     * 
     * @param majorIds 需要删除的专业信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseMajorByMajorIds(Long[] majorIds)
    {
        // 先删除关联关系
        baseCollegeMajorMapper.deleteCollegeMajorByMajorIds(majorIds);
        return baseMajorMapper.deleteBaseMajorByMajorIds(majorIds);
    }

    /**
     * 删除专业信息信息
     * 
     * @param majorId 专业信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseMajorByMajorId(Long majorId)
    {
        // 先删除关联关系
        baseCollegeMajorMapper.deleteCollegeMajorByMajorId(majorId);
        return baseMajorMapper.deleteBaseMajorByMajorId(majorId);
    }
}
