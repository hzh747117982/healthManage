package hzh.health.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.Medicine;
import hzh.health.entity.MyPatient;
import hzh.health.entity.Record;
import hzh.health.entity.User;
import hzh.health.service.MedicineService;
import hzh.health.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;


@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private MedicineService medicineService;
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        int adminId = (int)param.get("adminId");
        String medicineTypeId = (String)param.get("medicineTypeId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Record> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Record::getAdminId, adminId);
        if (StringUtils.isNotBlank(medicineTypeId) && !"null".equals(medicineTypeId)) {
            lambdaQueryWrapper.like(Record::getMedicineTypeId, medicineTypeId);
        }
        IPage result = recordService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Medicine medicine = medicineService.getById(record.getMedicineId());
        System.out.println(record);
        int n = record.getCount();
        //出库
        if("2".equals(record.getAction())){
             n = -n;
             record.setCount(n);
             Medicine medicine2 = new Medicine();
             medicine2.setName(medicine.getName());
             medicine2.setMedicineType(medicine.getMedicineType());
             medicine2.setRemark(record.getRemark());
             medicine2.setCount(-n);
             medicine2.setPossessorId(record.getPatientId());
            medicineService.save(medicine2);
        }
        int num = medicine.getCount()+n;
        medicine.setCount(num);
        medicineService.updateById(medicine);
        LocalDateTime dateTime = LocalDateTime.now();

        record.setCreatetime(dateTime);
        return recordService.save(record)?Result.suc():Result.fail();
    }
}
