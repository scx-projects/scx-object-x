package dev.scx.object.x.mapper.bean;

public final class BeanFieldReadResult {

    private static final BeanFieldReadResult SKIP_BEAN_FIELD_READ_RESULT = new BeanFieldReadResult(true, null);

    private final boolean skip;
    private final String name;

    private BeanFieldReadResult(boolean skip, String name) {
        if (!skip && name == null) {
            throw new NullPointerException("name can not be null");
        }
        this.skip = skip;
        this.name = name;
    }

    public static BeanFieldReadResult of(String name) {
        return new BeanFieldReadResult(false, name);
    }

    public static BeanFieldReadResult ofSkip() {
        return SKIP_BEAN_FIELD_READ_RESULT;
    }

    public boolean skip() {
        return skip;
    }

    public String name() {
        return name;
    }

}
