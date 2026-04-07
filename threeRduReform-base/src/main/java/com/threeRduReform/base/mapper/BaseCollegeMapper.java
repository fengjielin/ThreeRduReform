package com.threeRduReform.base.mapper;

import java.util.List;
import com.threeRduReform.base.domain.BaseCollege;

/**
 * 院校信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface BaseCollegeMapper 
{
    /**
     * 查询院校信息
     * 
     * @param collegeId 院校信息主键
     * @return 院校信息
     */
    public BaseCollege selectBaseCollegeByCollegeId(Long collegeId);

    /**
     * 查询院校信息列表
     * 
     * @param baseCollege 院校信息
     * @return 院校信息集合
     */
    public List<BaseCollege> selectBaseCollegeList(BaseCollege baseCollege);

    /**
     * 新增院校信息
     * 
     * @param baseCollege 院校信息
     * @return 结果
     */
    public int insertBaseCollege(BaseCollege baseCollege);

    /**
     * 修改院校信息
     * 
     * @param baseCollege 院校信息
     * @return 结果
     */
    public int updateBaseCollege(BaseCollege baseCollege);

    /**
     * 删除院校信息
     * 
     * @param collegeId 院校信息主键
     * @return 结果
     */
    public int deleteBaseCollegeByCollegeId(Long collegeId);

    /**
     * 批量删除院校信息
     * 
     * @param collegeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseCollegeByCollegeIds(Long[] collegeIds);

    /**
     * 查询院校信息树列表
     * 
     * @param baseCollege 院校信息
     * @return 院校信息集合
     */
    public List<BaseCollege> selectBaseCollegeTreeList(BaseCollege baseCollege);
}
