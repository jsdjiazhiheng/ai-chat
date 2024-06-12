package cn.com.chat.chat.mapper;

import cn.com.chat.chat.domain.SearchEngine;
import cn.com.chat.chat.domain.vo.SearchEngineVo;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索引擎Mapper接口
 *
 * @author JiaZH
 * @date 2024-05-07
 */
@Mapper
public interface SearchEngineMapper extends BaseMapperPlus<SearchEngine, SearchEngineVo> {

}
