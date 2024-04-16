package hzh.health.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.MyPatient;
import hzh.health.entity.User;
import hzh.health.service.MyPatientService;
import hzh.health.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/myPatient")
public class MyPatientController {
    @Autowired
    private MyPatientService myPatientService;

    @Autowired()
    private UserService userService;
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody MyPatient myPatient) {
        return myPatientService.save(myPatient) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody MyPatient myPatient) {
        return myPatientService.updateById(myPatient) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return myPatientService.removeById(id) ? Result.suc() : Result.fail();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrmod(@RequestBody HashMap form) {
        String phone = (String)form.get("phone");
        String remark = (String)form.get("remark");
        int manageId = (int)form.get("manageId");
        List list = userService.lambdaQuery().eq(User::getPhone,phone).list();
        MyPatient myPatient = new MyPatient();
        User user = (User)list.get(0);
        myPatient.setPatientId(user.getUserId());
        myPatient.setPatientName(user.getName());
        myPatient.setRemark(remark);
        myPatient.setManageId(manageId);
        myPatient.setPhone(phone);
        if(myPatientService.saveOrUpdate(myPatient)){
            return Result.suc();
        }
        return Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String manageId = (String) param.get("manageId");
        String name = (String) param.get("name");
        System.out.println(manageId);
        Page<MyPatient> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<MyPatient> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(manageId) && !"null".equals(manageId)) {
            lambdaQueryWrapper.like(MyPatient::getManageId, manageId);
        }

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(MyPatient::getPatientName, name);
        }
        IPage result = myPatientService.pageCC(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    @GetMapping("/list")
    public Result list() {
        List list = myPatientService.list();
        return Result.suc(list);
    }
}
