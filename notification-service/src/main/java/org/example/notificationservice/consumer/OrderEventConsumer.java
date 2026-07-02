package org.example.notificationservice.consumer;

import org.example.notificationservice.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    @KafkaListener(
            topics = "medicine-stock-events",
            groupId = "notification-group"
    )
    public void listen(OrderEvent orderEvent,
                      @Header(KafkaHeaders.RECEIVED_KEY) String medicineId) {
        System.out.println("=== Đã nhận được sự kiện để gửi thông báo ===");
        System.out.println("Order ID: " + orderEvent.getOrderId());
        System.out.println("Medicine ID: " + orderEvent.getMedicineId());
        System.out.println("Quantity: " + orderEvent.getQuantity());

        sendInvoiceEmail(orderEvent, medicineId);
    }

    private void sendInvoiceEmail(OrderEvent orderEvent, String medicineId) {
        String customerEmail = "customer@example.com";
        String emailContent = String.format("""
                Kính gửi Quý khách hàng,

                Đơn hàng của bạn đã được xử lý thành công!

                Thông tin đơn hàng:
                - Mã đơn hàng: %s
                - Mã thuốc: %s
                - Số lượng: %d
                - Thời gian: %s

                Cảm ơn bạn đã mua hàng tại hệ thống của chúng tôi!
                """,
                orderEvent.getOrderId(),
                orderEvent.getMedicineId(),
                orderEvent.getQuantity(),
                orderEvent.getTimestamp());

        System.out.println("==============================================");
        System.out.println("✓ Đã gửi hóa đơn cho đơn hàng: " + orderEvent.getOrderId() + " với Key: " + medicineId);
        System.out.println("✓ Đã gửi thông báo tới email khách hàng: " + customerEmail);
        System.out.println("Nội dung email:");
        System.out.println(emailContent);
        System.out.println("==============================================");
    }
}