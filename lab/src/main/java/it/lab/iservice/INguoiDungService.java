package it.lab.iservice;

import it.lab.common.Page;
import it.lab.common.ResponObject;
import it.lab.dto.NguoiDungDTO;
import it.lab.entity.NguoiDung;
import it.lab.enums.APIStatus;
import it.lab.enums.CapNhat;
import it.lab.modelcustom.request.DoiMatKhau;
import it.lab.modelcustom.request.NguoiDungRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface INguoiDungService {
    public Page<NguoiDungDTO> layHetNguoiDung();
    public NguoiDungDTO layThongTinTaiKhoanById(Long id);
    public ResponObject<String, APIStatus> capNhatNguoiDung(NguoiDungRequest nguoiDungRequest,MultipartFile anhdaidien) throws IOException;
    public ResponObject<NguoiDungDTO, CapNhat> doiMatKhau(DoiMatKhau matKhau);
    public Page<NguoiDungDTO> xoaNguoiDung(Long nguoiDungId);
    public ResponObject<String, APIStatus> themNguoiDung(NguoiDungRequest nguoiDung, MultipartFile anhdaidien)throws IOException;
}
