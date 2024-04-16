package hzh.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import hzh.health.common.QueryPageParam;
import hzh.health.common.Result;
import hzh.health.entity.Menu;
import hzh.health.entity.MyPatient;
import hzh.health.entity.User;
import hzh.health.service.MenuService;
import hzh.health.service.MyPatientService;
import hzh.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniSMS;
import com.apistd.uni.sms.UniMessage;

import java.util.HashMap;
import java.util.List;

/**
 * @author hzh
 * @date 2024/1/23 13:27
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired()
    private UserService userService;
    @Autowired()
    private MyPatientService myPatientService;
    @Autowired()
    private MenuService menuService;
    //查询所有
    @GetMapping("/list")
    public Result list() {
        return Result.suc(Long.valueOf(userService.list().size()), userService.list());
    }
    //查询no是否存在
    @GetMapping("/findByNumber")
    public Result findByNo(@RequestParam String number) {
        List list = userService.lambdaQuery().eq(User::getNumber,number).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    //查询no是否存在
    @GetMapping("/findByPhone")
    public Result findByNumber(@RequestParam String phone) {
        List list = userService.lambdaQuery().eq(User::getPhone,phone).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }

    //新增,注册账号
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        if(userService.save(user)){
            return Result.suc();
        }
        return Result.suc();
    }
    //修改
    @PostMapping("/mod")
    public Result mod(@RequestBody User user) {
        if(userService.updateById(user)){
            return Result.suc();
        }
        return Result.suc();
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public Result saveOrmod(@RequestBody User user) {
        String md5str = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5str);
        MyPatient myPatient = new MyPatient();
        myPatient.setPatientName(user.getName());
        myPatient.setRemark("本人");
        myPatient.setPhone(user.getPhone());
        if(userService.saveOrUpdate(user)){
            System.out.println(user.getUserId());
            List userList = userService.lambdaQuery().eq(User::getNumber,user.getNumber()).list();
            User user2 = (User)userList.get(0);
            myPatient.setPatientId(user2.getUserId());
            myPatient.setManageId(user2.getUserId());
            myPatientService.save(myPatient);
            return Result.suc();

        }
        return Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String userId) {
        return userService.removeById(userId)?Result.suc():Result.fail();
    }
    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String md5str = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5str);
        List list = userService.lambdaQuery()
                .eq(User::getNumber, user.getNumber())
                .eq(User::getPassword, user.getPassword()).list();

        if(list.size() > 0 ){
            User user1 = (User) list.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuRight,user1.getRoleId()).list();
            HashMap res = new HashMap();
            res.put("user",user1);
            res.put("menu",menuList);
            return Result.suc(res);
        }
        return Result.fail();
    }

    //模糊（查询，匹配）
    @PostMapping("/listP")
    public List<User> listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(User::getName, user.getName());
        return userService.list(lambdaQueryWrapper);
    }

    //带参查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();//除分页之后的参数
        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");
        System.out.println(name);
        System.out.println(sex);
        System.out.println(roleId);
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex) && !"null".equals(sex)) {
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roleId)) {
            lambdaQueryWrapper.eq(User::getRoleId, roleId);
        }
        IPage result = userService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getTotal(), result.getRecords());
    }
}
