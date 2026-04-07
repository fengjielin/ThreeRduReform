/** 查询参数 */
export interface BaseMajorQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页数量 */
  pageSize?: number
  /** 专业编码 */
  majorCode?: string
  /** 专业名称 */
  majorName?: string
  /** 专业类别 */
  majorCategory?: string
  /** 学历层次 */
  eduLevel?: string
  /** 专业状态 */
  status?: string
}

/** 实体类 */
export interface BaseMajor {
  /** 专业ID */
  majorId?: number
  /** 专业编码 */
  majorCode?: string
  /** 专业名称 */
  majorName?: string
  /** 专业简称 */
  majorShortName?: string
  /** 专业类别 */
  majorCategory?: string
  /** 学制 */
  duration?: string
  /** 学历层次 */
  eduLevel?: string
  /** 专业简介 */
  intro?: string
  /** 专业状态（0正常 1停用） */
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
