import request from '@/utils/request'
import type { AjaxResult, TableDataInfo } from '@/types'
import type { BaseStudent, BaseStudentQueryParams } from '@/types/api/base/student'

// 查询学生信息列表
export function listStudent(query: BaseStudentQueryParams): Promise<TableDataInfo<BaseStudent[]>> {
  return request({
    url: '/base/student/list',
    method: 'get',
    params: query
  })
}

// 查询学生信息详细
export function getStudent(studentId: number): Promise<AjaxResult<BaseStudent>> {
  return request({
    url: '/base/student/' + studentId,
    method: 'get'
  })
}

// 新增学生信息
export function addStudent(data: BaseStudent): Promise<AjaxResult> {
  return request({
    url: '/base/student',
    method: 'post',
    data: data
  })
}

// 修改学生信息
export function updateStudent(data: BaseStudent): Promise<AjaxResult> {
  return request({
    url: '/base/student',
    method: 'put',
    data: data
  })
}

// 删除学生信息
export function delStudent(studentId: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/base/student/' + studentId,
    method: 'delete'
  })
}
