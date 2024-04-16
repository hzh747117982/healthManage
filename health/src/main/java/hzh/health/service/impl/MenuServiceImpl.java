package hzh.health.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.Menu;
import hzh.health.mapper.MenuMapper;
import hzh.health.service.MenuService;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
