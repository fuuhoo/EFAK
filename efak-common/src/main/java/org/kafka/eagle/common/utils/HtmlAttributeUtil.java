/**
 * HtmlAttributeUtil.java
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
package org.kafka.eagle.common.utils;

import cn.hutool.core.util.StrUtil;
import org.kafka.eagle.common.constants.KConstants;

/**
 * Description: TODO
 *
 * @Author: smartloli
 * @Date: 2023/6/4 20:58
 * @Version: 3.4.0
 */
public class HtmlAttributeUtil {

    private HtmlAttributeUtil() {

    }

    public static String getClusterStatusHtml(int status) {
        String result = "";
        if (status == 0) {
            result = "<span class='badge bg-danger'>异常</span>";
        } else if (status == 1) {
            result = "<span class='badge bg-success'>健康</span>";
        } else if (status == 2) {
            result = "<span class='badge bg-secondary'>初始化</span>";
        }
        return result;
    }

    public static String getUserRoleHtml(String role) {
        String result = "";
        if ("ROLE_ADMIN".equals(role)) {
            result = "<span class='badge bg-success'>管理员</span>";
        } else if ("ROLE_DEV".equals(role)) {
            result = "<span class='badge bg-primary'>开发</span>";
        } else if ("ROLE_TEST".equals(role)) {
            result = "<span class='badge bg-secondary'>测试</span>";
        }
        return result;
    }

    public static String getAlertChannelHtml(String type) {
        String result = "";
        if (KConstants.AlertChannel.WECHAT.equals(type)) {
            result = "<span class='badge bg-success'>" + KConstants.AlertChannel.WECHAT_NAME + "</span>";
        } else if (KConstants.AlertChannel.DINGDING.equals(type)) {
            result = "<span class='badge bg-primary'>" + KConstants.AlertChannel.DINGDING_NAME + "</span>";
        } else if (KConstants.AlertChannel.EMAIL.equals(type)) {
            result = "<span class='badge bg-secondary'>" + KConstants.AlertChannel.EMAIL_NAME + "</span>";
        }
        return result;
    }

    public static String getAlertChannelUrlHtml(String url, Long id, String type) {
        String result = "";
        if (StrUtil.isBlank(url)) {
            return "无";
        }
        if (url.length() > 0 && url.length() < 32) {
            result = "<a href='' name='" + type + "' alert_channel_url_len_id='" + id + "'>" + url + "</a>";
        } else {
            result = "<a href='' name='" + type + "' alert_channel_url_len_id='" + id + "'>" + url.substring(0, 32) + "...</a>";
        }

        return result;
    }

    public static String getTopicSpreadHtml(int brokerSpread) {
        String result = "";
        if (brokerSpread < KConstants.Topic.TOPIC_BROKER_SPREAD_ERROR) {
            String msg="异常("+brokerSpread+"%)";
            result = "<span class='badge bg-danger'>"+msg+"</span>";
        } else if (brokerSpread >= KConstants.Topic.TOPIC_BROKER_SPREAD_ERROR && brokerSpread < KConstants.Topic.TOPIC_BROKER_SPREAD_NORMAL) {
            String msg="告警("+brokerSpread+"%)";
            result = "<span class='badge bg-danger'>"+msg+"</span>";
        } else if (brokerSpread >= KConstants.Topic.TOPIC_BROKER_SPREAD_NORMAL) {
            result = "<span class='badge bg-success'>健康</span>";
        } else {
            result = "<span class='badge bg-secondary'>未知</span>";
        }
        return result;
    }

    public static String getPreferredLeader(boolean preferredLeader) {
        String result = "";
        if (preferredLeader) {
            result = "<span class='badge bg-success'>健康</span>";
        } else {
            result = "<span class='badge bg-danger'>异常</span>";
        }
        return result;
    }

    public static String getUnderReplicated(boolean underReplicated) {
        String result = "";
        if (!underReplicated) { // false => healthy
            result = "<span class='badge bg-success'>健康</span>";
        } else {
            result = "<span class='badge bg-danger'>异常</span>";
        }
        return result;
    }

    public static String getTopicSkewedHtml(int brokerSkewed) {
        String result = "";
        if (brokerSkewed >= KConstants.Topic.TOPIC_BROKER_SKEW_ERROR) {
            result = "<span class='badge bg-danger'>异常</span>";
        } else if (brokerSkewed > KConstants.Topic.TOPIC_BROKER_SKEW_NORMAL && brokerSkewed < KConstants.Topic.TOPIC_BROKER_SKEW_ERROR) {
            result = "<span class='badge bg-warning'>警告</span>";
        } else if (brokerSkewed <= KConstants.Topic.TOPIC_BROKER_SKEW_NORMAL) {
            result = "<span class='badge bg-success'>健康</span>";
        } else {
            result = "<span class='badge bg-secondary'>未知</span>";
        }

        return result;
    }

    public static String getTopicLeaderSkewedHtml(int brokerLeaderSkewed) {
        String result = "";
        if (brokerLeaderSkewed >= KConstants.Topic.TOPIC_BROKER_LEADER_SKEW_ERROR) {
            result = "<span class='badge bg-danger'>异常</span>";
        } else if (brokerLeaderSkewed > KConstants.Topic.TOPIC_BROKER_LEADER_SKEW_NORMAL && brokerLeaderSkewed < KConstants.Topic.TOPIC_BROKER_LEADER_SKEW_ERROR) {
            result = "<span class='badge bg-warning'>警告</span>";
        } else if (brokerLeaderSkewed <= KConstants.Topic.TOPIC_BROKER_LEADER_SKEW_NORMAL) {
            result = "<span class='badge bg-success'>健康</span>";
        } else {
            result = "<span class='badge bg-secondary'>未知</span>";
        }

        return result;
    }

    public static String getAuthHtml(String auth) {
        String result = "";
        if ("Y".equals(auth)) {
            result = "<span class='badge bg-primary'>是</span>";
        } else {
            result = "<span class='badge bg-secondary'>否</span>";
        }
        return result;
    }

    public static String getConsumerGroupHtml(String state) {
        String result = "";
        if ("STABLE".equals(state)) {
            result = "<span class='badge bg-success'>消费中</span>";
        } else {
            result = "<span class='badge bg-danger'>已停止</span>";
        }
        return result;
    }

    public static String getConsumerGroupTopicHtml(Short status) {
        String result = "";
        if (status == 0) {
            result = "<span class='badge bg-success'>正常</span>";
        } else if (status == 1) {
            result = "<span class='badge bg-danger'>异常</span>";
        } else if (status == 2) {
            result = "<span class='badge bg-warning'>暂停</span>";
        }
        return result;
    }

}
