package hzh.health.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.MyPatient;
import hzh.health.mapper.MyPatientMapper;
import hzh.health.service.MyPatientService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-04
 */
@Service
public class MyPatientServiceImpl extends ServiceImpl<MyPatientMapper, MyPatient> implements MyPatientService {
    @Resource
    private MyPatientMapper myPatientMapper;

    @Override
    public IPage pageCC(IPage<MyPatient> page, Wrapper wrapper) {
        return myPatientMapper.pageCC(page,wrapper);
    }
}

