package cn.horizon.xinge.repository;

import cn.horizon.xinge.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author horizon
 * @create 2023/3/24 22:41
 **/
@Repository
@Mapper
public interface UserDao extends BaseMapper<User> {
}
