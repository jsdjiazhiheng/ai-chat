## 使用说明

### 1. 启动服务
启动服务需要确保后端服务已经启动，并且数据库已经配置好。

### 2. 配置AI模型key
在项目配置文件中配置不同模型的key

`ai-chat-admin/src/main/resources/application-dev.yml` 和 `application-prod.yml` 中配置AI模型key

#### 示例
```yaml
ai:
  # Kimi模型 只有文本模型
  # 获取地址：https://platform.moonshot.cn/
  kimi:
    token: 
  # 百度模型 画图和文本模型都有
  # 获取地址：https://console.bce.baidu.com/  
  baidu:
    client-id: 
    client-secret: 
  # 智谱清言模型 文本和画图模型
  # https://maas.aminer.cn/  
  zhipu:
    token: 
  # 触站AI 只有画图模型      
  czhan:
    token: 
  # deepseek 只有文本模型
  # https://platform.deepseek.com/  
  deepseek:
    token: 
  # 星火模型 文本和画图模型
  # https://www.xfyun.cn/  
  spark:
    appid: 
    api-key: 
    api-secret: 
  # 阿里云 文本和画图模型
  # https://www.aliyun.com/  
  aliyun:
    token: 
  # 画宇宙 只有画图模型 目前不推荐使用，画图效果一般
  # https://open.creator.nolibox.com/  
  nolipix:
    token: 
  # 火山引擎 文本和画图模型
  # https://www.volcengine.com/
  volcengine:
    token: 
    image-access-key-id: 
    image-secret-access-key: 
  # 执行任务 模型      
  completion:
    model: BAIDU
```

### 注意事项

`ICompletionService`中使用的model是根据配置文件中的配置模型`ai.completion.model`


火山引擎需要控制台创建推理点，将接入点填写至火山引擎枚举类中 `ai-chat-modules/ai-chat-chat/src/main/java/cn/com/chat/chat/chain/enums/model/VolcengineModelEnums.java`

```java
@Getter
@AllArgsConstructor
public enum VolcengineModelEnums {

    /**
     * 豆包模型
     */
    DOUBAO_LITE_4K("Doubao-lite-4k", ""),
    DOUBAO_LITE_32K("Doubao-lite-32k", ""),
    DOUBAO_LITE_128K("Doubao-lite-128k", ""),

    DOUBAO_PRO_4K("Doubao-pro-4k", ""),
    DOUBAO_PRO_32K("Doubao-pro-32k", ""),
    DOUBAO_PRO_128K("Doubao-pro-128k", ""),
    ;

    private final String model;

    /**
     * 推理接入点
     */
    private final String point;

    public static String getPoint(String model) {
        for (VolcengineModelEnums value : VolcengineModelEnums.values()) {
            if (value.getModel().equals(model)) {
                return value.getPoint();
            }
        }
        return null;
    }

}
```