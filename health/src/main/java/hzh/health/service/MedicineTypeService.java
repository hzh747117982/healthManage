package hzh.health.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import hzh.health.entity.MedicineType;
import hzh.health.entity.MedicineType;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
public interface MedicineTypeService extends IService<MedicineType> {
    IPage pageCC(IPage<MedicineType> page, Wrapper wrapper);
}
