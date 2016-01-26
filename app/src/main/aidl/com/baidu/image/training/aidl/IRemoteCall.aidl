// IRemoteCall.aidl
package com.baidu.image.training.aidl;
import com.baidu.image.training.aidl.IRemoteCallback;
// Declare any non-default types here with import statements

interface IRemoteCall {
    void foo();
    void registerCallback(IRemoteCallback cb);
    void unregisterCallback(IRemoteCallback cb);
}
