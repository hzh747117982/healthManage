package hzh.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.MedicineType;
import hzh.health.mapper.MedicineTypeMapper;
import hzh.health.service.MedicineTypeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-15
 */
@Service
public class MedicineTypeServiceImpl extends ServiceImpl<MedicineTypeMapper, MedicineType> implements MedicineTypeService {

    @Resource
    private MedicineTypeMapper medicineTypeMapper;
    @Override
    public IPage pageCC(IPage<MedicineType> page, Wrapper wrapper) {
        return medicineTypeMapper.pageCC(page,wrapper);
    }
}
