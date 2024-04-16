package hzh.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author hzh
 * @date 2024/3/19 12:29
 */
@Data
public class MyPatient {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private int manageId;

    private int patientId;

    private String patientName;

    private String remark;

    private String phone;
}
