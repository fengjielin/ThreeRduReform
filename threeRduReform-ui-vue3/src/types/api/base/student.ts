/** 查询参数 */
export interface BaseStudentQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页数量 */
  pageSize?: number
  /** 学号 */
  studentCode?: string
  /** 学生姓名 */
  studentName?: string
  /** 性别 */
  sex?: string
  /** 所属院校 */
  collegeId?: number
  /** 所学专业 */
  majorId?: number
  /** 年级 */
  grade?: string
  /** 状态 */
  status?: string
}

/** 实体类 */
export interface BaseStudent {
  /** 学生ID */
  studentId?: number
  /** 学号 */
  studentCode?: string
  /** 学生姓名 */
  studentName?: string
  /** 性别（0男 1女） */
  sex?: string
  /** 出生日期 */
  birthday?: Date
  /** 民族 */
  nation?: string
  /** 身份证号 */
  idCard?: string
  /** 手机号码 */
  phone?: string
  /** 所属院校 */
  collegeId?: number
  /** 所属院校名称 */
  collegeName?: string
  /** 所学专业 */
  majorId?: number
  /** 所学专业名称 */
  majorName?: string
  /** 年级 */
  grade?: string
  /** 班级 */
  className?: string
  /** 入校时间 */
  entranceDate?: Date
  /** 毕业时间 */
  graduationDate?: Date
  /** 毕业去向 */
  graduationDest?: string
  /** 状态（0在读 1毕业 2肄业） */
  status?: string
  /** 删除标志（0代表存在 2代表删除） */
  delFlag?: string
  /** 创建者 */
  createBy?: string
  /** 创建时间 */
  createTime?: Date
  /** 更新者 */
  updateBy?: string
  /** 更新时间 */
  updateTime?: Date
  /** 备注 */
  remark?: string
}
