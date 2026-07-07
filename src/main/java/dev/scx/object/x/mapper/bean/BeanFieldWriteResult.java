package dev.scx.object.x.mapper.bean;

public final class BeanFieldWriteResult {

    private static final BeanFieldWriteResult SKIP_BEAN_FIELD_WRITE_RESULT = new BeanFieldWriteResult(true, null, null);

    private final boolean skip;
    private final String name;
    private final Object value;

    private BeanFieldWriteResult(boolean skip, String name, Object value) {
        if (!skip && name == null) {
            throw new NullPointerException("name can not be null");
        }
        this.skip = skip;
        this.name = name;
        this.value = value;
    }

    public static BeanFieldWriteResult of(String name, Object value) {
        return new BeanFieldWriteResult(false, name, value);
    }

    public static BeanFieldWriteResult ofSkip() {
        return SKIP_BEAN_FIELD_WRITE_RESULT;
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
