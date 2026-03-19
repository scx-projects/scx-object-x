package dev.scx.object.x.mapper.record;

public final class RecordComponentWriteResult {

    private static final RecordComponentWriteResult SKIP_RECORD_COMPONENT_WRITE_RESULT = new RecordComponentWriteResult(true, null, null);

    private final boolean skip;
    private final String name;
    private final Object value;

    private RecordComponentWriteResult(boolean skip, String name, Object value) {
        if (!skip && name == null) {
            throw new NullPointerException("name can not be null");
        }
        this.skip = skip;
        this.name = name;
        this.value = value;
    }

    public static RecordComponentWriteResult of(String name, Object value) {
        return new RecordComponentWriteResult(false, name, value);
    }

    public static RecordComponentWriteResult ofSkip() {
        return SKIP_RECORD_COMPONENT_WRITE_RESULT;
    }

    public boolean skip() {
        return skip;
    }

    public String name() {
        return name;
    }

    public Object value() {
        return value;
    }

}
