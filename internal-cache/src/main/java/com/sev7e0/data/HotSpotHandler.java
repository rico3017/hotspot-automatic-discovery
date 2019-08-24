package com.sev7e0.data;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * annotation handler class
 */
@Component
@Aspect
public class HotSpotHandler {

    Logger logger = LoggerFactory.getLogger(HotSpotHandler.class);

    @Autowired
    KafkaTemplate kafka;

    /**
     * pointcut
     */
    @Pointcut("@annotation(com.sev7e0.data.HotSpot)")
    public void hotSpotLogging() {
    }


    @Around("com.sev7e0.data.HotSpotHandler.hotSpotLogging() && @annotation(hotspot)")
    public Object around(ProceedingJoinPoint joinPoint, HotSpot hotspot) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("hotspot-automatic-discovery准备向 Kafka 发送日志数据!");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("method name:{},params:{}", joinPoint.getSignature().getName(), joinPoint.getArgs());
        }

        List<Object> collect = Stream.of(joinPoint.getArgs()).collect(Collectors.toList());

        HotSpotLog log = new HotSpotLog.Builder()
                .value(collect)
                .className(joinPoint.getTarget().getClass().getName())
                .methodName(joinPoint.getSignature().getName())
                .builder();

        ProducerRecord<String, Object> record = new ProducerRecord<>(hotspot.name(),null, Instant.now().getEpochSecond(),null, JSON.toJSONString(log));

        kafka.send(record).addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("send failed");
            }

            @Override
            public void onSuccess(Object o) {
                logger.debug("send success");
            }
        });
        if (logger.isDebugEnabled()) {
            logger.debug("hotspot-automatic-discovery向 Kafka 发送日志数据 finish!");
        }
        return joinPoint.proceed();
    }

    @Before("com.sev7e0.data.HotSpotHandler.hotSpotLogging()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

}
