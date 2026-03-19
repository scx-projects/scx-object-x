package dev.scx.object.x;

import dev.scx.node.Node;
import dev.scx.object.NodeToObjectException;
import dev.scx.object.ObjectNodeConverter;
import dev.scx.object.ObjectToNodeException;
import dev.scx.object.x.mapper.StringNodeMapper;
import dev.scx.object.x.mapper.UntypedNodeMapper;
import dev.scx.object.x.mapper.math.BigDecimalNodeMapper;
import dev.scx.object.x.mapper.math.BigIntegerNodeMapper;
import dev.scx.object.x.mapper.node.*;
import dev.scx.object.x.mapper.other.URINodeMapper;
import dev.scx.object.x.mapper.other.UUIDNodeMapper;
import dev.scx.object.x.mapper.primitive.*;
import dev.scx.object.x.mapper.time.DateNodeMapper;
import dev.scx.object.x.mapper.time.TemporalAccessorNodeMapper;
import dev.scx.object.x.mapper.time.TemporalAmountNodeMapper;
import dev.scx.object.x.mapper_factory.*;
import dev.scx.reflect.TypeInfo;

import java.time.*;

public final class DefaultObjectNodeConverter implements ObjectNodeConverter<DefaultObjectNodeConvertOptions> {

    private final TypeNodeMapperSelector selector;

    public DefaultObjectNodeConverter() {
        this.selector = new TypeNodeMapperSelectorImpl();

        // 基本类型
        this.selector.registerMapper(new ByteNodeMapper(true));
        this.selector.registerMapper(new ShortNodeMapper(true));
        this.selector.registerMapper(new IntNodeMapper(true));
        this.selector.registerMapper(new LongNodeMapper(true));
        this.selector.registerMapper(new FloatNodeMapper(true));
        this.selector.registerMapper(new DoubleNodeMapper(true));
        this.selector.registerMapper(new BooleanNodeMapper(true));
        this.selector.registerMapper(new CharNodeMapper(true));


        // 基本类型包装类型
        this.selector.registerMapper(new ByteNodeMapper(false));
        this.selector.registerMapper(new ShortNodeMapper(false));
        this.selector.registerMapper(new IntNodeMapper(false));
        this.selector.registerMapper(new LongNodeMapper(false));
        this.selector.registerMapper(new FloatNodeMapper(false));
        this.selector.registerMapper(new DoubleNodeMapper(false));
        this.selector.registerMapper(new BooleanNodeMapper(false));
        this.selector.registerMapper(new CharNodeMapper(false));


        // 字符串
        this.selector.registerMapper(new StringNodeMapper());


        //大数字类型
        this.selector.registerMapper(new BigIntegerNodeMapper());
        this.selector.registerMapper(new BigDecimalNodeMapper());


        // 时间
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(LocalDateTime.class, LocalDateTime::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(LocalDate.class, LocalDate::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(LocalTime.class, LocalTime::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(OffsetDateTime.class, OffsetDateTime::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(OffsetTime.class, OffsetTime::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(ZonedDateTime.class, ZonedDateTime::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(Year.class, Year::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(Month.class, Month::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(MonthDay.class, MonthDay::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(YearMonth.class, YearMonth::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(DayOfWeek.class, DayOfWeek::from));
        this.selector.registerMapper(new TemporalAccessorNodeMapper<>(Instant.class, Instant::from));
        this.selector.registerMapper(new TemporalAmountNodeMapper<>(Duration.class, Duration::toString, Duration::parse));
        this.selector.registerMapper(new TemporalAmountNodeMapper<>(Period.class, Period::toString, Period::parse));
        this.selector.registerMapper(new DateNodeMapper());


        // Node 类型
        this.selector.registerMapper(new NodeNodeMapper());
        this.selector.registerMapper(new ValueNodeNodeMapper());
        this.selector.registerMapper(new ContainerNodeNodeMapper());
        this.selector.registerMapper(new NullNodeNodeMapper());
        this.selector.registerMapper(new NumberNodeNodeMapper());
        this.selector.registerMapper(new StringNodeNodeMapper());
        this.selector.registerMapper(new BooleanNodeNodeMapper());
        this.selector.registerMapper(new IntNodeNodeMapper());
        this.selector.registerMapper(new LongNodeNodeMapper());
        this.selector.registerMapper(new FloatNodeNodeMapper());
        this.selector.registerMapper(new DoubleNodeNodeMapper());
        this.selector.registerMapper(new BigIntegerNodeNodeMapper());
        this.selector.registerMapper(new BigDecimalNodeNodeMapper());
        this.selector.registerMapper(new ArrayNodeNodeMapper());
        this.selector.registerMapper(new ObjectNodeNodeMapper());


        // Untyped
        this.selector.registerMapper(new UntypedNodeMapper());


        // Other
        this.selector.registerMapper(new UUIDNodeMapper());
        this.selector.registerMapper(new URINodeMapper());


        // 注意顺序
        this.selector.registerMapperFactory(new ArrayNodeMapperFactory());
        this.selector.registerMapperFactory(new CollectionNodeMapperFactory());
        this.selector.registerMapperFactory(new MapNodeMapperFactory());
        this.selector.registerMapperFactory(new BeanNodeMapperFactory());
        this.selector.registerMapperFactory(new RecordNodeMapperFactory());
        this.selector.registerMapperFactory(new EnumNodeMapperFactory());

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
