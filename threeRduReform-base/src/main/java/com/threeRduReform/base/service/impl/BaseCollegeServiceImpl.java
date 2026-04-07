package com.threeRduReform.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.threeRduReform.base.mapper.BaseCollegeMapper;
import com.threeRduReform.base.domain.BaseCollege;
import com.threeRduReform.base.service.IBaseCollegeService;

/**
 * 院校信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class BaseCollegeServiceImpl implements IBaseCollegeService 
{
    @Autowired
    private BaseCollegeMapper baseCollegeMapper;

    /**
     * 查询院校信息
     * 
     * @param collegeId 院校信息主键
     * @return 院校信息
     */
    @Override
    public BaseCollege selectBaseCollegeByCollegeId(Long collegeId)
    {
        return baseCollegeMapper.selectBaseCollegeByCollegeId(collegeId);
    }

    /**
     * 查询院校信息列表
     * 
     * @param baseCollege 院校信息
     * @return 院校信息
     */
    @Override
    public List<BaseCollege> selectBaseCollegeList(BaseCollege baseCollege)
    {
        return baseCollegeMapper.selectBaseCollegeList(baseCollege);
    }

    /**
     * 查询院校信息树列表
     * 
     * @param baseCollege 院校信息
     * @return 院校信息集合
     */
    @Override
    public List<BaseCollege> selectBaseCollegeTreeList(BaseCollege baseCollege)
    {
        return baseCollegeMapper.selectBaseCollegeTreeList(baseCollege);
    }

    /**
     * 新增院校信息
     * 
     * @param baseCollege 院校信息
     * @return 结果
     */
    @Override
    public int insertBaseCollege(BaseCollege baseCollege)
    {
        return baseCollegeMapper.insertBaseCollege(baseCollege);
    }

    /**
     * 修改院校信息
     * 
     * @param baseCollege 院校信息
     * @return 结果
     */
    @Override
    public int updateBaseCollege(BaseCollege baseCollege)
    {
        return baseCollegeMapper.updateBaseCollege(baseCollege);
    }

    /**
     * 批量删除院校信息
     * 
     * @param collegeIds 需要删除的院校信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseCollegeByCollegeIds(Long[] collegeIds)
    {
        return baseCollegeMapper.deleteBaseCollegeByCollegeIds(collegeIds);
    }

    /**
     * 删除院校信息信息
     * 
     * @param collegeId 院校信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseCollegeByCollegeId(Long collegeId)
    {
        return baseCollegeMapper.deleteBaseCollegeByCollegeId(collegeId);
    }
}
