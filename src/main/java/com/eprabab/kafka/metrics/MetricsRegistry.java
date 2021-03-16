package com.eprabab.kafka.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.AtomicDouble;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class MetricsRegistry {

    private final Map<String, AtomicDouble> gaugeCollector = new ConcurrentHashMap<>();

    @Autowired
    private MeterRegistry meterRegistry;

    public AtomicDouble createOrGetMetrics(final String gaugeName, final String tag) {
        final String gaugeWithAccount = StringUtils.joinWith("_", gaugeName);
        final AtomicDouble gauge = gaugeCollector.get(gaugeWithAccount);
        if (gauge != null) {
            return gauge;
        }
        synchronized (gaugeCollector) {
            if (gaugeCollector.get(gaugeWithAccount) == null) {
                final AtomicDouble obj = new AtomicDouble();
                Gauge.builder(gaugeName, obj, AtomicDouble::get).tags("account", tag).register(meterRegistry);
                gaugeCollector.putIfAbsent(gaugeWithAccount, obj);
            }
        }
        return gaugeCollector.get(gaugeWithAccount);
    }
}
