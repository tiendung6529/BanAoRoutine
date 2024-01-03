package it.lab.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.lab.common.ResponObject;
import it.lab.entity.ChatLieu;
import it.lab.entity.SuKienGiamGia;
import it.lab.enums.APIStatus;
import it.lab.modelcustom.request.NguoiDungRequest;
import it.lab.modelcustom.request.SuKienGiamGiaReque;
import it.lab.service.SuKienGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sukiengiamgia")
public class SuKienGiamGiaController {
    @Autowired
    SuKienGiamGiaService suKienGiamGiaService;

    @RequestMapping(value = "/laysukiengiamgia", method = RequestMethod.GET)
    public ResponseEntity<?> laySuKiengiamGia() {
        return ResponseEntity.ok(suKienGiamGiaService.layHetSuKienGiamGia());
    }

    @RequestMapping(value = "/themsukiengg", method = RequestMethod.POST)
    public ResponseEntity<?> themSuKien(@RequestPart(value = "logoSuKien" ,required = false) MultipartFile logoSuKien, @RequestPart("data") String suKienGiamGia) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();
        return ResponseEntity.ok(suKienGiamGiaService.themSuKien(gson.fromJson(suKienGiamGia, SuKienGiamGiaReque.class), logoSuKien));
    }

    @RequestMapping(value = "/themsukiengiamgia", method = RequestMethod.POST)
    public ResponseEntity<?> themSuKienGiamGia(@RequestBody SuKienGiamGia suKienGiamGia) {
        return ResponseEntity.ok(suKienGiamGiaService.themSuKienGiamGia(suKienGiamGia));
    }

    @RequestMapping(value = "/xoasukiengiamgia", method = RequestMethod.GET)
    public ResponseEntity<?> xoaSuKienGiamGia(@RequestParam Long suKienGiamGiaId) {
        return ResponseEntity.ok(suKienGiamGiaService.xoaSuKienGiamGia(suKienGiamGiaId));
    }

    @RequestMapping(value = "/suasukiengiamgia", method = RequestMethod.POST)
    public ResponseEntity<?> suaSuKienGiamGia(@RequestBody SuKienGiamGia suKienGiamGia) {
        return ResponseEntity.ok(suKienGiamGiaService.suaSuKienGiamGia(suKienGiamGia));
    }

    @RequestMapping(value = "/laysukiengiamgiabyid", method = RequestMethod.GET)
    public ResponseEntity<?> laySuKienGiamGiaById(@RequestParam Long sukienGiamGiaId) {
        return ResponseEntity.ok(suKienGiamGiaService.laySuKienGiamGiaById(sukienGiamGiaId));
    }

    @PutMapping("/capnhatsukiengg/{id}")
    public ResponseEntity<ResponObject<String, APIStatus>> capNhatSuKienGiamGia(
            @PathVariable Long id,
            @RequestBody SuKienGiamGiaReque suKienGiamGiaReque,
            @RequestParam(value = "hinh", required = false) MultipartFile hinh) {
        try {
            ResponObject<String, APIStatus> response = suKienGiamGiaService.suaSuKien(id, suKienGiamGiaReque, hinh);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Xử lý ngoại lệ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponObject<>("Lỗi khi cập nhật sự kiện giảm giá", APIStatus.THATBAI, "Lỗi"));
        }
    }

}