package it.lab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.lab.enums.TrangThaiSanPham;
import it.lab.enums.TrangThaiSanPhamSuKien;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sanphamsukien")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamSuKien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "sanphamid")
    @ManyToOne
    private SanPham sanPham;
    @JoinColumn(name = "sukiengiamgiaid")
    @ManyToOne
    private SuKienGiamGia suKienGiamGia;
    @Column(name = "ngaytao")
    private LocalDateTime ngayTao;
    @Column(name = "ngaycapnhat")
    private LocalDateTime ngayCapNhat;
    @Column(name = "trangthai")
    private TrangThaiSanPhamSuKien trangThai;
}
