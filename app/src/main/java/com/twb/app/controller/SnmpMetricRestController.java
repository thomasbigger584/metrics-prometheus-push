package com.twb.app.controller;

import com.twb.app.dto.MetricDTO;
import com.twb.app.dto.SnmpWalkDTO;
import com.twb.app.service.SnmpMetricService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnmpMetricRestController {
    private final SnmpMetricService service;

    public SnmpMetricRestController(SnmpMetricService service) {
        this.service = service;
    }

    @PostMapping("/snmp-walk")
    public SnmpWalkDTO sendSnmpWalk(@RequestBody SnmpWalkDTO walk) {
        return service.sendSnmpWalk(walk);
    }

    @GetMapping("/metric")
    public MetricDTO getMetric(@RequestParam("metric_name") String metricName) {
        return service.getMetric(metricName);
    }
}
