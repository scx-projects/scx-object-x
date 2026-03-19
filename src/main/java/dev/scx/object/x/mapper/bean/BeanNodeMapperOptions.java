package dev.scx.object.x.mapper.bean;

import dev.scx.object.x.TypeNodeMapperOptions;

public final class BeanNodeMapperOptions implements TypeNodeMapperOptions {

    private BeanFieldWritePolicy beanFieldWritePolicy;
    private BeanFieldReadPolicy beanFieldReadPolicy;

    public BeanNodeMapperOptions() {
        this.beanFieldWritePolicy = null;
        this.beanFieldReadPolicy = null;
    }

    public BeanFieldWritePolicy beanFieldWritePolicy() {
        return beanFieldWritePolicy;
    }

    public BeanNodeMapperOptions beanFieldWritePolicy(BeanFieldWritePolicy beanFieldWritePolicy) {
        this.beanFieldWritePolicy = beanFieldWritePolicy;
        return this;
    }

    public BeanFieldReadPolicy beanFieldReadPolicy() {
        return beanFieldReadPolicy;
    }

    public BeanNodeMapperOptions beanFieldReadPolicy(BeanFieldReadPolicy beanFieldReadPolicy) {
        this.beanFieldReadPolicy = beanFieldReadPolicy;
        return this;
    }

}
