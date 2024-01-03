package it.lab.service;

import it.lab.common.CloudinaryUpload;
import it.lab.common.Page;
import it.lab.common.ResponObject;
import it.lab.dto.SuKienGiamGiaDTO;
import it.lab.entity.SuKienGiamGia;
import it.lab.enums.APIStatus;
import it.lab.enums.TrangThaiSuKienGiamGia;
import it.lab.iservice.ISuKienGiamGiaService;
import it.lab.modelcustom.request.SuKienGiamGiaReque;
import it.lab.repository.SuKienGiamGiaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SuKienGiamGiaService implements ISuKienGiamGiaService {
    @Autowired
    private SuKienGiamGiaRepo suKienGiamGiaRepo;

    @Override
    public Page<SuKienGiamGiaDTO> layHetSuKienGiamGia() {
        return new Page<SuKienGiamGiaDTO>(SuKienGiamGiaDTO.fromCollection(suKienGiamGiaRepo.getAll()), 0, 10000);
    }

    @Override
    public Page<SuKienGiamGiaDTO> xoaSuKienGiamGia(Long suKienGiamGiaId) {
        SuKienGiamGia suKienGiamGiaGoc = suKienGiamGiaRepo.findById(suKienGiamGiaId).orElse(null);
        assert suKienGiamGiaGoc != null;
        suKienGiamGiaGoc.setTrangThai(TrangThaiSuKienGiamGia.DANGUNG);
        suKienGiamGiaRepo.save(suKienGiamGiaGoc);
        return layHetSuKienGiamGia();
    }

    @Override
    public Page<SuKienGiamGiaDTO> suaSuKienGiamGia(SuKienGiamGia suKienGiamGia) {
        SuKienGiamGia suKienGiamGiaGoc = suKienGiamGiaRepo.findById(suKienGiamGia.getId()).get();
        suKienGiamGiaGoc.setTenSuKien(suKienGiamGia.getTenSuKien());
        suKienGiamGiaGoc.setNgayBatDau(suKienGiamGia.getNgayBatDau());
        suKienGiamGiaGoc.setNgayKetThuc(suKienGiamGia.getNgayKetThuc());
        suKienGiamGiaGoc.setMoTa(suKienGiamGia.getMoTa());
        suKienGiamGiaGoc.setLogoSuKien(suKienGiamGia.getLogoSuKien());
        suKienGiamGiaGoc.setTrangThai(suKienGiamGia.getTrangThai());
        suKienGiamGiaGoc.setNgayTao(suKienGiamGia.getNgayTao());
        suKienGiamGiaGoc.setNgayCapNhat(LocalDateTime.now());
        suKienGiamGiaRepo.save(suKienGiamGiaGoc);
        return layHetSuKienGiamGia();
    }

    @Override
    public Page<SuKienGiamGiaDTO> themSuKienGiamGia(SuKienGiamGia suKienGiamGia) {
        suKienGiamGia.setNgayTao(LocalDateTime.now());
        suKienGiamGiaRepo.save(suKienGiamGia);
        suKienGiamGiaRepo.save(suKienGiamGia);
        return layHetSuKienGiamGia();
    }

    @Override
    public ResponObject<String, APIStatus> themSuKien(SuKienGiamGiaReque suKienGiamGia, MultipartFile hinh) throws IOException {
        SuKienGiamGia suKienGiamGia1=new SuKienGiamGia();
        suKienGiamGia1.setTenSuKien(suKienGiamGia.getTenSuKien());
        suKienGiamGia1.setNgayTao(LocalDateTime.now());
        suKienGiamGia1.setNgayCapNhat(LocalDateTime.now());
        suKienGiamGia1.setNgayBatDau(suKienGiamGia.getNgayBatDau());
        suKienGiamGia1.setNgayKetThuc(suKienGiamGia.getNgayKetThuc());
        suKienGiamGia1.setLogoSuKien(CloudinaryUpload.uploadFile(hinh));
        suKienGiamGia1.setGiaTriGiam(suKienGiamGia.getGiaTriGiam());
        suKienGiamGia1.setMoTa(suKienGiamGia.getMoTa());
        if(suKienGiamGia.getNgayBatDau().isAfter(LocalDateTime.now())){
            suKienGiamGia1.setTrangThai(TrangThaiSuKienGiamGia.CHUADIENRA);
        }else {
            suKienGiamGia1.setTrangThai(TrangThaiSuKienGiamGia.HOATDONG);

        }
        suKienGiamGiaRepo.save(suKienGiamGia1);
        return new ResponObject<String, APIStatus>("Thành công", APIStatus.THANHCONG, "Thành công");

    }
    @Override
    public ResponObject<String, APIStatus> suaSuKien(Long id, SuKienGiamGiaReque suKienGiamGia, MultipartFile hinh) throws IOException {
        Optional<SuKienGiamGia> optionalSuKien = suKienGiamGiaRepo.findById(id);
        if (optionalSuKien.isPresent()) {
            SuKienGiamGia existingSuKien = optionalSuKien.get();
            existingSuKien.setTenSuKien(suKienGiamGia.getTenSuKien());
            existingSuKien.setNgayBatDau(suKienGiamGia.getNgayBatDau());
            existingSuKien.setNgayKetThuc(suKienGiamGia.getNgayKetThuc());
            existingSuKien.setNgayCapNhat(LocalDateTime.now());
            existingSuKien.setGiaTriGiam(suKienGiamGia.getGiaTriGiam());
            existingSuKien.setMoTa(suKienGiamGia.getMoTa());
            if(suKienGiamGia.getNgayBatDau().isAfter(LocalDateTime.now())){
                existingSuKien.setTrangThai(TrangThaiSuKienGiamGia.CHUADIENRA);
            }else {
                existingSuKien.setTrangThai(TrangThaiSuKienGiamGia.HOATDONG);

            }
            // Check if a new image is uploaded and update if necessary
            if (hinh != null) {
                existingSuKien.setLogoSuKien(CloudinaryUpload.uploadFile(hinh));
            }
            // Save the updated SuKienGiamGia
            suKienGiamGiaRepo.save(existingSuKien);
            return new ResponObject<String, APIStatus>("Cập nhật thành công", APIStatus.THANHCONG, "Thành công");
        } else {
            return new ResponObject<String, APIStatus>("Không tìm thấy sự kiện", APIStatus.THATBAI, "Lỗi");
        }
    }


    @Override
    public SuKienGiamGiaDTO laySuKienGiamGiaById(Long sukienGiamGiaId) {
        return SuKienGiamGiaDTO.fromEntity(suKienGiamGiaRepo.findById(sukienGiamGiaId).get());
    }

    @Override
    public SuKienGiamGia findById(Long suKienGiamGiaId) {
        return suKienGiamGiaRepo.findById(suKienGiamGiaId).orElse(null);
    }

    @Override
    public List<SuKienGiamGia> getAll() {
        return suKienGiamGiaRepo.getAll();
    }
}