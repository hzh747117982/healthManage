package hzh.health.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class CheckIn {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty()
    private String medicineName;

    @ApiModelProperty(value = "为打卡次数")
    private Integer notCheckIn = 0;

    @ApiModelProperty(value = "打卡次数")
    private Integer checkIn = 0;

    private Integer patientId;

    private Date lastTime;

    private Integer manageId;

    private Integer count;

    private String patientName;

    private String phone;
    private Character checked;
}
