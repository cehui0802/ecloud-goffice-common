package cn.gwssi.ecloudbpm.goffice.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMQConfig {
    @Value("${jms.goffice.url:https://bpm.ecloud.work/ecloud-bpm-system}")
    private String url;

    @Value("${dataSet.url:https://bpm.ecloud.work/ecloud-bpm-dataset}")
    private String dataSetUrl;

}
