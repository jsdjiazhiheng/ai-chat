package cn.com.chat.system.mapper;

import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;
import cn.com.chat.system.domain.SysOss;
import cn.com.chat.system.domain.vo.SysOssVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOssMapper extends BaseMapperPlus<SysOss, SysOssVo> {
}
