package com.sev7e0.data;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.stream.Stream;

@Component
@Aspect
public class HotSpotHandler {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Logger logger = LoggerFactory.getLogger(HotSpotHandler.class);

    @Autowired
    KafkaTemplate kafka;

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
        StringJoiner JOINER = new StringJoiner("\",\"", "[", "]");
        Stream.of(joinPoint.getArgs()).forEach(c -> JOINER.add(c.toString()));

        HotSpotLog log = new HotSpotLog.Builder()
                .dataTime(formatter.format(LocalDateTime.now()))
                .value(JOINER.toString())
                .className(joinPoint.getTarget().getClass().getName())
                .method(joinPoint.getSignature().getName())
                .builder();

        ProducerRecord<String, Object> record = new ProducerRecord<>(hotspot.name(), log.toString());

        kafka.send(record).addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("send failed");
            }

            @Override
            public void onSuccess(Object o) {
                logger.info("send success");
            }
        });
        if (logger.isDebugEnabled()) {
            logger.debug("hotspot-automatic-discovery向 Kafka 发送日志数据 finish!");
        }
        return joinPoint.proceed();
    }

}
