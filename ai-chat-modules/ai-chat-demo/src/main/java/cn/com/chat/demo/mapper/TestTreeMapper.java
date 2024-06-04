package cn.com.chat.demo.mapper;

import cn.com.chat.common.mybatis.annotation.DataColumn;
import cn.com.chat.common.mybatis.annotation.DataPermission;
import cn.com.chat.common.mybatis.core.mapper.BaseMapperPlus;
import cn.com.chat.demo.domain.TestTree;
import cn.com.chat.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTree, TestTreeVo> {

}
