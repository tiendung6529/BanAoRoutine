package it.lab.controller;

import it.lab.entity.SanPham;
import it.lab.entity.SanPhamSuKien;
import it.lab.entity.SuKienGiamGia;
import it.lab.enums.TrangThaiSanPhamSuKien;
import it.lab.iservice.ISanPhamService;
import it.lab.iservice.ISanPhamSuKienService;
import it.lab.iservice.ISuKienGiamGiaService;
import it.lab.repository.NhomSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sanphamsukien")
public class SanPhamSuKienController {
    @Autowired
    ISanPhamSuKienService service;
    @Autowired
    ISanPhamService iSanPhamService;
    @Autowired
    ISuKienGiamGiaService suKienGiamGiaService;

    @Autowired
    private ISanPhamService _sanPhamService;
    @GetMapping("/get-page")
    public ResponseEntity<?> getPage(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        return ResponseEntity.ok(service.getPage(pageable));
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    @GetMapping("/get-nhomsp")
    public ResponseEntity<?> getAllNhomSP() {
        return ResponseEntity.ok(_sanPhamService.getAll());
    }
    @GetMapping("/get-all-sukiengiamgia")
    public ResponseEntity<?> getAllSuKienGiamGia() {
        return ResponseEntity.ok(suKienGiamGiaService.getAll());
    }

    @GetMapping("/getnhomsanpham")
    public ResponseEntity<List<SanPham>> getNhomSP(@RequestParam(name = "id") long id) {
        return ResponseEntity.ok(service.getSanPhamTheoNhom(id));
    }

    @GetMapping("/sanphame")
    public ResponseEntity<?> getSanPhamE() {
        return ResponseEntity.ok(service.getSanPhamE(11, 2023));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SanPhamSuKien sanPhamSuKien) {
        return ResponseEntity.ok(service.save(sanPhamSuKien));
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveTheoSanPhamE(@RequestParam(name = "id") long id,
                                              @RequestParam(name = "idSuKien") long idSK
    ) {
        LocalDateTime currentDate = LocalDateTime.now();
        SanPham sanPham = iSanPhamService.findById(id);
        SuKienGiamGia suKienGiamGia = suKienGiamGiaService.findById(idSK);
        SanPhamSuKien sanPhamSuKien = new SanPhamSuKien();
        sanPhamSuKien.setSanPham(sanPham);
        sanPhamSuKien.setSuKienGiamGia(suKienGiamGia);
        sanPhamSuKien.setNgayTao(currentDate);
        sanPhamSuKien.setNgayCapNhat(currentDate);
        sanPhamSuKien.setTrangThai(TrangThaiSanPhamSuKien.CHAY_SU_KIEN);
        service.save(sanPhamSuKien);
        return ResponseEntity.ok(service.save(sanPhamSuKien));
    }

    @PostMapping("/add-nhom-san-pham")
    public ResponseEntity<?> saveTheoSanPhamNhom(
            @RequestParam(name = "idSuKien") long idSK,
            @RequestParam(name = "id") long idNhom
    ) {
        List<SanPhamSuKien> sanPhamSuKienList=service.getAll();
        Map<Long,SanPhamSuKien> mapSP=new HashMap<>();
        for (SanPhamSuKien sanPhamSuKien : sanPhamSuKienList){
            mapSP.put(sanPhamSuKien.getSanPham().getId(),sanPhamSuKien);
        }
        SuKienGiamGia suKienGiamGia = suKienGiamGiaService.findById(idSK);
        List<SanPham> sanPhamList = service.getSanPhamTheoNhom(idNhom);
        SanPhamSuKien sanPhamSuKien=null;
        LocalDateTime currentDate = LocalDateTime.now();
        for (int i = 0; i < sanPhamList.size(); i++) {
            sanPhamSuKien=mapSP.get(sanPhamList.get(i).getId());
            if(sanPhamSuKien == null){
                sanPhamSuKien = new SanPhamSuKien();
                sanPhamSuKien.setSanPham(sanPhamList.get(i));
                sanPhamSuKien.setSuKienGiamGia(suKienGiamGia);
                sanPhamSuKien.setNgayTao(currentDate);
                sanPhamSuKien.setTrangThai(TrangThaiSanPhamSuKien.CHAY_SU_KIEN);
                service.save(sanPhamSuKien);
            }
        }
        return ResponseEntity.ok(sanPhamSuKien);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update( @PathVariable(name = "id") Long id) {
        SanPhamSuKien phamSuKien=service.findById(id);
        LocalDateTime time=LocalDateTime.now();
       if(phamSuKien.getTrangThai()==TrangThaiSanPhamSuKien.CHAY_SU_KIEN){
           phamSuKien.setTrangThai(TrangThaiSanPhamSuKien.NGUNG_SU_KIEN);
           phamSuKien.setNgayCapNhat(time);
       }
       else {
           phamSuKien.setTrangThai(TrangThaiSanPhamSuKien.CHAY_SU_KIEN);
           phamSuKien.setNgayCapNhat(time);
       }
        service.save(phamSuKien);
        return ResponseEntity.ok(phamSuKien);
    }
    @PutMapping("/update-ngung-su-kien-nhom")
    public ResponseEntity<?> ngungNhomSanPham(@RequestParam(name = "id") long idNhom
    ) {
        try {
            List<SanPham> sanPhamList = service.getSanPhamTheoNhom(idNhom);
            Map<Long, SanPham> sanPhamMap = new HashMap<>();
            for (SanPham sanPham : sanPhamList) {
                sanPhamMap.put(sanPham.getId(), sanPham);
            }
            List<SanPhamSuKien> sanPhamSuKienList = service.getAll();
            LocalDateTime currentDate = LocalDateTime.now();
            for (SanPhamSuKien sanPhamSuKien : sanPhamSuKienList) {
                SanPham sanPham = sanPhamMap.get(sanPhamSuKien.getSanPham().getId());
                if (sanPham != null && sanPhamSuKien.getTrangThai() == TrangThaiSanPhamSuKien.CHAY_SU_KIEN) {
                    sanPhamSuKien.setNgayCapNhat(currentDate);
                    sanPhamSuKien.setTrangThai(TrangThaiSanPhamSuKien.NGUNG_SU_KIEN);
                    service.save(sanPhamSuKien);
                }
            }
            return ResponseEntity.ok("Yêu cầu đã được xử lý thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi xử lý yêu cầu");
        }
    }
}
