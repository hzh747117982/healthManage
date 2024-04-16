package hzh.health.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hzh.health.entity.Medicine;

public interface MedicineService extends IService<Medicine> {
    IPage pageCC(IPage<Medicine> page, Wrapper wrapper);
}
