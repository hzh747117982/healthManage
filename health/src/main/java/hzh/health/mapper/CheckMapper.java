package hzh.health.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import hzh.health.entity.CheckIn;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface CheckMapper extends BaseMapper<CheckIn> {
}
