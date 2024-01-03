package it.lab.controller;

import it.lab.iservice.IDoiTraService;
import it.lab.modelcustom.request.ChiTietDoiTra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class DoiTraController {
    @Autowired
    private IDoiTraService _doiTraService;

    @RequestMapping(value = "/laydanhsachchitietdoitracuahoadon", method = RequestMethod.GET)
    public ResponseEntity<?> layChiTiet(Long hoaDonId) {
        return ResponseEntity.ok(_doiTraService.layHoaDonChiTietCuaHoaDon(hoaDonId));
    }

    @RequestMapping(value = "/taoyeucau", method = RequestMethod.POST)
    public ResponseEntity<?> taoYeuCau(@RequestBody List<ChiTietDoiTra> data) {
        return ResponseEntity.ok(_doiTraService.doiTra(data));
    }

    @RequestMapping(value = "/huydoitra", method = RequestMethod.GET)
    public ResponseEntity<?> taoYeuCau(@RequestParam Long hoaDonId) {
        return ResponseEntity.ok(_doiTraService.huyDoiTra(hoaDonId));
    }
}
