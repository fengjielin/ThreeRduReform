/** 查询参数 */
export interface BaseTeacherQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页数量 */
  pageSize?: number
  /** 教师工号 */
  teacherCode?: string
  /** 教师姓名 */
  teacherName?: string
  /** 教师类型 */
  teacherType?: string
  /** 性别 */
  sex?: string
  /** 所属院校 */
  collegeId?: number
  /** 状态 */
  status?: string
}

/** 实体类 */
export interface BaseTeacher {
  /** 教师ID */
  teacherId?: number
  /** 教师工号 */
  teacherCode?: string
  /** 教师姓名 */
  teacherName?: string
  /** 教师类型（enterprise企业导师 teacher教师） */
  teacherType?: string
  /** 性别（0男 1女） */
  sex?: string
  /** 手机号码 */
  phone?: string
  /** 邮箱 */
  email?: string
  /** 个人照片 */
  avatar?: string
  /** 个人简介 */
  intro?: string
  /** 所属院校 */
  collegeId?: number
  /** 显示顺序 */
  sort?: number
  /** 状态（0正常 1停用） */
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
