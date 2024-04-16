package hzh.health.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.MedicineType;
import hzh.health.service.MedicineTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/medicineType")
public class MedicineTypeController {
    @Autowired
    private MedicineTypeService medicineTypeService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody MedicineType medicineType) {
        return medicineTypeService.save(medicineType) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody MedicineType medicineType) {
        return medicineTypeService.updateById(medicineType) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return medicineTypeService.removeById(id) ? Result.suc() : Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrmod(@RequestBody MedicineType medicineType) {
        if(medicineTypeService.saveOrUpdate(medicineType)){
            return Result.suc();
        }
        return Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<MedicineType> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<MedicineType> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(MedicineType::getName, name);
        }

        IPage result = medicineTypeService.pageCC(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List list = medicineTypeService.list();
        return Result.suc(list);
    }
}
