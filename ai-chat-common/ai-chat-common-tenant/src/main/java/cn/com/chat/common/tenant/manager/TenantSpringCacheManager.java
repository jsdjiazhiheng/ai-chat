package cn.com.chat.common.tenant.manager;

import cn.com.chat.common.core.constant.GlobalConstants;
import cn.com.chat.common.core.utils.StringUtils;
import cn.com.chat.common.redis.manager.PlusSpringCacheManager;
import cn.com.chat.common.tenant.helper.TenantHelper;
import org.springframework.cache.Cache;

/**
 * 重写 cacheName 处理方法 支持多租户
 *
 * @author Lion Li
 */
public class TenantSpringCacheManager extends PlusSpringCacheManager {

    public TenantSpringCacheManager() {
    }

    @Override
    public Cache getCache(String name) {
        if (StringUtils.contains(name, GlobalConstants.GLOBAL_REDIS_KEY)) {
            return super.getCache(name);
        }
        String tenantId = TenantHelper.getTenantId();
        if (StringUtils.startsWith(name, tenantId)) {
            // 如果存在则直接返回
            return super.getCache(name);
        }
        return super.getCache(tenantId + ":" + name);
    }

}
