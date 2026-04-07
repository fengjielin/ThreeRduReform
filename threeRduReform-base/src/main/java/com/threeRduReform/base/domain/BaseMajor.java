package com.threeRduReform.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.threeRduReform.common.annotation.Excel;
import com.threeRduReform.common.core.domain.BaseEntity;

/**
 * 专业信息对象 base_major
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class BaseMajor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 专业ID */
    @Excel(name = "专业ID")
    private Long majorId;

    /** 专业编码 */
    @Excel(name = "专业编码")
    private String majorCode;

    /** 专业名称 */
    @Excel(name = "专业名称")
    private String majorName;

    /** 专业简称 */
    @Excel(name = "专业简称")
    private String majorShortName;

    /** 专业类别 */
    @Excel(name = "专业类别")
    private String majorCategory;

    /** 学制 */
    @Excel(name = "学制")
    private String duration;

    /** 学历层次 */
    @Excel(name = "学历层次")
    private String eduLevel;

    /** 专业简介 */
    @Excel(name = "专业简介")
    private String intro;

    /** 关联院校ID（非数据库字段） */
    private Long collegeId;

    /** 关联院校名称（非数据库字段） */
    private String collegeName;

    /** 专业状态（0正常 1停用） */
    @Excel(name = "专业状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMajorId(Long majorId) 
    {
        this.majorId = majorId;
    }

    public Long getMajorId() 
    {
        return majorId;
    }

    public void setMajorCode(String majorCode) 
    {
        this.majorCode = majorCode;
    }

    public String getMajorCode() 
    {
        return majorCode;
    }

    public void setMajorName(String majorName) 
    {
        this.majorName = majorName;
    }

    public String getMajorName() 
    {
        return majorName;
    }

    public void setMajorShortName(String majorShortName) 
    {
        this.majorShortName = majorShortName;
    }

    public String getMajorShortName() 
    {
        return majorShortName;
    }

    public void setMajorCategory(String majorCategory) 
    {
        this.majorCategory = majorCategory;
    }

    public String getMajorCategory() 
    {
        return majorCategory;
    }

    public void setDuration(String duration) 
    {
        this.duration = duration;
    }

    public String getDuration() 
    {
        return duration;
    }

    public void setEduLevel(String eduLevel) 
    {
        this.eduLevel = eduLevel;
    }

    public String getEduLevel() 
    {
        return eduLevel;
    }

    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }

    public void setCollegeId(Long collegeId)
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId()
    {
        return collegeId;
    }

    public void setCollegeName(String collegeName)
    {
        this.collegeName = collegeName;
    }

    public String getCollegeName()
    {
        return collegeName;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("majorId", getMajorId())
            .append("majorCode", getMajorCode())
            .append("majorName", getMajorName())
            .append("majorShortName", getMajorShortName())
            .append("majorCategory", getMajorCategory())
            .append("duration", getDuration())
            .append("eduLevel", getEduLevel())
            .append("intro", getIntro())
            .append("collegeId", getCollegeId())
            .append("collegeName", getCollegeName())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
