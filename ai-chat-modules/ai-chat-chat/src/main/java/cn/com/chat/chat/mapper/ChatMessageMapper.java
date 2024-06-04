package cn.com.chat.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.com.chat.chat.domain.ChatMessage;
import cn.com.chat.chat.domain.vo.ChatMessageVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 对话消息Mapper接口
 *
 * @author JiaZH
 * @date 2024-05-22
 */
@Mapper
public interface ChatMessageMapper extends BaseMapperPlus<ChatMessage, ChatMessageVo> {

}