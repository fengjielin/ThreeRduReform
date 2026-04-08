import request from '@/utils/request'
import type { AjaxResult } from '@/types'
import type { BaseCollege, BaseCollegeQueryParams } from '@/types/api/base/college'

// 查询院校信息列表
export function listCollege(query: BaseCollegeQueryParams): Promise<AjaxResult<BaseCollege[]>> {
  return request({
    url: '/base/college/list',
    method: 'get',
    params: query
  })
}

// 查询院校信息详细
export function getCollege(collegeId: number): Promise<AjaxResult<BaseCollege>> {
  return request({
    url: '/base/college/' + collegeId,
    method: 'get'
  })
}

// 新增院校信息
export function addCollege(data: BaseCollege): Promise<AjaxResult> {
  return request({
    url: '/base/college',
    method: 'post',
    data: data
  })
}

// 修改院校信息
export function updateCollege(data: BaseCollege): Promise<AjaxResult> {
  return request({
    url: '/base/college',
    method: 'put',
    data: data
  })
}

// 删除院校信息
export function delCollege(collegeId: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/base/college/' + collegeId,
    method: 'delete'
  })
}
