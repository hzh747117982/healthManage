package hzh.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.CheckIn;
import hzh.health.entity.CheckIn;
import hzh.health.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/checkIn")
public class CheckInController {
    @Autowired
    private CheckService checkService;
    static Timer timer = new Timer();
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date date = new Date();

    @PostMapping("/save")
    public Result send(@RequestBody Map mp) {
        int dayNum = 0;
        CheckIn check = new CheckIn();
        System.out.println(check.getLastTime());
        boolean flag = false;
        if (!((String) mp.get("timeValue1")).isEmpty()) {
            String time1 = (String) mp.get("timeValue1");

            time1 = time1.replace("Z", " UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat chinaSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(time1);
                String result = chinaSdf.format(date);
                date = chinaSdf.parse(result);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                if (!flag) {
                    check.setLastTime(date);
                    flag = true;
                }
                for (int i = 1; i <= (int) mp.get("num"); i++) {
                    // 把日期往后增加一天,整数  往后推,负数往前移动
                    // 这个时间就是日期往后推一天的结果
                    date = calendar.getTime();
                    timer.schedule(new DoSomethingTimerTask(mp, checkService), date);
                    calendar.add(Calendar.DATE, 1);
                }
            } catch (Exception e) {
                date = null;
            }
        }
        if (!((String) mp.get("timeValue2")).isEmpty()) {
            String time1 = (String) mp.get("timeValue2");
            time1 = time1.replace("Z", " UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat chinaSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(time1);
                String result = chinaSdf.format(date);
                date = chinaSdf.parse(result);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                System.out.println(date.toString() + "444");
                if (!flag) {
                    check.setLastTime(date);
                    flag = true;
                }
                for (int i = 1; i <= (int) mp.get("num"); i++) {
                    // 把日期往后增加一天,整数  往后推,负数往前移动
                    // 这个时间就是日期往后推一天的结果
                    date = calendar.getTime();
                    timer.schedule(new DoSomethingTimerTask(mp, checkService), date);
                    calendar.add(Calendar.DATE, 1);
                }
            } catch (Exception e) {
                date = null;
            }
        }
        if (!((String) mp.get("timeValue3")).isEmpty()) {
            String time1 = (String) mp.get("timeValue3");
            time1 = time1.replace("Z", " UTC");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat chinaSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(time1);
                String result = chinaSdf.format(date);
                date = chinaSdf.parse(result);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                if (!flag) {
                    check.setLastTime(date);
                    flag = true;
                }
                for (int i = 1; i <= (int) mp.get("num"); i++) {
                    // 把日期往后增加一天,整数  往后推,负数往前移动
                    // 这个时间就是日期往后推一天的结果
                    date = calendar.getTime();
                    timer.schedule(new DoSomethingTimerTask(mp, checkService), date);
                    calendar.add(Calendar.DATE, 1);
                }
            } catch (Exception e) {
                date = null;
            }
        }
        //添加check表的内容
        check.setMedicineName((String) mp.get("name1"));
        check.setPatientId((int) mp.get("patientId"));
        check.setManageId((int) mp.get("manageId"));
        check.setCount((int) mp.get("count"));
        check.setPatientName((String) mp.get("name"));
        check.setPhone((String) mp.get("phone"));
        check.setChecked('0');
        checkService.save(check);
        mp.put("checkId", check.getId());

        System.out.println(date);
        System.out.println(mp);
        return Result.suc();
    }

    //
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();//除分页之后的参数
        Page<CheckIn> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<CheckIn> lambdaQueryWrapper = new LambdaQueryWrapper();
        Integer manageId = (Integer) param.get("manageId");
        String name = (String) param.get("name");

        lambdaQueryWrapper.eq(CheckIn::getManageId, manageId);

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(CheckIn::getPatientName, name);
        }
        IPage result = checkService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }

    //找到打卡记录
    @GetMapping("/findCheck")
    public Result listPage1(@RequestParam Integer id) {

        CheckIn check = checkService.getById(id);
        return Result.suc(check);
    }

    //打卡操作
    @GetMapping("/doCheck")
    public Result doCheck(@RequestParam Integer id) {
        Date time1 = new Date();
        System.out.println(time1);
        CheckIn check = checkService.getById(id);
        System.out.println(check.getLastTime());
        long diff = time1.getTime() - check.getLastTime().getTime();
        System.out.println(diff);
        if(diff < 0){
            return Result.fail1();
        }
        if((diff > 1000 * 15 * 60)){
            return Result.fail();
        }
        check.setChecked('1');
        int n = check.getNotCheckIn();
        int m = check.getCheckIn();//未打卡少1，已打卡多1。
        check.setNotCheckIn(n - 1);
        check.setCheckIn(m + 1);
        checkService.saveOrUpdate(check);
        return Result.suc(check);
    }
}