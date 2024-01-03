package it.lab.service;

import it.lab.common.Email;
import it.lab.common.Template;
import it.lab.dto.*;
import it.lab.entity.*;
import it.lab.enums.TrangThaiDiaChi;
import it.lab.enums.TrangThaiHoaDon;
import it.lab.enums.TrangThaiNguoiDung;
import it.lab.enums.TrangThaiQuetMa;
import it.lab.iservice.IMuaTaiQuayService;
import it.lab.modelcustom.request.MuaTaiQuayRequest;
import it.lab.modelcustom.respon.HoaDonChoTaiCuaHang;
import it.lab.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MuaTaiQuayService implements IMuaTaiQuayService {
    @Autowired
    private HoaDonRepo _hoaDonRepo;
    @Autowired
    private NguoiDungRepo _nguoiDungRepo;
    @Autowired
    private SanPhamChiTietRepo _sanPhamChiTietRepo;
    @Autowired
    private HoaDonChiTietRepo _hoaDonChiTietRepo;
    @Autowired
    private DiaChiRepo _diaChiRepo;
    @Autowired
    private PhuongThucThanhToanRepo _phuongThucThanhToanRepo;
    @Autowired
    private PhuongThucVanChuyenRepo _phuongThucVanChuyenRepo;
    @Autowired
    private RankKhachHangRepo _rankKhachRepo;
    @Autowired
private SanPhamRepo _sanPhamRepo;
    @Override
    public List<HoaDonChoTaiCuaHang> layDanhSachTaiCuaHang() {
        return HoaDonChoTaiCuaHang.fromCollection(_hoaDonRepo.findAll());
    }

    @Override
    public String taoMoiHoaDon(Long nhanVienId) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNhanVien(_nguoiDungRepo.findById(nhanVienId).get());
        hoaDon.setGiaTriHd(0d);
        hoaDon.setPhiGiaoHang(0d);
        hoaDon.setTrangThai(TrangThaiHoaDon.HOADONCHO);
        _hoaDonRepo.save(hoaDon);
        hoaDon.setMaHoaDon("HD" + hoaDon.getId());
        _hoaDonRepo.save(hoaDon);
        return hoaDon.getMaHoaDon();
    }

    @Override
    public List<SanPhamChiTietDTO> layHetChiTiet() {
        return SanPhamChiTietDTO.fromCollection(_sanPhamChiTietRepo.findAll());
    }

    @Override
    public List<HoaDonChiTietDTO> gioHangCuaHoaDon(Long hoaDonId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        return HoaDonChiTietDTO.fromCollection(_hoaDonChiTietRepo.findHoaDonChiTietsByHoaDon(hoaDon));
    }

    @Override
    public List<HoaDonChiTietDTO> themSanPhamVaoHoaDon(Long hoaDonId, Long sanPhamId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        SanPhamChiTiet sanPhamChiTiet = _sanPhamChiTietRepo.findById(sanPhamId).get();
        HoaDonChiTiet hoaDonNew = new HoaDonChiTiet();
        hoaDonNew.setHoaDon(hoaDon);
        hoaDonNew.setSoLuong(0);
        hoaDonNew.setSanPhamChiTiet(sanPhamChiTiet);
        hoaDonNew.setNgayTao(LocalDateTime.now());
        hoaDonNew.setDonGia(sanPhamChiTiet.getGiaBan());
        _hoaDonChiTietRepo.save(hoaDonNew);
        _hoaDonRepo.save(hoaDon);
        return gioHangCuaHoaDon(hoaDonId);
    }

    @Override
    public HoaDonDTO layHoaDon(Long hoaDonId) {
        return HoaDonDTO.fromEntity(_hoaDonRepo.findById(hoaDonId).get());
    }

    @Override
    public List<NguoiDungDTO> layDanhSachKhachHang() {
        return NguoiDungDTO.fromCollection(_nguoiDungRepo.findAll());
    }

    @Override
    public List<DiaChiDTO> layDiaChiNguoiDung(Long nguoiDungId) {
        return DiaChiDTO.fromCollection(_diaChiRepo.findDiaChisByNguoiDung(_nguoiDungRepo.findById(nguoiDungId).get()));
    }

    private void thayDoiSoLuongKhiConfirmHoaDon(long hoaDonId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        var chiTiet = _hoaDonChiTietRepo.findHoaDonChiTietsByHoaDon(hoaDon);
        for (var item : chiTiet) {
            SanPhamChiTiet sp = item.getSanPhamChiTiet();
            SanPham sanPham = sp.getSanPham();
            sanPham.setSoLuongTon(sanPham.getSoLuongTon() - item.getSoLuong());
            sanPham.setSoLuongDaBan(sanPham.getSoLuongDaBan() + item.getSoLuong());
            sp.setSoLuongDaBan(sp.getSoLuongDaBan() + item.getSoLuong());
            sp.setSoLuongTon(sp.getSoLuongTon() - item.getSoLuong());
            _sanPhamChiTietRepo.save(sp);
            _sanPhamRepo.save(sanPham);
        }
    }

    @Override
    public Boolean taoHoaDonTaiQuay(MuaTaiQuayRequest muaTaiQuayRequest) {
        HoaDon hoaDon = _hoaDonRepo.findById(muaTaiQuayRequest.getHoaDonId()).get();
        NguoiDung nguoiDung = _nguoiDungRepo.findById(muaTaiQuayRequest.getKhachHangId()).get();
        PhuongThucThanhToan phuongThucThanhToan = _phuongThucThanhToanRepo.findById(muaTaiQuayRequest.getPhuongThucThanhToan()).get();
        hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
        PhuongThucVanChuyen phuongThucVanChuyen = _phuongThucVanChuyenRepo.findById(muaTaiQuayRequest.getPhuongThucVanChuyen()).get();
        hoaDon.setPhuongThucVanChuyen(phuongThucVanChuyen);
        if (muaTaiQuayRequest.getIsCoDiaChiMoi()) {
            DiaChi diaChi = new DiaChi();
            diaChi.setChiTietDiaChi(muaTaiQuayRequest.getChiTietDiaChi());
            diaChi.setNgayTao(LocalDateTime.now());
            diaChi.setHuyenId(muaTaiQuayRequest.getHuyenId());
            diaChi.setHuyen(muaTaiQuayRequest.getHuyen());
            diaChi.setTinh(muaTaiQuayRequest.getTinhId());
            diaChi.setTinh(muaTaiQuayRequest.getTinh());
            diaChi.setXaId(muaTaiQuayRequest.getXaId());
            diaChi.setXa(muaTaiQuayRequest.getXa());
            diaChi.setNguoiDung(nguoiDung);
            diaChi.setLaDiaChiChinh(false);
            diaChi.setTrangThai(TrangThaiDiaChi.HOATDONG);
            diaChi.setSoDienThoai(muaTaiQuayRequest.getSoDienThoai());
            _diaChiRepo.save(diaChi);
            hoaDon.setTrangThai(TrangThaiHoaDon.CHOGIAOHANG);
            hoaDon.setDiaChiGiao(diaChi);
        } else {
            hoaDon.setTrangThai(TrangThaiHoaDon.DAGIAO);
        }

        hoaDon.setNguoiMua(nguoiDung);
        if (muaTaiQuayRequest.getPhuongThucVanChuyen() == 1) {
            hoaDon.setPhiGiaoHang(muaTaiQuayRequest.getPhiVanChuyen());
            hoaDon.setGiaTriHd(hoaDon.getGiaTriHd() + muaTaiQuayRequest.getPhiVanChuyen());
        }
        if (muaTaiQuayRequest.getPhuongThucVanChuyen() == 3) {
            hoaDon.setNgayGiao(LocalDateTime.now());
        }
        _hoaDonRepo.save(hoaDon);
        thayDoiSoLuongKhiConfirmHoaDon(hoaDon.getId());
        String thongbao = Template.hoaDonMoi(hoaDon);
        guiThongBaoChoNhanVien(thongbao);
        return true;
    }

    private void guiThongBaoChoNhanVien(String thongBao) {
//        List<NguoiDung> lst = _nguoiDungRepo.findAll().stream().filter(x -> {
//            for (var item : x.getQuyenNguoiDungList()) {
//                if (item.getQuyen().getId() == 3 || item.getQuyen().getId() == 2) {
//                    return true;
//                }
//            }
//            return false;
//        }).toList();
//        for (var item : lst) {
//            if (item.getEmail() != null) {
//                Email email = new Email();
//                email.sendContentHTML(item.getEmail(), "Hóa đơn mới", thongBao);
//            }
//        }
    }

    @Override
    public String[] taoHoaDonTaiQuayThanhToanVNPAY(MuaTaiQuayRequest muaTaiQuayRequest) {
        String[] res = new String[2];
        HoaDon hoaDon = _hoaDonRepo.findById(muaTaiQuayRequest.getHoaDonId()).get();
        NguoiDung nguoiDung = _nguoiDungRepo.findById(muaTaiQuayRequest.getKhachHangId()).get();
        PhuongThucThanhToan phuongThucThanhToan = _phuongThucThanhToanRepo.findById(muaTaiQuayRequest.getPhuongThucThanhToan()).get();
        hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
        PhuongThucVanChuyen phuongThucVanChuyen = _phuongThucVanChuyenRepo.findById(muaTaiQuayRequest.getPhuongThucVanChuyen()).get();
        hoaDon.setPhuongThucVanChuyen(phuongThucVanChuyen);
        if (!muaTaiQuayRequest.getKoDungDiaChi()) {
            if (muaTaiQuayRequest.getIsCoDiaChiMoi()) {
                DiaChi diaChi = new DiaChi();
                diaChi.setChiTietDiaChi(muaTaiQuayRequest.getChiTietDiaChi());
                diaChi.setNgayTao(LocalDateTime.now());
                diaChi.setHuyenId(muaTaiQuayRequest.getHuyenId());
                diaChi.setHuyen(muaTaiQuayRequest.getHuyen());
                diaChi.setTinh(muaTaiQuayRequest.getTinhId());
                diaChi.setTinh(muaTaiQuayRequest.getTinh());
                diaChi.setXaId(muaTaiQuayRequest.getXaId());
                diaChi.setXa(muaTaiQuayRequest.getXa());
                diaChi.setNguoiDung(nguoiDung);
                diaChi.setLaDiaChiChinh(false);
                diaChi.setTrangThai(TrangThaiDiaChi.HOATDONG);
                diaChi.setSoDienThoai(muaTaiQuayRequest.getSoDienThoai());
                _diaChiRepo.save(diaChi);
                hoaDon.setDiaChiGiao(diaChi);
            } else {
                DiaChi diaChi = _diaChiRepo.findById(muaTaiQuayRequest.getDiaChiId()).get();
                hoaDon.setDiaChiGiao(diaChi);

            }
        }
        hoaDon.setNguoiMua(nguoiDung);
        if (muaTaiQuayRequest.getPhuongThucVanChuyen() == 1) {
            hoaDon.setPhiGiaoHang(muaTaiQuayRequest.getPhiVanChuyen());
            hoaDon.setGiaTriHd(hoaDon.getGiaTriHd() + muaTaiQuayRequest.getPhiVanChuyen());
            hoaDon.setTrangThai(TrangThaiHoaDon.CHOGIAOHANG);
        }
        if (muaTaiQuayRequest.getPhuongThucVanChuyen() == 3) {
            hoaDon.setNgayGiao(LocalDateTime.now());
            hoaDon.setTrangThai(TrangThaiHoaDon.DAGIAO);
        }
        _hoaDonRepo.save(hoaDon);
        thayDoiSoLuongKhiConfirmHoaDon(hoaDon.getId());
        res[0] = hoaDon.getMaHoaDon();
        res[1] = String.valueOf(hoaDon.getGiaTriHd().intValue());
        return res;
    }

    private Long taoMoiNguoiDung(MuaTaiQuayRequest mua) {
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setEmail(mua.getEmail());
        nguoiDung.setTen(mua.getNguoiNhan());
        nguoiDung.setHo(mua.getHoNguoiNhan());
        nguoiDung.setDiem(0);
        nguoiDung.setGioiTinh(false);
        nguoiDung.setMatKhau(UUID.randomUUID().toString());
        nguoiDung.setNgayTao(LocalDateTime.now());
        nguoiDung.setTrangThai(TrangThaiNguoiDung.HOATDONG);
        nguoiDung.setRankKhachHang(_rankKhachRepo.findById(1l).get());
        _nguoiDungRepo.save(nguoiDung);
        nguoiDung.setMaNguoiDung("MEM" + nguoiDung.getId());
        _nguoiDungRepo.save(nguoiDung);
        return nguoiDung.getId();
    }

    @Override
    public TrangThaiQuetMa quetMa(String maSp, Long hoaDonId) {
        HoaDon hoaDon = _hoaDonRepo.findById(hoaDonId).get();
        Optional<SanPhamChiTiet> sanPhamChiTiet = _sanPhamChiTietRepo.findSanPhamChiTietByMaSanPham(maSp);
        if (sanPhamChiTiet.isEmpty()) {
            return TrangThaiQuetMa.KHONGTONTAI;
        }
        if (sanPhamChiTiet.get().getSoLuongTon() == 0) {
            return TrangThaiQuetMa.HETHANG;
        }
        Optional<HoaDonChiTiet> a = _hoaDonChiTietRepo.findHoaDonChiTietByHoaDonAndSanPhamChiTiet(hoaDon, sanPhamChiTiet.get());
        if (!a.isEmpty()) {
            return TrangThaiQuetMa.DACO;
        }
        HoaDonChiTiet hoaDonNew = new HoaDonChiTiet();
        hoaDonNew.setHoaDon(hoaDon);
        hoaDonNew.setSoLuong(0);
        hoaDonNew.setSanPhamChiTiet(sanPhamChiTiet.get());
        hoaDonNew.setNgayTao(LocalDateTime.now());
        hoaDonNew.setDonGia(sanPhamChiTiet.get().getGiaBan());
        _hoaDonChiTietRepo.save(hoaDonNew);
        _hoaDonRepo.save(hoaDon);
        return TrangThaiQuetMa.THANHCONG;
    }
}
