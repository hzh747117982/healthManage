package hzh.health.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.Medicine;
import hzh.health.entity.Record;
import hzh.health.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/medicine")
public class MedicineController {

   @Autowired
    private MedicineService medicineService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Medicine medicine){
        return medicineService.save(medicine)?Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Medicine medicine){
        return medicineService.updateById(medicine)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id){
        return medicineService.removeById(id)?Result.suc():Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrmod(@RequestBody Medicine goods) {
        if(medicineService.saveOrUpdate(goods)){
            return Result.suc();
        }
        return Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        String medicineType = (String)param.get("medicineType");
        String possessorId = (String)param.get("possessorId");

        Page<Medicine> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Medicine> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(name) && !"null".equals(name)){
            lambdaQueryWrapper.like(Medicine::getName,name);
        }
        if(StringUtils.isNotBlank(medicineType) && !"null".equals(medicineType)){
            lambdaQueryWrapper.eq(Medicine::getMedicineType,medicineType);
        }
        lambdaQueryWrapper.eq(Medicine::getPossessorId,possessorId);
        IPage result = medicineService.pageCC(page,lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    //
    @PostMapping("/list")
    public Result list(){
        return Result.suc(medicineService.list());
    }
}
