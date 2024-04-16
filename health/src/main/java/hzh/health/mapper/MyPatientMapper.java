package hzh.health.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import hzh.health.entity.MedicineType;
import hzh.health.entity.MyPatient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPatientMapper extends BaseMapper<MyPatient> {

    IPage pageCC(IPage<MyPatient> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
