package hzh.health.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="")
public class Menu {

    private static final long serialVersionUID = 1L;

    private Integer id;


    @ApiModelProperty(value = "菜单名字")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty(value = "菜单的父code")
    @TableField("menu_parent_code")
    private String menuParentCode;

    @ApiModelProperty(value = "点击触发的函数")
    @TableField("menu_click")
    private String menuClick;

    @ApiModelProperty(value = "权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用")
    @TableField("menu_right")
    private String menuRight;

    @TableField("menu_component")
    private String menuComponent;

    @TableField("menu_icon")
    private String menuIcon;


}
