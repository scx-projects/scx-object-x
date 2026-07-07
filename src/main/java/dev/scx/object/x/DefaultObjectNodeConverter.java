package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectNodeConverter;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.context.NodeToObjectContextImpl;
import dev.scx.object.x.context.ObjectToNodeContextImpl;
import dev.scx.object.x.mapper.TypeNodeMapperSelector;
import dev.scx.reflect.TypeInfo;

/// DefaultObjectNodeConverter
///
/// 一个 `Object <-> Node` 的转换器.
///
/// 更准确地说, DefaultObjectNodeConverter 绑定了一个 TypeNodeMapperSelector,
/// 然后在每一次转换时接收 DefaultObjectNodeConvertOptions.
///
/// 也就是说:
///
/// - TypeNodeMapperSelector 是 DefaultObjectNodeConverter 的对象状态;
/// - DefaultObjectNodeConvertOptions 是每次转换的方法参数.
///
/// 这是本类最基本的设计约束.
///
/// 简单地说:
///
/// ```java
/// var converter = new DefaultObjectNodeConverter(selector);
///
/// converter.objectToNode(value, options);
/// converter.nodeToObject(node, type, options);
/// ```
///
/// 而不是:
///
/// ```java
/// var converter = new DefaultObjectNodeConverter();
///
/// converter.objectToNode(value, selector, options);
/// converter.nodeToObject(node, type, selector, options);
/// ```
///
/// 也不是:
///
/// ```java
/// var converter = new DefaultObjectNodeConverter(selector, options);
///
/// converter.objectToNode(value);
/// converter.nodeToObject(node, type);
/// ```
///
/// 这个设计不是唯一正确的设计, 也不是从某个绝对理论中推导出来的结论.
/// 它只是一种工程化取舍.
///
/// 本库并不试图证明:
///
/// - selector 在"本质上"一定应该是对象状态;
/// - options 在"本质上"一定应该是方法参数;
/// - 日期格式在"本质上"一定应该是运行参数;
/// - mapper 构造函数在"本质上"一定不能接收这些配置.
///
/// 这些说法都太绝对, 也不符合真实的 API 设计过程.
///
/// 实际上, "参数化"和"实例化"并不是两种完全不同的宇宙.
/// 一个构造函数参数, 本质上只是一个被提前绑定的方法参数.
/// 一个方法参数, 如果被长期保存、复用、组合和命名, 最终也会变成某种实例状态.
///
/// 换句话说:
///
/// - 把 selector 作为方法参数传入, 并不是错误的;
/// - 把 options 绑定到 converter 实例上, 也不是错误的;
/// - 把日期 formatter 放进 TemporalAccessorNodeMapper 的构造函数, 也不是错误的;
/// - 把日期 formatter 放进 TemporalAccessorNodeMapperOptions, 同样也不是某种唯一真理.
///
/// 这些设计都可以成立.
///
/// 真正需要决定的不是:
///
/// - 参数化是否正确?
/// - 实例化是否正确?
/// - 某个配置在本体论意义上到底属于哪里?
///
/// 而是:
///
/// - 哪些东西应该出现在日常调用路径上?
/// - 哪些东西应该沉到构建期?
/// - 哪些东西用户会经常临时调整?
/// - 哪些东西一旦变化, 就代表这套 converter 的能力边界发生了变化?
/// - 哪些东西如果也变成参数, 会让 API 绕一圈后又回到另一个 converter?
/// - 哪些东西如果也绑定到实例, 会让 converter 变成一个隐藏大量调用行为的重对象?
///
/// 所以, 本类以及本包采用的不是"绝对分类规则",
/// 而是一套为了使用体验和长期维护而制定的工程约束.
///
/// 这套约束可以概括为:
///
/// - selector / converter 管"规则归属"和"类型分派能力";
/// - TypeNodeMapper 构造函数管"mapper 身份"和"结构性信息";
/// - options 管"本次转换的运行策略";
/// - context 管"一次转换过程中的执行现场".
///
/// 这几个边界不是为了证明别的设计错误,
/// 而是为了让本库在长期演化中不要滑向两个极端:
///
/// - 所有东西都是参数, 最后参数对象膨胀成另一个 converter;
/// - 所有东西都绑定到实例, 最后 converter 变成一个隐藏大量行为的 ObjectMapper 式对象.
///
///
/// ## 1. 为什么 selector 属于 DefaultObjectNodeConverter?
///
/// TypeNodeMapperSelector 的职责是把 TypeInfo 解析为 TypeNodeMapper.
///
/// 它回答的是:
///
/// - 给定这个 Java 类型, 应该由哪个 mapper 处理?
///
/// 例如:
///
/// - String -> StringNodeMapper
/// - int / Integer -> IntNodeMapper
/// - Date -> DateNodeMapper
/// - 某个 record 类型 -> RecordNodeMapperFactory 产生的 mapper
/// - 某个 bean 类型 -> BeanNodeMapperFactory 产生的 mapper
/// - 某个 array 类型 -> ArrayNodeMapperFactory 产生的 mapper
///
/// 这是一张"类型到 mapper"的选择表.
///
/// 它包含:
///
/// - 已注册的 mapper;
/// - 已注册的 mapper factory;
/// - mapper 查找顺序;
/// - mapper 创建逻辑;
/// - mapper 查找缓存;
/// - 本 converter 能处理哪些类型;
/// - 这些类型分别由谁处理.
///
/// 这些东西共同构成了这套 converter 的"能力边界".
///
/// 也就是说, 一个 DefaultObjectNodeConverter 之所以是"这一套 converter",
/// 很大程度上就是因为它绑定了"这一套 TypeNodeMapperSelector".
///
/// 如果 mapper 集合、mapper factory、mapper 选择顺序或者类型分派关系发生变化,
/// 那就不再只是"一次转换怎么运行"的问题,
/// 而是"这套 converter 能处理什么, 以及由谁处理"的问题.
///
/// 因此, 本库规定:
///
/// - TypeNodeMapperSelector 属于 DefaultObjectNodeConverter;
/// - selector 不作为 objectToNode / nodeToObject 的方法参数;
/// - 如果要改变 mapper 集合、mapper factory 或类型选择关系,
///   应该通过 DefaultObjectNodeConverterBuilder 创建新的 DefaultObjectNodeConverter.
///
/// 这条规则的目的不是证明"selector 本质上不能是参数".
/// 从最抽象的角度看, 一次转换当然可以写成:
///
/// ```java
/// objectToNode(value, selector, options)
/// nodeToObject(node, type, selector, options)
/// ```
///
/// 这种设计并非错误.
///
/// 但是在本库中, 如果 selector 也成为每次调用的普通参数,
/// API 表面上会更"纯函数化", 但使用者会在每一次转换时被迫面对一个问题:
///
/// - 这次调用到底应该使用哪张 mapper 选择表?
///
/// 更重要的是, selector 一旦和 options 一起成为方法参数,
/// 它们就很容易自然合流:
///
/// - 既然 options 是参数, selector 也是参数,
///   那么为什么 options 不能持有 selector?
///
/// - 既然 options 可以影响本次转换,
///   那么为什么 options 不能注册 mapper?
///
/// - 既然 options 可以注册 mapper,
///   那么为什么 options 不能注册 mapperFactory?
///
/// - 既然 options 同时持有 mapper、mapperFactory、selector 和各种运行策略,
///   那么 options 和 converter 还有什么区别?
///
/// 这样继续演化下去, DefaultObjectNodeConvertOptions 会逐渐膨胀成另一个转换器配置对象,
/// 最终 DefaultObjectNodeConverter 反而变成空壳.
///
/// 这就是所谓"全参数化会绕一圈回到实例化".
///
/// 因此, 本库选择把 selector 固定在 DefaultObjectNodeConverter 中.
/// 这样"类型由谁处理"这件事拥有一个清晰的所有者.
///
///
/// ## 2. 为什么 options 属于每次调用?
///
/// DefaultObjectNodeConvertOptions 表示"一次转换的运行策略".
///
/// 它不定义这套 converter 能处理哪些类型.
/// 它也不定义某个 TypeInfo 应该由哪个 TypeNodeMapper 处理.
///
/// options 的职责是在 mapper 已经被 selector 选中之后,
/// 告诉这个 mapper:
///
/// - 本次应该如何运行;
/// - 本次应该采用哪些兼容策略;
/// - 本次应该使用哪些 mapper 专属选项;
/// - 本次转换过程有哪些限制.
///
/// 例如:
///
/// - 是否使用注解;
/// - 是否忽略 null 值;
/// - 数字转换策略;
/// - primitive 遇到 null 时的策略;
/// - single value array 兼容策略;
/// - 最大嵌套深度;
/// - 日期 / 时间格式化策略;
/// - timestamp / string 表示模式;
/// - 某些 mapper 支持的本次运行模式;
/// - NodeTypeAdapter 这样的本次 Node 形状适配策略.
///
/// 这些选项当然会影响转换结果.
///
/// 例如:
///
/// - 同一个 Date 可以输出成字符串, 也可以输出成时间戳;
/// - 同一个 LocalDateTime 可以使用不同格式输出;
/// - 同一个对象可以忽略 null 字段, 也可以保留 null 字段;
/// - 同一个 Node 可以允许 single value array 兼容, 也可以严格匹配.
///
/// 所以不能说 options 只是"不重要的小开关".
///
/// options 可以显著改变本次转换结果.
///
/// 但是, options 不应该改变这条关系:
///
/// ```text
/// TypeInfo -> TypeNodeMapper
/// ```
///
/// 换句话说, options 可以改变"已选中的 mapper 本次如何运行",
/// 但不应该改变"某个 Java 类型由哪个 mapper 处理".
///
/// 因此, 本库规定:
///
/// - DefaultObjectNodeConvertOptions 不持有 selector;
/// - DefaultObjectNodeConvertOptions 不注册 mapper;
/// - DefaultObjectNodeConvertOptions 不注册 mapperFactory;
/// - DefaultObjectNodeConvertOptions 不改变 TypeInfo -> TypeNodeMapper 的选择关系.
///
/// 判断一个新功能应该放在哪里时, 可以先问一个非常具体的问题:
///
/// - 这个功能会不会改变 selector.findMapper(type) 的结果?
///
/// 如果会, 它属于 selector / mapper / mapperFactory / converter 的构建过程.
///
/// 如果不会, 它只是已选 mapper 的本次运行策略,
/// 才考虑放入 DefaultObjectNodeConvertOptions 或某个 TypeNodeMapperOptions.
///
/// 这个问题不是完美判定器.
/// 它不是数学定理.
/// 但它是一个非常有用的工程判断习惯.
///
///
/// ## 3. 为什么不能把 options 也绑定到 converter 上?
///
/// 反过来, 也可以走向另一个极端:
///
/// ```java
/// var converter = new DefaultObjectNodeConverter(selector, options);
///
/// converter.objectToNode(value);
/// converter.nodeToObject(node, type);
/// ```
///
/// 这种设计也并非错误.
///
/// selector 和 options 都被绑定到实例中,
/// 实例本身携带了完成转换所需的一切规则.
///
/// 这种设计的优点是调用点非常简洁.
///
/// 但是它也有问题.
///
/// 例如, 临时修改日期格式是一个非常常见的需求:
///
/// - 这次输出给 API, 使用 ISO 字符串;
/// - 另一次输出给旧系统, 使用自定义格式;
/// - 某一次为了兼容历史数据, 使用 timestamp;
/// - 某一次解析外部输入, 使用对方传来的日期格式.
///
/// 用户不应该为了"这一次换一个日期格式"就重新创建一套 converter.
/// 用户也不应该为了"这一次改变 null 策略"就重新注册 mapper.
/// 用户更不应该因为几个临时运行参数不同,
/// 就维护一批只在 options 上不同的 converter 实例.
///
/// 如果把所有运行策略都绑定到 converter 上,
/// 那么 converter 会越来越重.
/// 使用者看到:
///
/// ```java
/// converter.objectToNode(value)
/// ```
///
/// 但未必知道这个 converter 内部绑定了:
///
/// - 哪个日期格式;
/// - 哪个 null 策略;
/// - 哪个 primitive null 策略;
/// - 哪个 NodeTypeAdapter;
/// - 哪个最大嵌套深度;
/// - 哪些 mapper 专属运行选项.
///
/// 这样会让一次调用的行为隐藏在实例内部.
///
/// 这就是所谓"全实例化会让 converter 变成隐藏大量行为的重对象".
///
/// 因此, 本库不把 DefaultObjectNodeConvertOptions 固定在 DefaultObjectNodeConverter 中.
/// options 保持为每次调用的显式参数.
///
/// 这样:
///
/// - 类型处理能力的变化, 通过创建新的 converter 表达;
/// - 单次转换策略的变化, 通过传入不同 options 表达.
///
///
/// ## 4. TypeNodeMapper 的构造函数应该放什么?
///
/// 上面的 selector / options 边界不仅适用于 DefaultObjectNodeConverter,
/// 也影响本库中 TypeNodeMapper 的设计.
///
/// 本库对 TypeNodeMapper 采用一种 "轻构造、运行策略参数化" 的倾向:
///
/// - TypeNodeMapper 构造函数只绑定 mapper 身份和结构性信息;
/// - TypeNodeMapperOptions 绑定本次调用的运行策略.
///
/// 换句话说:
///
/// - constructor 决定这个 mapper 是谁;
/// - options 决定这个 mapper 本次怎么跑.
///
/// TypeNodeMapper 的构造函数适合接收:
///
/// - 目标 TypeInfo;
/// - 目标 Class;
/// - array component type;
/// - collection element type;
/// - map key type / value type;
/// - record components;
/// - bean fields;
/// - constructor parameters;
/// - mapper 创建后必须稳定存在的结构性依赖;
/// - 用于表达 mapper 身份的必要信息.
///
/// 这些信息不是"本次调用想不想这样运行"的策略,
/// 而是这个 mapper 之所以是这个 mapper 的基础.
///
/// 例如:
///
/// - ArrayNodeMapper 绑定某个数组类型的 component type 是合理的;
/// - CollectionNodeMapper 绑定某个集合类型的 element type 是合理的;
/// - RecordNodeMapper 绑定某个 record 类型的 components 是合理的;
/// - BeanNodeMapper 绑定某个 bean 类型的可读写字段是合理的.
///
/// 这些都属于 mapper 的身份和结构.
///
/// 但是, TypeNodeMapper 的构造函数不应该轻易绑定普通运行策略.
///
/// 普通运行策略更适合进入 TypeNodeMapperOptions,
/// 再通过 DefaultObjectNodeConvertOptions 在每次调用时传入.
///
/// 例如:
///
/// - 日期格式;
/// - timestamp / string 表示模式;
/// - 是否忽略 null;
/// - 是否使用注解;
/// - 字段命名策略;
/// - 某个 mapper 的兼容模式;
/// - 某个 mapper 的宽松 / 严格解析模式.
///
/// 这些配置可能明显影响转换结果,
/// 但通常不应该改变 mapper 的身份.
///
///
/// ## 5. 为什么 TemporalAccessorNodeMapper 的 formatter 是 options, 而不是构造函数参数?
///
/// TemporalAccessorNodeMapper 是这套设计哲学的典型例子.
///
/// 它当然可以被设计成:
///
/// ```java
/// new TemporalAccessorNodeMapper(formatter)
/// ```
///
/// 或者:
///
/// ```java
/// new TemporalAccessorNodeMapper(formatter, useTimestamp)
/// ```
///
/// 这种设计并非错误.
///
/// 但是本库没有这样设计.
///
/// 原因是, 在本库的抽象中, TemporalAccessorNodeMapper 的身份是:
///
/// - 负责处理 TemporalAccessor 相关类型.
///
/// 而不是:
///
/// - 负责用 yyyy-MM-dd 格式处理 TemporalAccessor;
/// - 负责用 yyyy-MM-dd HH:mm:ss 格式处理 TemporalAccessor;
/// - 负责把某些 temporal 类型输出成 timestamp;
/// - 负责把某些 temporal 类型输出成 ISO 字符串.
///
/// formatter、timestamp 模式、字符串格式等配置,
/// 改变的是已选中的 TemporalAccessorNodeMapper 本次如何把值表示成 Node.
///
/// 它们不改变:
///
/// ```text
/// LocalDateTime -> TemporalAccessorNodeMapper
/// LocalDate -> TemporalAccessorNodeMapper
/// Instant -> TemporalAccessorNodeMapper
/// ```
///
/// 这类类型分派关系.
///
/// 因此, 在本库中, 日期 / 时间格式化器属于 TemporalAccessorNodeMapperOptions,
/// 而不是 TemporalAccessorNodeMapper 的构造函数参数.
///
/// 这样做的好处是:
///
/// - 用户可以在某一次转换中临时改变日期格式;
/// - 用户不需要为了换一个 formatter 重新 build converter;
/// - 用户不需要注册多个只在日期格式上不同的 TemporalAccessorNodeMapper;
/// - mapper 的身份保持稳定;
/// - converter 的能力边界不会因为普通运行策略而碎片化.
///
/// 这里需要强调:
///
/// 这并不是说"日期格式在所有设计中都必然属于 options".
///
/// 日期格式当然也可以被设计成 mapper 构造函数参数.
/// 只是那样一来, mapper 的身份就会从:
///
/// ```text
/// 处理 TemporalAccessor 类型
/// ```
///
/// 变成:
///
/// ```text
/// 以某种格式处理 TemporalAccessor 类型
/// ```
///
/// 这会让 mapper 越来越像"提前绑定了一部分 options 的小 converter".
///
/// 本库不采用这种方向.
///
/// 本库更倾向于:
///
/// - mapper 表示"谁处理这个类型";
/// - options 表示"本次怎么处理".
///
///
/// ## 6. "能参数化的尽量参数化"是什么意思?
///
/// 本库确实倾向于:
///
/// - 能作为本次调用运行策略表达的东西, 尽量参数化;
/// - 能让用户临时调整的东西, 尽量不要强迫用户重新创建 converter;
/// - 能放在 TypeNodeMapperOptions 中表达的普通运行策略, 尽量不要塞进 mapper 构造函数.
///
/// 但是, 这不是无条件的"所有东西都参数化".
///
/// 更准确地说:
///
/// 当一个配置项既可以被设计成构造函数参数,
/// 也可以被设计成 options 字段,
/// 并且两种设计都说得通时,
/// 本库默认优先考虑 options.
///
/// 原因是:
///
/// - options 更适合表达临时调用策略;
/// - 用户可以在不重建 converter 的情况下调整行为;
/// - mapper 构造函数可以保持稳定、轻量;
/// - converter 的身份不会因为普通运行策略而碎片化;
/// - 调用者可以在调用点显式看见本次转换策略.
///
/// 但这个默认倾向有边界.
///
/// 如果某个配置会改变以下内容, 它就不应该进入 options:
///
/// - TypeInfo 到 TypeNodeMapper 的选择关系;
/// - 某个 Java 类型由哪个 mapper 处理;
/// - mapper 的身份;
/// - mapper 之间的递归依赖关系;
/// - mapperFactory 的选择结果;
/// - mapper 缓存作用域;
/// - converter 能处理哪些类型;
/// - converter 的能力边界;
/// - 类型分派规则的所有权.
///
/// 这些东西属于 mapper / mapperFactory / selector / converter 的构建过程,
/// 而不是一次转换的普通运行参数.
///
/// 所以, 本库的倾向不是:
///
/// - 所有东西都参数化.
///
/// 而是:
///
/// - 默认参数化运行策略;
/// - 禁止把规则归属参数化成一次调用的 options.
///
///
/// ## 7. 如何判断一个新功能应该放在哪里?
///
/// 当新增一个配置项或扩展点时, 可以按下面的问题判断.
///
/// ### 7.1 它会不会改变 selector.findMapper(type) 的结果?
///
/// 如果会, 它不属于 options.
///
/// 例如:
///
/// - 新增 MoneyNodeMapper;
/// - 让 LocalDateTime 改由另一个 mapper 处理;
/// - 为 ScxPath 注册专用 mapper;
/// - 改变某个 mapperFactory 的优先级;
/// - 改变某类 TypeInfo 的 mapper 创建规则.
///
/// 这些都会改变:
///
/// ```text
/// TypeInfo -> TypeNodeMapper
/// ```
///
/// 因此应该通过 mapper / mapperFactory / selector / builder / converter 表达.
///
/// ### 7.2 它是不是只影响已选 mapper 本次如何运行?
///
/// 如果是, 它可以考虑放入 options.
///
/// 例如:
///
/// - 日期格式;
/// - timestamp / string 表示模式;
/// - 是否忽略 null;
/// - 是否使用注解;
/// - primitive 遇到 null 时的策略;
/// - single value array 兼容;
/// - 最大嵌套深度;
/// - 数字转换策略;
/// - 字段命名策略;
/// - 某个 mapper 的宽松解析模式.
///
/// 这些配置可能改变转换结果,
/// 但不改变哪个 mapper 被选中.
///
/// ### 7.3 用户会不会合理地希望"这一次临时改一下"?
///
/// 如果会, 优先考虑 options.
///
/// 例如:
///
/// - 这次日期输出 yyyy-MM-dd;
/// - 下次日期输出 yyyy-MM-dd HH:mm:ss;
/// - 某次为了兼容旧系统输出 timestamp;
/// - 某次解析外部输入时启用 single value array 兼容;
/// - 某次导出时忽略 null, 某次内部保存时保留 null.
///
/// 用户不应该为了这些临时策略重新 build converter.
///
/// ### 7.4 它是不是 mapper 创建后必须稳定存在的结构信息?
///
/// 如果是, 放在 mapper 构造函数或 mapperFactory 中.
///
/// 例如:
///
/// - 目标类型;
/// - record components;
/// - bean fields;
/// - array component type;
/// - collection element type;
/// - map key type / value type;
/// - constructor parameter metadata.
///
/// 这些信息定义了 mapper 的结构和身份,
/// 不适合每次调用临时改变.
///
/// ### 7.5 它会不会改变 mapper 缓存或递归转换的规则作用域?
///
/// 如果会, 不要放进 options.
///
/// 例如:
///
/// - 本次转换临时注册 mapper;
/// - 本次转换临时替换 selector;
/// - 本次转换临时修改 mapperFactory;
/// - 本次转换临时改变 mapper 查找顺序.
///
/// 这些行为会让 context 从"转换过程"变成"临时 converter builder".
///
/// 一旦允许这种行为, 以下问题都会变得模糊:
///
/// - mapper 缓存是否还有效?
/// - 递归转换子值时是否继承临时 mapper?
/// - 临时 mapper 的作用域到哪里结束?
/// - 同一个 mapper 在不同 selector 下缓存的子 mapper 是否安全?
/// - 一次转换中的规则和 converter 的规则到底谁优先?
///
/// 因此, 这些能力不属于 options.
///
///
/// ## 8. NodeTypeAdapter 为什么可以属于 options?
///
/// NodeTypeAdapter 也是一个容易被误解的例子.
///
/// 它看起来像是在处理"类型",
/// 但它不负责选择 TypeNodeMapper.
///
/// 它不是:
///
/// ```java
/// TypeInfo -> TypeNodeMapper
/// ```
///
/// 的参与者.
///
/// 它的职责是在 nodeToObject 阶段,
/// 当 mapper 已经由 selector 选中之后,
/// 如果实际 Node 类型和 mapper 期望的 Node 类型不一致,
/// 提供一次本次调用级别的 Node 形状适配.
///
/// 例如:
///
/// - single value array 兼容;
/// - 某些宽松的 Node 类型转换;
/// - 某些输入形态兼容策略.
///
/// 这些行为改变的是"已选 mapper 本次如何接受输入 Node",
/// 而不是"某个 Java 类型由哪个 mapper 处理".
///
/// 因此 NodeTypeAdapter 可以属于 DefaultObjectNodeConvertOptions.
///
/// 但它不应该注册 mapper.
/// 它不应该替换 selector.
/// 它不应该决定 TypeInfo 由哪个 TypeNodeMapper 处理.
///
///
/// ## 9. Context 为什么同时持有 selector 和 options?
///
/// ObjectToNodeContextImpl / NodeToObjectContextImpl 会同时接收 selector 和 options.
///
/// 这并不表示 selector 和 options 在职责上是同一类东西.
///
/// Context 表示一次转换过程.
/// 它是执行现场, 不是规则所有者.
///
/// 一次转换过程中, context 需要知道:
///
/// - 当前使用哪套 mapper 选择表;
/// - 当前调用使用哪些运行参数;
/// - 当前转换路径;
/// - 当前嵌套深度;
/// - 如何递归转换子值;
/// - 如何调用已选 mapper;
/// - 如何在子对象、字段、数组元素、集合元素之间继续转换.
///
/// 因此, context 同时接收 selector 和 options 是合理的.
///
/// 但是:
///
/// - context 不拥有 selector;
/// - context 不拥有 options;
/// - context 不注册 mapper;
/// - context 不修改 selector;
/// - context 不替换 options;
/// - context 不把临时 mapper 塞进本次转换.
///
/// 如果 context 可以注册 mapper 或修改 selector,
/// 它就会从"一次转换过程"变成"临时 converter builder".
///
/// 那样会让:
///
/// - mapper 缓存;
/// - 递归转换;
/// - 规则作用域;
/// - 子 mapper 查找;
/// - converter 的稳定身份;
///
/// 都变得模糊.
///
/// 所以 context 只能使用规则, 不能拥有规则.
/// context 只能执行转换, 不能构建 converter.
///
///
/// ## 10. 这个设计是混合式的, 而不是教条式的
///
/// 本类没有选择"所有东西都是参数".
///
/// 因为全参数化虽然显式,
/// 但会让调用点不断膨胀.
///
/// ```java
/// objectToNode(value, selector, options)
/// nodeToObject(node, type, selector, options)
/// ```
///
/// 继续发展下去, 用户会自然地把 selector、mapper、mapperFactory、options
/// 包装成一个更大的参数对象.
///
/// 而这个参数对象最终又会变成另一个 converter.
///
/// 本类也没有选择"所有东西都绑定到实例".
///
/// 因为全实例化虽然调用简单,
/// 但会把大量本次调用行为隐藏到 converter 内部.
///
/// ```java
/// converter.objectToNode(value)
/// ```
///
/// 使用者看到这次调用时, 未必知道 converter 内部绑定了哪些运行策略.
///
/// 所以当前设计刻意取中间值:
///
/// - selector 绑定到 converter,
///   因为它代表相对稳定的类型到 mapper 的选择关系;
///
/// - options 保留为调用参数,
///   因为它代表本次转换的运行策略;
///
/// - mapper 构造函数绑定身份和结构,
///   因为这些信息定义 mapper 是谁;
///
/// - TypeNodeMapperOptions 承载 mapper 的本次运行参数,
///   因为这些信息定义 mapper 这次怎么运行;
///
/// - context 同时拿到 selector 和 options,
///   因为它需要执行一次完整转换,
///   但它不拥有也不修改这些规则.
///
/// 这个边界的目标不是证明其他设计错误,
/// 而是防止本库在后续演化中滑向两个极端:
///
/// - 把 selector 参数化, 让 options 逐渐变成另一个 converter;
/// - 把 options 实例化, 让 converter 逐渐变成一个隐藏大量调用行为的重对象.
///
///
/// ## 11. 本库最终采用的基本规则
///
/// 后续维护本库时, 请遵守以下规则.
///
/// ### 规则一: DefaultObjectNodeConverter 持有 TypeNodeMapperSelector
///
/// TypeNodeMapperSelector 定义:
///
/// - 哪些类型能被处理;
/// - 每个类型由哪个 mapper 处理;
/// - mapper 和 mapperFactory 的查找关系;
/// - mapper 查找缓存;
/// - converter 的能力边界.
///
/// selector 不作为每次转换的方法参数.
///
/// 如果 selector 需要变化,
/// 应该通过 DefaultObjectNodeConverterBuilder 创建新的 DefaultObjectNodeConverter.
///
/// ### 规则二: DefaultObjectNodeConvertOptions 是每次转换的运行参数
///
/// DefaultObjectNodeConvertOptions 可以包含:
///
/// - 通用转换选项;
/// - mapper 专属 options;
/// - NodeTypeAdapter;
/// - 最大嵌套深度;
/// - null 策略;
/// - 数字策略;
/// - 日期 / 时间格式策略;
/// - 本次转换兼容策略.
///
/// 但它不应该包含:
///
/// - selector;
/// - mapper 注册表;
/// - mapperFactory 注册表;
/// - converter;
/// - 临时 mapper 注册逻辑;
/// - 临时 mapperFactory 注册逻辑.
///
/// ### 规则三: TypeNodeMapper 构造函数绑定身份和结构
///
/// TypeNodeMapper 构造函数适合接收:
///
/// - 目标类型;
/// - 类型结构;
/// - 字段结构;
/// - 参数结构;
/// - component type;
/// - element type;
/// - key / value type;
/// - mapper 创建后必须稳定存在的信息.
///
/// 它不应该轻易接收普通运行策略.
///
/// ### 规则四: TypeNodeMapperOptions 绑定本次运行策略
///
/// 如果某个配置只影响已选 mapper 本次如何运行,
/// 默认优先放入 TypeNodeMapperOptions.
///
/// 例如:
///
/// - 日期格式;
/// - timestamp 模式;
/// - 是否使用注解;
/// - 是否忽略 null;
/// - 宽松 / 严格模式;
/// - 本次解析兼容策略.
///
/// ### 规则五: 当边界不好判断时, 默认偏向 options, 但不能越界
///
/// 有些配置既可以放构造函数,
/// 也可以放 options.
///
/// 这时不要试图寻找一个"绝对正确"的答案.
///
/// 本库的默认倾向是:
///
/// - 如果它是用户可能临时改变的调用策略, 优先放 options;
/// - 如果它不会改变 TypeInfo -> TypeNodeMapper 的选择关系, 优先放 options;
/// - 如果它不会改变 mapper 身份、mapper graph、缓存作用域或 converter 能力边界, 优先放 options.
///
/// 但是, 如果它会改变:
///
/// - 类型由谁处理;
/// - selector.findMapper(type) 的结果;
/// - mapper 注册关系;
/// - mapperFactory 注册关系;
/// - converter 支持的类型集合;
/// - mapper 缓存的作用域;
/// - 递归转换时的规则来源;
///
/// 那么它不应该进入 options.
///
///
/// ## 12. 总结
///
/// 这套设计可以总结为一句话:
///
/// ```text
/// selector / converter 管规则归属;
/// mapper constructor 管身份和结构;
/// options 管本次运行策略;
/// context 管本次执行现场.
/// ```
///
/// 或者更短:
///
/// ```text
/// converter 决定能处理什么以及由谁处理;
/// options 决定这一次怎么处理.
/// ```
///
/// 这不是唯一正确的设计.
/// 这不是不可反驳的理论.
/// 这不是试图给"参数"和"实例状态"划出一条绝对边界.
///
/// 它只是一套工程约定:
///
/// - 让高频、临时、调用级配置保持轻量;
/// - 让低频、结构性、能力级配置保持稳定;
/// - 让用户不必为了临时修改日期格式之类的需求重新创建 converter;
/// - 让用户也不必在每次转换时面对 selector 这种结构性概念;
/// - 让 options 不膨胀成另一个 converter;
/// - 让 converter 不退化成隐藏大量调用行为的重对象;
/// - 让 mapper 的身份和运行策略保持相对清晰.
///
/// 后续扩展本库时, 不需要把这些规则理解成绝对真理.
/// 但应该把它们理解成当前库的设计边界.
///
/// 如果要打破这些边界, 应该先确认:
///
/// - 是否真的提升了使用体验;
/// - 是否会让 options 变成 converter;
/// - 是否会让 converter 隐藏过多调用行为;
/// - 是否会让 mapper 选择关系变得模糊;
/// - 是否会让 context 变成临时 builder;
/// - 是否会让普通用户在日常调用路径上承担不必要的概念负担.
///
/// @author scx567888
public final class DefaultObjectNodeConverter implements ObjectNodeConverter<DefaultObjectNodeConvertOptions> {

    public static final DefaultObjectNodeConverter DEFAULT_OBJECT_NODE_CONVERTER = DefaultObjectNodeConverter.builder().registerDefaultMappers().build();

    private final TypeNodeMapperSelector selector;

    DefaultObjectNodeConverter(TypeNodeMapperSelector selector) {
        this.selector = selector;
    }

    public static DefaultObjectNodeConverterBuilder builder() {
        return new DefaultObjectNodeConverterBuilder();
    }

    @Override
    public Node objectToNode(Object value, DefaultObjectNodeConvertOptions options) throws ObjectToNodeException {
        var objectToNodeContext = new ObjectToNodeContextImpl(selector, options);
        return objectToNodeContext.objectToNode(value, "$"); // 我们用 '$' 表示根节点
    }

    @Override
    public <T> T nodeToObject(Node node, TypeInfo type, DefaultObjectNodeConvertOptions options) throws NodeToObjectException {
        var nodeToObjectContext = new NodeToObjectContextImpl(selector, options);
        return nodeToObjectContext.nodeToObject(node, type);
    }

    @Override
    public <T> T nodeToObject(Node node, Class<T> clazz, DefaultObjectNodeConvertOptions options) throws NodeToObjectException {
        var nodeToObjectContext = new NodeToObjectContextImpl(selector, options);
        return nodeToObjectContext.nodeToObject(node, clazz);
    }

}
