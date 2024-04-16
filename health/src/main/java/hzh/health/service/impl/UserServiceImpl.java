package hzh.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import hzh.health.entity.User;
import hzh.health.mapper.UserMapper;
import hzh.health.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author hzh
 * @date 2024/3/9 15:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
