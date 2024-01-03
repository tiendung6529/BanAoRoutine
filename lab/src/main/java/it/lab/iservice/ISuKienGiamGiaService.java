package it.lab.iservice;

import it.lab.common.Page;
import it.lab.common.ResponObject;
import it.lab.dto.NhomSanPhamDTO;
import it.lab.dto.SuKienGiamGiaDTO;
import it.lab.entity.ChatLieu;
import it.lab.entity.SuKienGiamGia;
import it.lab.enums.APIStatus;
import it.lab.modelcustom.request.SanPhamRequest;
import it.lab.modelcustom.request.SuKienGiamGiaReque;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ISuKienGiamGiaService {
    public Page<SuKienGiamGiaDTO> layHetSuKienGiamGia();

    public Page<SuKienGiamGiaDTO> xoaSuKienGiamGia(Long suKienGiamGiaId);

    public Page<SuKienGiamGiaDTO> suaSuKienGiamGia(SuKienGiamGia suKienGiamGia);

    public Page<SuKienGiamGiaDTO> themSuKienGiamGia(SuKienGiamGia suKienGiamGia);
    public ResponObject<String, APIStatus> themSuKien(SuKienGiamGiaReque suKienGiamGia, MultipartFile hinh1) throws IOException;
    public SuKienGiamGiaDTO laySuKienGiamGiaById(Long suKienGiamGiaId);
    public SuKienGiamGia findById(Long suKienGiamGiaId);
    public List<SuKienGiamGia> getAll();
    public ResponObject<String, APIStatus> suaSuKien(Long id,SuKienGiamGiaReque suKienGiamGia, MultipartFile hinh) throws IOException;
}