package com.eprabab.kafka;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import com.eprabab.kafka.consumer.Callback;

@Component
public class TestCallBack implements Callback<ConsumerRecord<?, ?>> {

    final List<ConsumerRecord<?, ?>> allDate = new ArrayList<>();

    @Override
    public void call(final ConsumerRecord<?, ?> data) {
        allDate.add(data);
    }

    public List<ConsumerRecord<?, ?>> getReceivedData() {
        return allDate;
    }
}
