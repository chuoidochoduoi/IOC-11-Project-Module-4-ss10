package org.example.pharmacyservice.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.pharmacyservice.dto.OrderEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "medicine-stock-events";

    @PostMapping("/sell")
    public ResponseEntity<String> sellMedicine(@RequestBody SellRequest request) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(request.getOrderId() != null ? request.getOrderId() : UUID.randomUUID().toString());
        orderEvent.setMedicineId(request.getMedicineId());
        orderEvent.setQuantity(request.getQuantity());
        orderEvent.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));

        kafkaTemplate.send(TOPIC, orderEvent.getMedicineId(), orderEvent);

        return ResponseEntity.ok("Gửi thông tin thanh toán thành công");
    }

    @Getter
    @Setter
    public static class SellRequest {
        private String orderId;
        private String medicineId;
        private Integer quantity;
    }
}