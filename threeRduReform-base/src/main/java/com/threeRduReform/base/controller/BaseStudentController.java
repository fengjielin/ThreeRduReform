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
import com.threeRduReform.base.domain.BaseStudent;
import com.threeRduReform.base.service.IBaseStudentService;
import com.threeRduReform.common.utils.poi.ExcelUtil;

/**
 * 学生信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/base/student")
public class BaseStudentController extends BaseController
{
    @Autowired
    private IBaseStudentService baseStudentService;

    /**
     * 查询学生信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:student:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseStudent baseStudent)
    {
        startPage();
        List<BaseStudent> list = baseStudentService.selectBaseStudentList(baseStudent);
        return getDataTable(list);
    }

    /**
     * 导出学生信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:student:export')")
    @Log(title = "学生信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseStudent baseStudent)
    {
        List<BaseStudent> list = baseStudentService.selectBaseStudentList(baseStudent);
        ExcelUtil<BaseStudent> util = new ExcelUtil<BaseStudent>(BaseStudent.class);
        util.exportExcel(response, list, "学生信息数据");
    }

    /**
     * 获取学生信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:student:query')")
    @GetMapping(value = "/{studentId}")
    public AjaxResult getInfo(@PathVariable("studentId") Long studentId)
    {
        return success(baseStudentService.selectBaseStudentByStudentId(studentId));
    }

    /**
     * 新增学生信息
     */
    @PreAuthorize("@ss.hasPermi('base:student:add')")
    @Log(title = "学生信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseStudent baseStudent)
    {
        return toAjax(baseStudentService.insertBaseStudent(baseStudent));
    }

    /**
     * 修改学生信息
     */
    @PreAuthorize("@ss.hasPermi('base:student:edit')")
    @Log(title = "学生信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseStudent baseStudent)
    {
        return toAjax(baseStudentService.updateBaseStudent(baseStudent));
    }

    /**
     * 删除学生信息
     */
    @PreAuthorize("@ss.hasPermi('base:student:remove')")
    @Log(title = "学生信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{studentIds}")
    public AjaxResult remove(@PathVariable Long[] studentIds)
    {
        return toAjax(baseStudentService.deleteBaseStudentByStudentIds(studentIds));
    }
}
