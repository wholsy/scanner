package com.whosly.scanner.callback;

import java.util.List;

/**
 * Callback函数接口
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-04 20:50
 */
public interface ScannerCallback<T> {
    /**
     * 回调方法
     *
     * @param clzs
     */
    void callback(List<T> clzs);
}
