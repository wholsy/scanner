package com.whosly.scanner.config;

import lombok.*;

/**
 * 扫描任务对象
 *
 * @Author yueny09 <deep_blue_yang@126.com>
 * @Date 2019-09-05 11:11
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ScanTaskId {
    /**
     * 扫描任务ID
     */
    private String taskId;
}
