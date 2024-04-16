package hzh.health.controller;

import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import hzh.health.entity.CheckIn;
import hzh.health.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class DoSomethingTimerTask extends TimerTask {
    UniMessage message = UniSMS.buildMessage();
    Map map;

    public CheckService checkService;

    @Override
    public void run() {
        Uni.init("n6UHu6BWcQkveDKNrYRpkMmZxD8HJn6Vjc5B1fMnGYNvafAqu"); // 若使用简易验签模式仅传入第一个参数即可
        // 设置自定义参数 (变量短信)
        System.out.println((int)map.get("checkId"));
        CheckIn check = checkService.getById((int)map.get("checkId"));
        int n = check.getNotCheckIn() + 1;
        check.setNotCheckIn(n);
        Date date1 = new Date(System.currentTimeMillis());
        check.setLastTime(date1);
        check.setChecked('0');
        checkService.saveOrUpdate(check);
        Map<String, String> templateData = new HashMap<String, String>();
        templateData.put("code", "6666");
        // 构建信息
        message = UniSMS.buildMessage()
                .setTo((String) map.get("phone"))
                .setSignature("黄志豪")
                .setTemplateId("pub_verif_basic")
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println(res);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }
    public DoSomethingTimerTask(@RequestBody Map mp, CheckService checkService) {
        this.checkService = checkService;
        System.out.println("1313131313");
        this.map = mp;
    }

}
