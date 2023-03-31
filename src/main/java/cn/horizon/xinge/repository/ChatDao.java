package cn.horizon.xinge.repository;

import cn.horizon.xinge.domain.ChatMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author horizon
 * @create 2023/3/30 21:29
 **/
@Repository
public interface ChatDao extends BaseMapper<ChatMsg> {

    void signMsg(List<String> msgIdList);

}
