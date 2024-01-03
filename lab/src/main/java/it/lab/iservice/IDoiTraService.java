package it.lab.iservice;

import it.lab.entity.HoaDon;
import it.lab.entity.HoaDonChiTiet;
import it.lab.modelcustom.request.ChiTietDoiTra;

import java.util.List;

public interface IDoiTraService {
    public List<HoaDonChiTiet> layHoaDonChiTietCuaHoaDon(Long hoaDonId);

    public Boolean doiTra(List<ChiTietDoiTra> data);

    public Boolean huyDoiTra(Long hoaDonId);
}
