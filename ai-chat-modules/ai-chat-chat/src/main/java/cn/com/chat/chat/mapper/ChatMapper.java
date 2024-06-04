package cn.com.chat.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.com.chat.chat.domain.Chat;
import cn.com.chat.chat.domain.vo.ChatVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 对话Mapper接口
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Mapper
public interface ChatMapper extends BaseMapperPlus<Chat, ChatVo> {

}
