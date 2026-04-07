package com.threeRduReform.base.service;

import java.util.List;
import java.util.Map;
import com.threeRduReform.base.domain.BaseCollegeMajor;

/**
 * 院校与专业关联Service接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface IBaseCollegeMajorService 
{
    /**
     * 查询专业关联的院校列表
     * 
     * @param majorId 专业ID
     * @return 院校列表
     */
    public List<Map<String, Object>> selectCollegeListByMajorId(Long majorId);

    /**
     * 查询院校关联的专业列表
     * 
     * @param collegeId 院校ID
     * @return 专业列表
     */
    public List<Map<String, Object>> selectMajorListByCollegeId(Long collegeId);

    /**
     * 添加院校与专业关联
     * 
     * @param collegeMajor 关联信息
     * @return 结果
     */
    public int insertCollegeMajor(BaseCollegeMajor collegeMajor);

    /**
     * 删除院校与专业关联
     * 
     * @param majorId 专业ID
     * @return 结果
     */
    public int deleteCollegeMajorByMajorId(Long majorId);

    /**
     * 批量删除院校与专业关联
     * 
     * @param majorIds 专业ID集合
     * @return 结果
     */
    public int deleteCollegeMajorByMajorIds(Long[] majorIds);

    /**
     * 根据院校ID删除关联
     * 
     * @param collegeId 院校ID
     * @return 结果
     */
    public int deleteCollegeMajorByCollegeId(Long collegeId);
}
