package com.threeRduReform.base.service;

import java.util.List;
import com.threeRduReform.base.domain.BaseMajor;

/**
 * 专业信息Service接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface IBaseMajorService 
{
    /**
     * 查询专业信息
     * 
     * @param majorId 专业信息主键
     * @return 专业信息
     */
    public BaseMajor selectBaseMajorByMajorId(Long majorId);

    /**
     * 查询专业信息列表
     * 
     * @param baseMajor 专业信息
     * @return 专业信息集合
     */
    public List<BaseMajor> selectBaseMajorList(BaseMajor baseMajor);

    /**
     * 新增专业信息
     * 
     * @param baseMajor 专业信息
     * @return 结果
     */
    public int insertBaseMajor(BaseMajor baseMajor);

    /**
     * 修改专业信息
     * 
     * @param baseMajor 专业信息
     * @return 结果
     */
    public int updateBaseMajor(BaseMajor baseMajor);

    /**
     * 批量删除专业信息
     * 
     * @param majorIds 需要删除的专业信息主键集合
     * @return 结果
     */
    public int deleteBaseMajorByMajorIds(Long[] majorIds);

    /**
     * 删除专业信息信息
     * 
     * @param majorId 专业信息主键
     * @return 结果
     */
    public int deleteBaseMajorByMajorId(Long majorId);
}
