package com.threeRduReform.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.threeRduReform.common.annotation.Log;
import com.threeRduReform.common.core.controller.BaseController;
import com.threeRduReform.common.core.domain.AjaxResult;
import com.threeRduReform.common.core.page.TableDataInfo;
import com.threeRduReform.common.enums.BusinessType;
import com.threeRduReform.base.domain.BaseTeacher;
import com.threeRduReform.base.service.IBaseTeacherService;
import com.threeRduReform.common.utils.poi.ExcelUtil;

/**
 * 教师信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/base/teacher")
public class BaseTeacherController extends BaseController
{
    @Autowired
    private IBaseTeacherService baseTeacherService;

    /**
     * 查询教师信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseTeacher baseTeacher)
    {
        startPage();
        List<BaseTeacher> list = baseTeacherService.selectBaseTeacherList(baseTeacher);
        return getDataTable(list);
    }

    /**
     * 导出教师信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:export')")
    @Log(title = "教师信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseTeacher baseTeacher)
    {
        List<BaseTeacher> list = baseTeacherService.selectBaseTeacherList(baseTeacher);
        ExcelUtil<BaseTeacher> util = new ExcelUtil<BaseTeacher>(BaseTeacher.class);
        util.exportExcel(response, list, "教师信息数据");
    }

    /**
     * 获取教师信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:query')")
    @GetMapping(value = "/{teacherId}")
    public AjaxResult getInfo(@PathVariable("teacherId") Long teacherId)
    {
        return success(baseTeacherService.selectBaseTeacherByTeacherId(teacherId));
    }

    /**
     * 新增教师信息
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:add')")
    @Log(title = "教师信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseTeacher baseTeacher)
    {
        return toAjax(baseTeacherService.insertBaseTeacher(baseTeacher));
    }

    /**
     * 修改教师信息
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:edit')")
    @Log(title = "教师信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseTeacher baseTeacher)
    {
        return toAjax(baseTeacherService.updateBaseTeacher(baseTeacher));
    }

    /**
     * 删除教师信息
     */
    @PreAuthorize("@ss.hasPermi('base:teacher:remove')")
    @Log(title = "教师信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{teacherIds}")
    public AjaxResult remove(@PathVariable Long[] teacherIds)
    {
        return toAjax(baseTeacherService.deleteBaseTeacherByTeacherIds(teacherIds));
    }
}
