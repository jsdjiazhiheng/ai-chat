package cn.com.chat.system.service;

import cn.com.chat.system.domain.SysClient;
import cn.com.chat.system.domain.vo.SysClientVo;
import cn.com.chat.system.domain.bo.SysClientBo;
import cn.com.chat.common.mybatis.core.page.TableDataInfo;
import cn.com.chat.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客户端管理Service接口
 *
 * @author Michelle.Chung
 * @date 2023-06-18
 */
public interface ISysClientService {

    /**
     * 查询客户端管理
     */
    SysClientVo queryById(Long id);

    /**
     * 查询客户端信息基于客户端id
     */
    SysClient queryByClientId(String clientId);

    /**
     * 查询客户端管理列表
     */
    TableDataInfo<SysClientVo> queryPageList(SysClientBo bo, PageQuery pageQuery);

    /**
     * 查询客户端管理列表
     */
    List<SysClientVo> queryList(SysClientBo bo);

    /**
     * 新增客户端管理
     */
    Boolean insertByBo(SysClientBo bo);

    /**
     * 修改客户端管理
     */
    Boolean updateByBo(SysClientBo bo);

    /**
     * 修改状态
     */
    int updateUserStatus(Long id, String status);

    /**
     * 校验并批量删除客户端管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
