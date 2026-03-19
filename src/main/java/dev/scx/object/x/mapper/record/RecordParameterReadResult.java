package dev.scx.object.x.mapper.record;

public final class RecordParameterReadResult {

    private static final RecordParameterReadResult SKIP_RECORD_PARAMETER_READ_RESULT = new RecordParameterReadResult(true, null);

    private final boolean skip;
    private final String name;

    private RecordParameterReadResult(boolean skip, String name) {
        if (!skip && name == null) {
            throw new NullPointerException("name can not be null");
        }
        this.skip = skip;
        this.name = name;
    }

    public static RecordParameterReadResult of(String name) {
        return new RecordParameterReadResult(false, name);
    }

    public static RecordParameterReadResult ofSkip() {
        return SKIP_RECORD_PARAMETER_READ_RESULT;
    }

    public boolean skip() {
        return skip;
    }

    public String name() {
        return name;
    }

}
