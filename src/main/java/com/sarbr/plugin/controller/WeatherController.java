package com.sarbr.plugin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarbr.plugin.mapper.SysParamMapper;
import com.sarbr.plugin.support.Weather;
import com.sarbr.plugin.utils.AlertHelper;
import com.sarbr.plugin.utils.SpringContextUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FXMLController
public class WeatherController {

    public WebView content;
    public TextField area;
    public ChoiceBox<String> type;

    private final Map<String, String> errorMsg = new HashMap<>();

    private final Map<String, TimeValue> cache = new HashMap<>();

    public void initialize(){
        this.errorMsg.put("city", "请输入地区如城市类：天津或区类：西青！");
        this.errorMsg.put("ip", "请输入IP地址！");
        this.errorMsg.put("location", "请输入经纬度(纬度在前，如：39.983424,116.322987)");
    }

    public void search(ActionEvent actionEvent) {
        final String text = area.getText();
        final String selectedItem = type.getSelectionModel().getSelectedItem();
        if (text.isEmpty()) {
            AlertHelper.errAlter(errorMsg.get(selectedItem));
        } else {
            final SysParamMapper sysParamMapper = SpringContextUtil.getBean(SysParamMapper.class);
            final String appcode = sysParamMapper.getByCode("appcode");
            if(StringUtils.isEmpty(appcode)){
                AlertHelper.errAlter("请前往 开始-参数配置 中设置[appcode]");
            }else{
                String weatherJson;
                if(cache.containsKey(text)){
                    final TimeValue timeValue = cache.get(text);
                    //简单设置30分钟间隔查询一次
                    if(System.currentTimeMillis() - timeValue.getTime() > 30 * 60 * 1000){
                        weatherJson = getWeatherJson(appcode);
                    }else{
                        weatherJson = timeValue.getValue();
                    }
                }else{
                    weatherJson = getWeatherJson(appcode);
                }
                content.getEngine().loadContent(toHtml(weatherJson));
            }
        }
    }

    private String getIpUrl() {
        final String selectedItem = type.getSelectionModel().getSelectedItem();
        final String text = area.getText();
        String IP_URL = "https://jisutqybmf.market.alicloudapi.com/weather/query";
        return IP_URL + "?" + selectedItem + "=" + text;
    }

    private String getWeatherJson(String appcode) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "APPCODE " + appcode);
        String body = "";
        final RestTemplate restTemplate = SpringContextUtil.getBean("restTemplate");
        final ResponseEntity<String> forEntity = restTemplate.exchange(getIpUrl(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if(forEntity.getStatusCode().isError()){
            AlertHelper.errAlter("查询次数已使用完，此时需要重新购买");
        }else{
            body = forEntity.getBody();
            cache.put(area.getText(), new TimeValue(System.currentTimeMillis(), body));
        }
        return body;
    }

    private String toHtml(String weatherJson) {
        final ObjectMapper objectMapper = SpringContextUtil.getBean(ObjectMapper.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        try {
            final JsonNode result = objectMapper.readTree(weatherJson).get("result");
            Weather weather = objectMapper.readValue(result.toString(), Weather.class);
            final List<Weather.IndexBean> indexBeanList = weather.getIndex();
            for (Weather.IndexBean indexBean : indexBeanList) {
                sb2.append(" <p style=\"color:#2A2A2A\">")
                        .append(indexBean.getIname()).append(":").append(indexBean.getDetail()).append(indexBean.getIvalue())
                        .append("</p>");
            }
            //AQI指数
            final Weather.AqiBean.AqiinfoBean aqiinfo = weather.getAqi().getAqiinfo();
            sb.append(" ").append("<h2>天气情况").append("<span style=\"font-size:15px;color:#FF0000\"> 更新时间：".concat(weather.getUpdatetime())).append("</span>").append("</h2>")
                    .append("<p style=\"color:#2A2A2A\">")
                    .append(weather.getCity().concat(":")).append(weather.getWeek()).append("，天气：").append(weather.getWeather()).append("，").append(weather.getWinddirect().concat("~").concat(weather.getWindpower()))
                    .append("，").append(weather.getTemp()).append("°，最高温度:").append(weather.getTemphigh()).append("°，最低温度：")
                    .append(weather.getTemplow()).append("°，").append("湿度：").append(weather.getHumidity()).append("，气压：").append(weather.getPressure())
                    .append("</p>")
                    .append("<h2>天气指数信息</h2>")
                    .append(sb2)
                    .append("<h2>AQI指数信息</h2>")
                    .append("<p style=\"color:#2A2A2A\">").append("对健康影响：").append(aqiinfo.getAffect()).append("</p>")
                    .append("<p style=\"color:#2A2A2A\">").append("建议采取的措施：").append(aqiinfo.getMeasure()).append("</p>");

        } catch (IOException ignored) {
        }
        return sb.toString();
    }

    private static class TimeValue{
        private Long time;
        private String value;

        public TimeValue(Long time, String value) {
            this.time = time;
            this.value = value;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeValue)) return false;

            TimeValue timeValue = (TimeValue) o;

            return getTime() != null ? getTime().equals(timeValue.getTime()) : timeValue.getTime() == null;
        }

        @Override
        public int hashCode() {
            return getTime() != null ? getTime().hashCode() : 0;
        }
    }
}
