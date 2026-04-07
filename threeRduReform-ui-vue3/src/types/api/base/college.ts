/** 查询参数 */
export interface BaseCollegeQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页数量 */
  pageSize?: number
  /** 院校编码 */
  collegeCode?: string
  /** 院校名称 */
  collegeName?: string
  /** 院校类型 */
  collegeType?: string
  /** 院校状态 */
  status?: string
}

/** 实体类 */
export interface BaseCollege {
  /** 主键 */
  collegeId?: number
  /** 父院校ID */
  parentId?: number
  /** 祖级列表 */
  ancestors?: string
  /** 院校编码 */
  collegeCode?: string
  /** 院校名称 */
  collegeName?: string
  /** 院校简称 */
  collegeShortName?: string
  /** 院校类型 */
  collegeType?: string
  /** 所在地区 */
  area?: string
  /** 详细地址 */
  address?: string
  /** 联系人 */
  contact?: string
  /** 联系电话 */
  phone?: string
  /** 院校简介 */
  intro?: string
  /** 显示顺序 */
  orderNum?: number
  /** 院校状态（0正常 1停用） */
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
  /** 子部门 */
  children?: BaseCollege[]
}
