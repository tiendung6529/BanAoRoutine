package it.lab.modelcustom.request;

import it.lab.entity.SanPhamSuKien;
import it.lab.enums.TrangThaiSuKienGiamGia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuKienGiamGiaReque {
    private Long id;
    private String tenSuKien;
    private String moTa;
    private Double giaTriGiam;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
}
