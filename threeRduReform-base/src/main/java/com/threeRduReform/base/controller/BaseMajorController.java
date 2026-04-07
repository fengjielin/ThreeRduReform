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
import com.threeRduReform.base.domain.BaseMajor;
import com.threeRduReform.base.service.IBaseMajorService;
import com.threeRduReform.common.utils.poi.ExcelUtil;

/**
 * 专业信息Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@RestController
@RequestMapping("/base/major")
public class BaseMajorController extends BaseController
{
    @Autowired
    private IBaseMajorService baseMajorService;

    /**
     * 查询专业信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:major:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseMajor baseMajor)
    {
        startPage();
        List<BaseMajor> list = baseMajorService.selectBaseMajorList(baseMajor);
        return getDataTable(list);
    }

    /**
     * 导出专业信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:major:export')")
    @Log(title = "专业信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseMajor baseMajor)
    {
        List<BaseMajor> list = baseMajorService.selectBaseMajorList(baseMajor);
        ExcelUtil<BaseMajor> util = new ExcelUtil<BaseMajor>(BaseMajor.class);
        util.exportExcel(response, list, "专业信息数据");
    }

    /**
     * 获取专业信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:major:query')")
    @GetMapping(value = "/{majorId}")
    public AjaxResult getInfo(@PathVariable("majorId") Long majorId)
    {
        return success(baseMajorService.selectBaseMajorByMajorId(majorId));
    }

    /**
     * 新增专业信息
     */
    @PreAuthorize("@ss.hasPermi('base:major:add')")
    @Log(title = "专业信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseMajor baseMajor)
    {
        return toAjax(baseMajorService.insertBaseMajor(baseMajor));
    }

    /**
     * 修改专业信息
     */
    @PreAuthorize("@ss.hasPermi('base:major:edit')")
    @Log(title = "专业信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseMajor baseMajor)
    {
        return toAjax(baseMajorService.updateBaseMajor(baseMajor));
    }

    /**
     * 删除专业信息
     */
    @PreAuthorize("@ss.hasPermi('base:major:remove')")
    @Log(title = "专业信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{majorIds}")
    public AjaxResult remove(@PathVariable Long[] majorIds)
    {
        return toAjax(baseMajorService.deleteBaseMajorByMajorIds(majorIds));
    }
}
