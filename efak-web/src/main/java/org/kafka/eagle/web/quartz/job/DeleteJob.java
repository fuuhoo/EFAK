/**
 * DeleteJob.java
 * <p>
 * Copyright 2023 smartloli
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kafka.eagle.web.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.kafka.eagle.common.utils.CalendarUtil;
import org.kafka.eagle.pojo.topic.TopicSummaryInfo;
import org.kafka.eagle.web.annotation.QuartzJob;
import org.kafka.eagle.web.service.IAuditDaoService;
import org.kafka.eagle.web.service.ITopicSummaryDaoService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: TODO
 * @Author: smartloli
 * @Date: 2023/7/15 10:47
 * @Version: 3.4.0
 */
@Component
@Slf4j
@QuartzJob(group = "INIT_GROUP", cron = "0 * * * * ?")
public class DeleteJob extends QuartzJobBean {


    @Value("${efak.collect.retain}")
    private Integer retain;

    @Autowired
    private ITopicSummaryDaoService topicSummaryDaoService;

    @Autowired
    private IAuditDaoService auditDaoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Delete job has started, class = {}", this.getClass().getName());
        // logics
        this.deleteExpireDataTask();
    }

    private void deleteExpireDataTask() {
        log.info("EFAK schedule delete expire data task start");
        String day = CalendarUtil.getCustomLastDay("yyyy-MM-dd HH:mm:ss",this.retain);
        // 1. delete expire topic summary
        List<TopicSummaryInfo> topicSummaryInfos = this.topicSummaryDaoService.list(day);
        if (topicSummaryInfos != null && topicSummaryInfos.size() > 0) {
            List<Long> waitDeleteTopicSummaryIds = new ArrayList<>();
            for (TopicSummaryInfo topicSummaryInfo : topicSummaryInfos) {
                waitDeleteTopicSummaryIds.add(topicSummaryInfo.getId());
            }
            if (waitDeleteTopicSummaryIds != null && waitDeleteTopicSummaryIds.size() > 0) {
                this.topicSummaryDaoService.delete(waitDeleteTopicSummaryIds);
            }
        }

        // 2. delete expire audit log
        this.auditDaoService.delete(day);
    }

}
