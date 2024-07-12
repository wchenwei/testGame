package com.hm.libcore.db.mongo;

import com.hm.libcore.msg.JsonMsg;
import org.springframework.data.annotation.Transient;

/**
 * 用处继承团队消息
 *
 * @param <T>
 */
public class MuliDataComponent<T> extends ClassChanged {
    @Transient
    private transient T context;

    public final void LateInit(T context) {
        if (null == this.context) {
            this.context = context;
            initData();
        }
    }

    /**
     * 初始化肢体数据
     */
    public void initData() {

    }

    public final T Context() {
        return this.context;
    }

    public void fillMsg(JsonMsg msg, boolean ignoreChange) {
        if (ignoreChange || ClientChanged()) {
            fillMsg(msg);
            if (!ignoreChange) {
                ClearClientChanged();
            }
        }
    }

    protected void fillMsg(JsonMsg msg) {
    }
}
