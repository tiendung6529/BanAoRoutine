package it.lab.service;

import it.lab.common.ResponObject;
import it.lab.dto.NguoiDungDTO;
import it.lab.entity.NguoiDung;
import it.lab.entity.QuyenNguoiDung;
import it.lab.enums.APIStatus;
import it.lab.enums.TrangThaiNguoiDung;
import it.lab.iservice.IAuthService;
import it.lab.repository.NguoiDungRepo;
import it.lab.repository.QuyenNguoiDungRepo;
import it.lab.repository.QuyenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private NguoiDungRepo _nguNguoiDungRepo;
    @Autowired
    private QuyenRepo _quyenRepo;
    @Autowired
    private QuyenNguoiDungRepo _quyenNguoiDung;

    @Override
    public ResponObject<NguoiDungDTO, APIStatus> dangKyTaiKhoan(NguoiDung nguoiDung) {
        NguoiDung ng = new NguoiDung();
        ng.setMatKhau(new BCryptPasswordEncoder().encode(nguoiDung.getMatKhau()));
        ng.setEmail(nguoiDung.getEmail());
        ng.setNgayTao(LocalDateTime.now());
        ng.setTrangThai(TrangThaiNguoiDung.HOATDONG);
        _nguNguoiDungRepo.save(ng);
        QuyenNguoiDung qnd = new QuyenNguoiDung();
        qnd.setNgayTao(LocalDateTime.now());
        qnd.setNguoiDung(ng);
        qnd.setQuyen(_quyenRepo.findById(1l).get());
        _quyenNguoiDung.save(qnd);
        return new ResponObject<>(NguoiDungDTO.fromEntity(ng),APIStatus.THANHCONG, "Thêm thành công");
    }
}
