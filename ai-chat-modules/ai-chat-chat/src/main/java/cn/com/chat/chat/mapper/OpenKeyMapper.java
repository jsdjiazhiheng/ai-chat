package cn.com.chat.chat.mapper;

import cn.com.chat.chat.domain.OpenKey;
import cn.com.chat.chat.domain.vo.OpenKeyVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 模型配置Mapper接口
 *
 * @author JiaZH
 * @date 2024-12-12
 */
@Mapper
public interface OpenKeyMapper extends BaseMapperPlus<OpenKey, OpenKeyVo> {

}
