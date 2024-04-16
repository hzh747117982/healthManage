package hzh.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author hzh
 * @date 2024/3/9 15:40
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;
    @TableId(value = "user_id", type = IdType.AUTO)
    private  int userId;

    private  String number;

    private  String name;

    private  String password;

    private  int sex;

    private  int age;

    private  int roleId;

    private  String phone;

}
