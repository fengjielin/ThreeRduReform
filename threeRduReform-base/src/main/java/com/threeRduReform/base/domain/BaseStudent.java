package com.threeRduReform.base.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.threeRduReform.common.annotation.Excel;
import com.threeRduReform.common.core.domain.BaseEntity;

/**
 * 学生信息对象 base_student
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public class BaseStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学生ID */
    @Excel(name = "学生ID")
    private Long studentId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentCode;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String sex;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 民族 */
    @Excel(name = "民族")
    private String nation;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 所属院校 */
    @Excel(name = "所属院校")
    private Long collegeId;

    /** 所属院校名称（非数据库字段） */
    private String collegeName;

    /** 所学专业 */
    @Excel(name = "所学专业")
    private Long majorId;

    /** 所学专业名称（非数据库字段） */
    private String majorName;

    /** 年级 */
    @Excel(name = "年级")
    private String grade;

    /** 班级 */
    @Excel(name = "班级")
    private String className;

    /** 入校时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入校时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entranceDate;

    /** 毕业时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "毕业时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date graduationDate;

    /** 毕业去向 */
    @Excel(name = "毕业去向")
    private String graduationDest;

    /** 状态（0在读 1毕业 2肄业） */
    @Excel(name = "状态", readConverterExp = "0=在读,1=毕业,2=肄业")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }

    public void setStudentCode(String studentCode) 
    {
        this.studentCode = studentCode;
    }

    public String getStudentCode() 
    {
        return studentCode;
    }

    public void setStudentName(String studentName) 
    {
        this.studentName = studentName;
    }

    public String getStudentName() 
    {
        return studentName;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }

    public void setNation(String nation) 
    {
        this.nation = nation;
    }

    public String getNation() 
    {
        return nation;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
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

    public void setMajorId(Long majorId) 
    {
        this.majorId = majorId;
    }

    public Long getMajorId() 
    {
        return majorId;
    }

    public void setMajorName(String majorName) 
    {
        this.majorName = majorName;
    }

    public String getMajorName() 
    {
        return majorName;
    }

    public void setGrade(String grade) 
    {
        this.grade = grade;
    }

    public String getGrade() 
    {
        return grade;
    }

    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getClassName() 
    {
        return className;
    }

    public void setEntranceDate(Date entranceDate) 
    {
        this.entranceDate = entranceDate;
    }

    public Date getEntranceDate() 
    {
        return entranceDate;
    }

    public void setGraduationDate(Date graduationDate) 
    {
        this.graduationDate = graduationDate;
    }

    public Date getGraduationDate() 
    {
        return graduationDate;
    }

    public void setGraduationDest(String graduationDest) 
    {
        this.graduationDest = graduationDest;
    }

    public String getGraduationDest() 
    {
        return graduationDest;
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
            .append("studentId", getStudentId())
            .append("studentCode", getStudentCode())
            .append("studentName", getStudentName())
            .append("sex", getSex())
            .append("birthday", getBirthday())
            .append("nation", getNation())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("collegeId", getCollegeId())
            .append("collegeName", getCollegeName())
            .append("majorId", getMajorId())
            .append("majorName", getMajorName())
            .append("grade", getGrade())
            .append("className", getClassName())
            .append("entranceDate", getEntranceDate())
            .append("graduationDate", getGraduationDate())
            .append("graduationDest", getGraduationDest())
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
