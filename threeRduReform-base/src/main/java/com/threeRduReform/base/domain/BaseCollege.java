package com.threeRduReform.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.threeRduReform.common.annotation.Excel;
import com.threeRduReform.common.core.domain.TreeEntity;

/**
 * 院校信息对象 base_college
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class BaseCollege extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 院校ID */
    @Excel(name = "院校ID")
    private Long collegeId;

    /** 院校编码 */
    @Excel(name = "院校编码")
    private String collegeCode;

    /** 院校名称 */
    @Excel(name = "院校名称")
    private String collegeName;

    /** 院校简称 */
    @Excel(name = "院校简称")
    private String collegeShortName;

    /** 院校类型 */
    @Excel(name = "院校类型")
    private String collegeType;

    /** 所在地区 */
    @Excel(name = "所在地区")
    private String area;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 院校简介 */
    @Excel(name = "院校简介")
    private String intro;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 院校状态（0正常 1停用） */
    @Excel(name = "院校状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setCollegeId(Long collegeId) 
    {
        this.collegeId = collegeId;
    }

    public Long getCollegeId() 
    {
        return collegeId;
    }

    public void setCollegeCode(String collegeCode) 
    {
        this.collegeCode = collegeCode;
    }

    public String getCollegeCode() 
    {
        return collegeCode;
    }

    public void setCollegeName(String collegeName) 
    {
        this.collegeName = collegeName;
    }

    public String getCollegeName() 
    {
        return collegeName;
    }

    public void setCollegeShortName(String collegeShortName) 
    {
        this.collegeShortName = collegeShortName;
    }

    public String getCollegeShortName() 
    {
        return collegeShortName;
    }

    public void setCollegeType(String collegeType) 
    {
        this.collegeType = collegeType;
    }

    public String getCollegeType() 
    {
        return collegeType;
    }

    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setContact(String contact) 
    {
        this.contact = contact;
    }

    public String getContact() 
    {
        return contact;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }

    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
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
            .append("collegeId", getCollegeId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("collegeCode", getCollegeCode())
            .append("collegeName", getCollegeName())
            .append("collegeShortName", getCollegeShortName())
            .append("collegeType", getCollegeType())
            .append("area", getArea())
            .append("address", getAddress())
            .append("contact", getContact())
            .append("phone", getPhone())
            .append("intro", getIntro())
            .append("orderNum", getOrderNum())
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
