package cn.com.chat.chat.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.com.chat.chat.domain.Model;
import cn.com.chat.chat.domain.vo.ModelVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 模型信息Mapper接口
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Mapper
public interface ModelMapper extends BaseMapperPlus<Model, ModelVo> {

}
