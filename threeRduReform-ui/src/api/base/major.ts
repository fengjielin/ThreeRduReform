import request from '@/utils/request'
import type { AjaxResult, TableDataInfo } from '@/types'
import type { BaseMajor, BaseMajorQueryParams } from '@/types/api/base/major'

// 查询专业信息列表
export function listMajor(query: BaseMajorQueryParams): Promise<TableDataInfo<BaseMajor[]>> {
  return request({
    url: '/base/major/list',
    method: 'get',
    params: query
  })
}

// 查询专业信息详细
export function getMajor(majorId: number): Promise<AjaxResult<BaseMajor>> {
  return request({
    url: '/base/major/' + majorId,
    method: 'get'
  })
}

// 新增专业信息
export function addMajor(data: BaseMajor): Promise<AjaxResult> {
  return request({
    url: '/base/major',
    method: 'post',
    data: data
  })
}

// 修改专业信息
export function updateMajor(data: BaseMajor): Promise<AjaxResult> {
  return request({
    url: '/base/major',
    method: 'put',
    data: data
  })
}

// 删除专业信息
export function delMajor(majorId: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/base/major/' + majorId,
    method: 'delete'
  })
}
