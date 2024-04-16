package hzh.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.CheckIn;
import hzh.health.mapper.CheckMapper;
import hzh.health.service.CheckService;
import org.springframework.stereotype.Service;

@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, CheckIn> implements CheckService {
}
