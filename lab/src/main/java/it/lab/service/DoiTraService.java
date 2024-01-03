package it.lab.service;

import it.lab.entity.HoaDon;
import it.lab.entity.HoaDonChiTiet;
import it.lab.entity.SanPham;
import it.lab.entity.SanPhamChiTiet;
import it.lab.enums.TrangThaiHoaDon;
import it.lab.iservice.IDoiTraService;
import it.lab.modelcustom.request.ChiTietDoiTra;
import it.lab.repository.HoaDonChiTietRepo;
import it.lab.repository.HoaDonRepo;
import it.lab.repository.SanPhamChiTietRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoiTraService implements IDoiTraService {
    @Autowired
    private HoaDonChiTietRepo _hoaDonChiTietRepo;
    @Autowired
    private HoaDonRepo _hoaDonRepo;
    @Autowired
    private SanPhamChiTietRepo _sanPhamChiTietRepo;

    @Override
    public List<HoaDonChiTiet> layHoaDonChiTietCuaHoaDon(Long hoaDonId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        return _hoaDonChiTietRepo.findHoaDonChiTietsByHoaDon(hoaDon);
    }

    @Override
    public Boolean doiTra(List<ChiTietDoiTra> data) {
        try {
            for (var item : data) {
                double totalGiaMoi = 0d;
                HoaDonChiTiet hoaDonChiTiet = _hoaDonChiTietRepo.findById(item.getChiTietId()).get();
                hoaDonChiTiet.setNgayCapNhat(LocalDateTime.now());
                hoaDonChiTiet.setTrangThai(1);
                hoaDonChiTiet.setSoLuongDoiTra(item.getSoLuong());
                SanPhamChiTiet sanPhamChiTiet = _sanPhamChiTietRepo.findById(hoaDonChiTiet.getSanPhamChiTiet().getId()).get();
                sanPhamChiTiet.setSoLuongLoi(sanPhamChiTiet.getSoLuongLoi() + item.getSoLuongLoi());
                sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() + item.getSoLuongDoiTra());
                sanPhamChiTiet.setSoLuongTraHang(sanPhamChiTiet.getSoLuongTraHang() + item.getSoLuongDoiTra());

                SanPham sanPham = sanPhamChiTiet.getSanPham();
                sanPham.setSoLuongTon(sanPham.getSoLuongTon() + item.getSoLuongDoiTra());
                sanPham.setSoLuongLoi(sanPham.getSoLuongLoi() + item.getSoLuongLoi());
                sanPham.setSoLuongTraHang(sanPham.getSoLuongTraHang() + item.getSoLuongDoiTra());
                hoaDonChiTiet.setGhiChu(item.getGhiChu());
                totalGiaMoi = item.getSoLuong() * sanPhamChiTiet.getGiaBan();
                _hoaDonChiTietRepo.save(hoaDonChiTiet);
                _sanPhamChiTietRepo.save(sanPhamChiTiet);
                HoaDon hoaDon = _hoaDonRepo.findById(hoaDonChiTiet.getHoaDon().getId()).get();
                hoaDon.setGiaTriHd(hoaDon.getGiaTriHd() - totalGiaMoi);
                if (item.getSoLuong() < hoaDonChiTiet.getSoLuong()) {
                    HoaDonChiTiet hoaDonChiTiet1 = new HoaDonChiTiet();
                    hoaDonChiTiet1.setHoaDon(hoaDon);
                    hoaDonChiTiet1.setSanPhamChiTiet(hoaDonChiTiet.getSanPhamChiTiet());
                    hoaDonChiTiet1.setDonGia(sanPhamChiTiet.getGiaBan());
                    hoaDonChiTiet1.setSoLuong(hoaDonChiTiet.getSoLuong() - item.getSoLuong());
                    hoaDonChiTiet1.setNgayTao(LocalDateTime.now());
                    totalGiaMoi += (hoaDonChiTiet.getSoLuong() - item.getSoLuong()) * sanPhamChiTiet.getGiaBan();
                    _hoaDonChiTietRepo.save(hoaDonChiTiet1);
                }
                for (var item2 : item.getDuLieuMoi()) {
                    SanPhamChiTiet spMoi = _sanPhamChiTietRepo.findById(item2.getId()).get();
                    Optional<HoaDonChiTiet> check = _hoaDonChiTietRepo.findHoaDonChiTietByHoaDonAndSanPhamChiTiet(hoaDon, spMoi);
                    if (check.isEmpty()) {
                        HoaDonChiTiet hoaDonChiTiet2 = new HoaDonChiTiet();
                        hoaDonChiTiet2.setHoaDon(hoaDon);
                        hoaDonChiTiet2.setSanPhamChiTiet(spMoi);
                        hoaDonChiTiet2.setDonGia(spMoi.getGiaBan());
                        hoaDonChiTiet2.setSoLuong(item2.getSoLuongDoi());
                        hoaDonChiTiet2.setNgayTao(LocalDateTime.now());
                        _hoaDonChiTietRepo.save(hoaDonChiTiet2);
                    } else {
                        check.get().setSoLuong(check.get().getSoLuong() + item2.getSoLuongDoi());
                        _hoaDonChiTietRepo.save(check.get());
                    }
                    spMoi.setSoLuongTon(item2.getSoLuongDoi());
                    totalGiaMoi += item2.getSoLuongDoi() * spMoi.getGiaBan();
                    _sanPhamChiTietRepo.save(spMoi);
                }
                hoaDon.setGiaTriHd(hoaDon.getGiaTriHd() + totalGiaMoi);
                hoaDon.setTrangThai(TrangThaiHoaDon.DADOITRA);
                hoaDon.setNgayCapNhat(LocalDateTime.now());
                _hoaDonRepo.save(hoaDon);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean huyDoiTra(Long hoaDonId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        hoaDon.setTrangThai(TrangThaiHoaDon.TUCHOIDOI);
        _hoaDonRepo.save(hoaDon);
        return true;
    }
}
