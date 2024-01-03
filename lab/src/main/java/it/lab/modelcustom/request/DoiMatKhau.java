package it.lab.modelcustom.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoiMatKhau {
    private Long nguoiDungId;
    private String matKhauCu;
    private String matKhauMoi;
}
