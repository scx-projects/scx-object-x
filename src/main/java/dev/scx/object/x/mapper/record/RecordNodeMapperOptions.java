package dev.scx.object.x.mapper.record;

import dev.scx.object.x.TypeNodeMapperOptions;

public final class RecordNodeMapperOptions implements TypeNodeMapperOptions {

    private RecordComponentWritePolicy recordComponentWritePolicy;
    private RecordParameterReadPolicy recordParameterReadPolicy;

    public RecordNodeMapperOptions() {
        this.recordComponentWritePolicy = null;
        this.recordParameterReadPolicy = null;
    }

    public RecordComponentWritePolicy recordComponentWritePolicy() {
        return recordComponentWritePolicy;
    }

    public RecordNodeMapperOptions recordComponentWritePolicy(RecordComponentWritePolicy recordComponentWritePolicy) {
        this.recordComponentWritePolicy = recordComponentWritePolicy;
        return this;
    }

    public RecordParameterReadPolicy recordParameterReadPolicy() {
        return recordParameterReadPolicy;
    }

    public RecordNodeMapperOptions recordParameterReadPolicy(RecordParameterReadPolicy recordParameterReadPolicy) {
        this.recordParameterReadPolicy = recordParameterReadPolicy;
        return this;
    }

}
