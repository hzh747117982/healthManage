package hzh.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.Medicine;
import hzh.health.mapper.MedicineMapper;
import hzh.health.service.MedicineService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {
    @Resource
    private MedicineMapper medicineMapper;
    @Override
    public IPage pageCC(IPage<Medicine> page, Wrapper wrapper) {
        return medicineMapper.pageCC(page,wrapper);
    }
}
