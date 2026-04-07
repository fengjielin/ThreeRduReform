package com.threeRduReform.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 院校与专业关联对象 base_college_major
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class BaseCollegeMajor
{
    private static final long serialVersionUID = 1L;

    /** 院校ID */
    private Long collegeId;

    /** 专业ID */
    private Long majorId;

    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }

    public void setMajorId(Long majorId) 
    {
        this.majorId = majorId;
    }

    public Long getMajorId() 
    {
        return majorId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("collegeId", getCollegeId())
            .append("majorId", getMajorId())
            .toString();
    }
}
