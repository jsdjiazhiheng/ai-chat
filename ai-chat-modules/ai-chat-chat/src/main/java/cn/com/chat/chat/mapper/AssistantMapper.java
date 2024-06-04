package cn.com.chat.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.com.chat.chat.domain.Assistant;
import cn.com.chat.chat.domain.vo.AssistantVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * AI助手Mapper接口
 *
 * @author JiaZH
 * @date 2024-05-23
 */
@Mapper
public interface AssistantMapper extends BaseMapperPlus<Assistant, AssistantVo> {

}
