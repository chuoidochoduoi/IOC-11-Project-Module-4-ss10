package org.example.inventoryservice.consumer;

import lombok.RequiredArgsConstructor;
import org.example.inventoryservice.dto.OrderEvent;
import org.example.inventoryservice.entity.Medicine;
import org.example.inventoryservice.entity.Order;
import org.example.inventoryservice.repository.MedicineRepository;
import org.example.inventoryservice.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final MedicineRepository medicineRepository;
    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "medicine-stock-events",
            groupId = "inventory-group"
    )
    public void listen(OrderEvent orderEvent) {
        System.out.println("=== Đã nhận được sự kiện đơn hàng từ Kafka ===");
        System.out.println("Order ID: " + orderEvent.getOrderId());
        System.out.println("Medicine ID: " + orderEvent.getMedicineId());
        System.out.println("Quantity: " + orderEvent.getQuantity());
        System.out.println("Timestamp: " + orderEvent.getTimestamp());

        Order order = new Order();
        order.setOrderId(orderEvent.getOrderId());
        order.setMedicineId(orderEvent.getMedicineId());
        order.setQuantity(orderEvent.getQuantity());
        order.setTimestamp(orderEvent.getTimestamp());
        orderRepository.save(order);
        System.out.println("✓ Đã thêm mới đơn hàng vào database");

        Medicine medicine = medicineRepository.findById(orderEvent.getMedicineId()).orElse(null);
        if (medicine != null) {
            medicine.setStock(medicine.getStock() - orderEvent.getQuantity());
            medicineRepository.save(medicine);
            System.out.println("✓ Đã trừ stock cho thuốc: " + orderEvent.getMedicineId());
        } else {
            System.out.println("✗ Thuốc không tồn tại: " + orderEvent.getMedicineId());
        }
    }
}