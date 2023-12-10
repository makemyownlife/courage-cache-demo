package com.courage.cache.service.commonspool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MyObjectFactory implements PooledObjectFactory<MyObject> {

    @Override
    public PooledObject<MyObject> makeObject() throws Exception {
        // 创建一个新对象
        MyObject object = new MyObject();
        // 初始化对象
        object.initialize();
        return new DefaultPooledObject<>(object);
    }

    @Override
    public void destroyObject(PooledObject<MyObject> p) throws Exception {
        // 销毁对象
        p.getObject().destroy();
    }

    @Override
    public boolean validateObject(PooledObject<MyObject> p) {
        return p.getObject().isValid();
    }

    @Override
    public void activateObject(PooledObject<MyObject> p) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<MyObject> p) throws Exception {
    }

}
