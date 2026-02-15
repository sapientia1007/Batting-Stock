package org.example.battingstock.domain.player.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.event.StockPriceUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Slf4j
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class PriceChangeLogController {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분 타임아웃
        emitters.add(emitter);

        // 연결 종료나 에러 시 리스트에서 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    // 서비스에서 발행한 이벤트를 수신하는 리스너
    @EventListener
    public void handleStockPriceUpdate(StockPriceUpdateEvent event) {
        log.info("주가 변동 이벤트 수신! 브로드캐스트 시작...");

        // 연결된 모든 브라우저에게 데이터 전송
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("playerRecordUpdate") // 프론트엔드에서 받을 이벤트 이름
                        .data(event.latestLogs()));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}