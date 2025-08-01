### 为什么controller中要给路径参数要确定一个值
Feign用动态代理机制生成了这个接口的实现类

在编译时期，编译器默认会丢弃方法参数的名称，只保留它们的类型信息。

在运行时，Feign需要参数名称来匹配URL模板，但运行时参数名称丢失，所以依赖于注解中指定的参数名称。

#### 为什么Java 编译器在编译时，默认丢弃方法参数名
1. 早期设计理念

   在 Java 设计初期，参数名只是给程序员看的，对程序运行没有任何意义：

   * JVM 只关心方法签名是否唯一（方法名 + 参数类型）
   * 方法调用时只需要知道类型顺序，参数名对虚拟机无意义
   * 字节码中参数默认被编号（如 arg0, arg1）

   所以从编译器设计角度：为了生成更小的 class 文件，提高执行效率，参数名可以丢弃。
   后续的java版本从向后兼容性考虑，也选择默认丢弃方法参数名
2. 安全和封装性考量

    从安全角度看，保留方法参数名有泄漏内部设计信息的风险，比如：

    `public void login(String username, String password) {}`

    如果通过反射能拿到 "password" 这个参数名，就可能被黑盒扫描、动态分析攻击、API签名破解等