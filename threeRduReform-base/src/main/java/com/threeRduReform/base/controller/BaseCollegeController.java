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
import com.threeRduReform.common.enums.BusinessType;
import com.threeRduReform.base.domain.BaseCollege;
import com.threeRduReform.base.service.IBaseCollegeService;
import com.threeRduReform.common.utils.poi.ExcelUtil;

/**
 * 院校信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/base/college")
public class BaseCollegeController extends BaseController
{
    @Autowired
    private IBaseCollegeService baseCollegeService;

    /**
     * 查询院校信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:college:list')")
    @GetMapping("/list")
    public AjaxResult list(BaseCollege baseCollege)
    {
        List<BaseCollege> list = baseCollegeService.selectBaseCollegeTreeList(baseCollege);
        return success(list);
    }

    /**
     * 导出院校信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:college:export')")
    @Log(title = "院校信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseCollege baseCollege)
    {
        List<BaseCollege> list = baseCollegeService.selectBaseCollegeList(baseCollege);
        ExcelUtil<BaseCollege> util = new ExcelUtil<BaseCollege>(BaseCollege.class);
        util.exportExcel(response, list, "院校信息数据");
    }

    /**
     * 获取院校信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:college:query')")
    @GetMapping(value = "/{collegeId}")
    public AjaxResult getInfo(@PathVariable("collegeId") Long collegeId)
    {
        return success(baseCollegeService.selectBaseCollegeByCollegeId(collegeId));
    }

    /**
     * 新增院校信息
     */
    @PreAuthorize("@ss.hasPermi('base:college:add')")
    @Log(title = "院校信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseCollege baseCollege)
    {
        return toAjax(baseCollegeService.insertBaseCollege(baseCollege));
    }

    /**
     * 修改院校信息
     */
    @PreAuthorize("@ss.hasPermi('base:college:edit')")
    @Log(title = "院校信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseCollege baseCollege)
    {
        return toAjax(baseCollegeService.updateBaseCollege(baseCollege));
    }

    /**
     * 删除院校信息
     */
    @PreAuthorize("@ss.hasPermi('base:college:remove')")
    @Log(title = "院校信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{collegeIds}")
    public AjaxResult remove(@PathVariable Long[] collegeIds)
    {
        return toAjax(baseCollegeService.deleteBaseCollegeByCollegeIds(collegeIds));
    }
}
