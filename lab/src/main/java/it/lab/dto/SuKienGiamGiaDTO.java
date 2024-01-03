package it.lab.dto;


import it.lab.entity.SanPhamSuKien;
import it.lab.entity.SuKienGiamGia;
import it.lab.enums.TrangThaiSuKienGiamGia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuKienGiamGiaDTO {
    private Long id;
    private String tenSuKien;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String moTa;
    private String logoSuKien;
    private TrangThaiSuKienGiamGia trangThai;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
//    private List<SanPhamSuKien> sanPhamSuKienList;
    private Double giaTriGiam;

    public SuKienGiamGiaDTO(Long id, String tenSuKien, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, String moTa, String logoSuKien, TrangThaiSuKienGiamGia trangThai, LocalDateTime ngayTao, LocalDateTime ngayCapNhat, Double giaTriGiam, Object giaTriGiam1) {
    }

    public static SuKienGiamGiaDTO fromEntity(SuKienGiamGia entity) {
        return new SuKienGiamGiaDTO(
                entity.getId(),
                entity.getTenSuKien(),
                entity.getNgayBatDau(),
                entity.getNgayKetThuc(),
                entity.getMoTa(),
                entity.getLogoSuKien(),
                entity.getTrangThai(),
                entity.getNgayTao(),
                entity.getNgayCapNhat(),
                entity.getGiaTriGiam()
        );
    }

    public static List<SuKienGiamGiaDTO> fromCollection(List<SuKienGiamGia> collection) {
        List<SuKienGiamGiaDTO> to = new ArrayList<>();
        collection.forEach(x -> {
            to.add(fromEntity(x));
        });
        return to;
    }
}