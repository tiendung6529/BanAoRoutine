package it.lab.repository;

import it.lab.entity.SuKienGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuKienGiamGiaRepo extends JpaRepository<SuKienGiamGia, Long> {
    @Query("SELECT s FROM SuKienGiamGia s WHERE s.trangThai =0 or s.trangThai =2 ORDER BY s.ngayCapNhat DESC")
    List<SuKienGiamGia> getAll();
}
