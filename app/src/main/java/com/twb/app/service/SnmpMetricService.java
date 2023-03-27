package com.twb.app.service;

import com.twb.app.dto.MetricDTO;
import com.twb.app.dto.SnmpWalkDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class SnmpMetricService {
    private static final Logger log = LoggerFactory.getLogger(SnmpMetricService.class);
    private static final int SCHEDULE_IN_SECONDS = 2;
    private static final Random RANDOM = new SecureRandom();

    @Scheduled(fixedDelay = SCHEDULE_IN_SECONDS, timeUnit = TimeUnit.SECONDS)
    public void scheduledSend() {
        SnmpWalkDTO walk = new SnmpWalkDTO();
        walk.setIfInOctets(getRandomNumber(10, 100));
        sendSnmpWalk(walk);
    }

    public SnmpWalkDTO sendSnmpWalk(SnmpWalkDTO walk) {
        if (walk.getSysUpTime() == null) {
            walk.setSysUpTime(System.currentTimeMillis());
        }

        String body = String.format("""
                # TYPE  ifInOctets  gauge
                # TYPE  sysUpTime   gauge
                ifInOctets  {instance="127.0.0.1", monitor="SNMP", node_id="1"}    %d
                sysUpTime   {instance="127.0.0.1", monitor="SNMP", node_id="1"}    %d
                """, walk.getIfInOctets(), walk.getSysUpTime());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://prom-pushgateway:9091/metrics/job/snmp-data"))
                    .headers(HttpHeaders.CONTENT_TYPE, "text/plain; version=0.0.4")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 400) {
                log.info("Successful Response - {}", response.body());
            } else {
                log.error("Error Response - {}", response.body());
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException(e);
        }

        return walk;
    }

    public MetricDTO getMetric(String metricName) {

        MetricDTO metric = new MetricDTO();
        metric.setName(metricName);
        metric.setValue(123L);
        return metric;
    }

    private long getRandomNumber(long min, long max) {
        return RANDOM.nextLong(max - min + 1) + min;
    }
}
