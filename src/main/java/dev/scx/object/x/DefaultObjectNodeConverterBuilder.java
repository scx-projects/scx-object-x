package dev.scx.object.x;

import dev.scx.object.x.mapper.*;
import dev.scx.object.x.mapper.atomic.AtomicBooleanNodeMapper;
import dev.scx.object.x.mapper.atomic.AtomicIntegerNodeMapper;
import dev.scx.object.x.mapper.atomic.AtomicLongNodeMapper;
import dev.scx.object.x.mapper.bean.BeanNodeMapperFactory;
import dev.scx.object.x.mapper.byte_array.ByteArrayNodeMapper;
import dev.scx.object.x.mapper.charset.CharsetNodeMapperFactory;
import dev.scx.object.x.mapper.collection.CollectionNodeMapperFactory;
import dev.scx.object.x.mapper.file.FileNodeMapper;
import dev.scx.object.x.mapper.map.MapNodeMapperFactory;
import dev.scx.object.x.mapper.math.BigDecimalNodeMapper;
import dev.scx.object.x.mapper.math.BigIntegerNodeMapper;
import dev.scx.object.x.mapper.node.*;
import dev.scx.object.x.mapper.other.URINodeMapper;
import dev.scx.object.x.mapper.other.UUIDNodeMapper;
import dev.scx.object.x.mapper.path.PathNodeMapperFactory;
import dev.scx.object.x.mapper.primitive.*;
import dev.scx.object.x.mapper.record.RecordNodeMapperFactory;
import dev.scx.object.x.mapper.time.*;

/// DefaultObjectNodeConverterBuilder
///
/// @author scx567888
public final class DefaultObjectNodeConverterBuilder {

    private final TypeNodeMapperSelector selector;

    DefaultObjectNodeConverterBuilder() {
        this.selector = new TypeNodeMapperSelectorImpl();
    }

    public DefaultObjectNodeConverterBuilder registerMapper(TypeNodeMapper<?, ?> mapper) {
        selector.registerMapper(mapper);
        return this;
    }

    public DefaultObjectNodeConverterBuilder registerMapperFactory(TypeNodeMapperFactory mapperFactory) {
        selector.registerMapperFactory(mapperFactory);
        return this;
    }

    public DefaultObjectNodeConverterBuilder registerMapperFactory(TypeNodeMapperFactory mapperFactory, int order) {
        selector.registerMapperFactory(mapperFactory, order);
        return this;
    }

    public DefaultObjectNodeConverterBuilder registerDefaultMappers() {
        // 基本类型
        this.registerMapper(new ByteNodeMapper(true));
        this.registerMapper(new ShortNodeMapper(true));
        this.registerMapper(new IntNodeMapper(true));
        this.registerMapper(new LongNodeMapper(true));
        this.registerMapper(new FloatNodeMapper(true));
        this.registerMapper(new DoubleNodeMapper(true));
        this.registerMapper(new BooleanNodeMapper(true));
        this.registerMapper(new CharNodeMapper(true));


        // 基本类型包装类型
        this.registerMapper(new ByteNodeMapper(false));
        this.registerMapper(new ShortNodeMapper(false));
        this.registerMapper(new IntNodeMapper(false));
        this.registerMapper(new LongNodeMapper(false));
        this.registerMapper(new FloatNodeMapper(false));
        this.registerMapper(new DoubleNodeMapper(false));
        this.registerMapper(new BooleanNodeMapper(false));
        this.registerMapper(new CharNodeMapper(false));


        // 字符串
        this.registerMapper(new StringNodeMapper());


        // byte[] 特化
        this.registerMapper(new ByteArrayNodeMapper());


        // 大数字类型
        this.registerMapper(new BigIntegerNodeMapper());
        this.registerMapper(new BigDecimalNodeMapper());


        // 时间
        this.registerMapper(new LocalDateTimeNodeMapper());
        this.registerMapper(new LocalDateNodeMapper());
        this.registerMapper(new LocalTimeNodeMapper());
        this.registerMapper(new OffsetDateTimeNodeMapper());
        this.registerMapper(new OffsetTimeNodeMapper());
        this.registerMapper(new ZonedDateTimeNodeMapper());
        this.registerMapper(new YearNodeMapper());
        this.registerMapper(new MonthNodeMapper());
        this.registerMapper(new MonthDayNodeMapper());
        this.registerMapper(new YearMonthNodeMapper());
        this.registerMapper(new DayOfWeekNodeMapper());
        this.registerMapper(new InstantNodeMapper());
        this.registerMapper(new DurationNodeMapper());
        this.registerMapper(new PeriodNodeMapper());
        this.registerMapper(new DateNodeMapper());


        // Node 类型
        this.registerMapper(new NodeNodeMapper());
        this.registerMapper(new ValueNodeNodeMapper());
        this.registerMapper(new ContainerNodeNodeMapper());
        this.registerMapper(new NullNodeNodeMapper());
        this.registerMapper(new NumberNodeNodeMapper());
        this.registerMapper(new StringNodeNodeMapper());
        this.registerMapper(new BooleanNodeNodeMapper());
        this.registerMapper(new IntNodeNodeMapper());
        this.registerMapper(new LongNodeNodeMapper());
        this.registerMapper(new FloatNodeNodeMapper());
        this.registerMapper(new DoubleNodeNodeMapper());
        this.registerMapper(new BigIntegerNodeNodeMapper());
        this.registerMapper(new BigDecimalNodeNodeMapper());
        this.registerMapper(new ArrayNodeNodeMapper());
        this.registerMapper(new ObjectNodeNodeMapper());


        // Untyped
        this.registerMapper(new UntypedNodeMapper());


        // File
        this.registerMapper(new FileNodeMapper());


        // Atomic 相关
        this.registerMapper(new AtomicIntegerNodeMapper());
        this.registerMapper(new AtomicLongNodeMapper());
        this.registerMapper(new AtomicBooleanNodeMapper());


        // Other
        this.registerMapper(new UUIDNodeMapper());
        this.registerMapper(new URINodeMapper());


        // 以下 Factory 注意注册顺序.

        // 数组
        this.registerMapperFactory(new ArrayNodeMapperFactory());


        // Collection 和 Map
        this.registerMapperFactory(new CollectionNodeMapperFactory());
        this.registerMapperFactory(new MapNodeMapperFactory());


        // Path 和 Charset
        this.registerMapperFactory(new PathNodeMapperFactory());
        this.registerMapperFactory(new CharsetNodeMapperFactory());


        // Bean 和 Record
        this.registerMapperFactory(new BeanNodeMapperFactory());
        this.registerMapperFactory(new RecordNodeMapperFactory());


        // Enum
        this.registerMapperFactory(new EnumNodeMapperFactory());

        return this;
    }

    public DefaultObjectNodeConverter build() {
        return new DefaultObjectNodeConverter(selector);
    }

}
