package com.threeRduReform.base.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.threeRduReform.common.annotation.Excel;
import com.threeRduReform.common.core.domain.BaseEntity;

/**
 * 教师信息对象 base_teacher
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class BaseTeacher extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 教师ID */
    @Excel(name = "教师ID")
    private Long teacherId;

    /** 教师工号 */
    @Excel(name = "教师工号")
    private String teacherCode;

    /** 教师姓名 */
    @Excel(name = "教师姓名")
    private String teacherName;

    /** 教师类型（enterprise企业导师 teacher教师） */
    @Excel(name = "教师类型", readConverterExp = "enterprise=企业导师,teacher=教师")
    private String teacherType;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String sex;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 个人照片 */
    @Excel(name = "个人照片")
    private String avatar;

    /** 个人简介 */
    @Excel(name = "个人简介")
    private String intro;

    /** 所属院校 */
    @Excel(name = "所属院校")
    private Long collegeId;

    /** 所属院校名称（非数据库字段） */
    private String collegeName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer sort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setTeacherId(Long teacherId) 
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() 
    {
        return teacherId;
    }

    public void setTeacherCode(String teacherCode) 
    {
        this.teacherCode = teacherCode;
    }

    public String getTeacherCode() 
    {
        return teacherCode;
    }

    public void setTeacherName(String teacherName) 
    {
        this.teacherName = teacherName;
    }

    public String getTeacherName() 
    {
        return teacherName;
    }

    public void setTeacherType(String teacherType) 
    {
        this.teacherType = teacherType;
    }

    public String getTeacherType() 
    {
        return teacherType;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
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

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
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
            .append("teacherId", getTeacherId())
            .append("teacherCode", getTeacherCode())
            .append("teacherName", getTeacherName())
            .append("teacherType", getTeacherType())
            .append("sex", getSex())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("avatar", getAvatar())
            .append("intro", getIntro())
            .append("collegeId", getCollegeId())
            .append("collegeName", getCollegeName())
            .append("sort", getSort())
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
