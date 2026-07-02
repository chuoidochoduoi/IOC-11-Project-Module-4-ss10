package org.example.inventoryservice.repository;

import org.example.inventoryservice.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, String> {

    @Modifying
    @Transactional
    @Query("UPDATE Medicine m SET m.stock = m.stock - :quantity WHERE m.id = :medicineId")
    void decreaseStock(String medicineId, Integer quantity);
}