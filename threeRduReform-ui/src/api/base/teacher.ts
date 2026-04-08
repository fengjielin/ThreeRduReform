import request from '@/utils/request'
import type { AjaxResult, TableDataInfo } from '@/types'
import type { BaseTeacher, BaseTeacherQueryParams } from '@/types/api/base/teacher'

// 查询教师信息列表
export function listTeacher(query: BaseTeacherQueryParams): Promise<TableDataInfo<BaseTeacher[]>> {
  return request({
    url: '/base/teacher/list',
    method: 'get',
    params: query
  })
}

// 查询教师信息详细
export function getTeacher(teacherId: number): Promise<AjaxResult<BaseTeacher>> {
  return request({
    url: '/base/teacher/' + teacherId,
    method: 'get'
  })
}

// 新增教师信息
export function addTeacher(data: BaseTeacher): Promise<AjaxResult> {
  return request({
    url: '/base/teacher',
    method: 'post',
    data: data
  })
}

// 修改教师信息
export function updateTeacher(data: BaseTeacher): Promise<AjaxResult> {
  return request({
    url: '/base/teacher',
    method: 'put',
    data: data
  })
}

// 删除教师信息
export function delTeacher(teacherId: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/base/teacher/' + teacherId,
    method: 'delete'
  })
}