import { useState, useEffect } from "react";
import {
  Button,
  Form,
  Input,
  Modal,
  Tooltip,
  notification,
  Checkbox,
  Radio,
  Select,
  Upload,
  message,
  InputNumber,
} from "antd";
import { useForm } from "antd/es/form/Form";
import { FaRegPenToSquare } from "react-icons/fa6";
import { useSanPhamStore } from "./useSanPhamStore";
import { PlusOutlined } from "@ant-design/icons";

function ModalSua({ id,thuocTinh, setData }) {
  const [form] = useForm()
  const { Option } = Select;
  const [sanPham, setSanPham] = useState(undefined);
  const trangThaiOptions = [
    { value: 'DANGBAN', label: 'Đang bán' },
    { value: 'HETHANG', label: 'Hết hàng' },
    { value: 'NGUNGBAN', label: 'Ngừng bán' },
    { value: 'CHAYSUKIEN', label: 'Chạy sự kiện' },
  ];
  function handleSetTenSP(e) {
    setSanPham({
      ...sanPham,
      tenSanPham: e.target.value,
    });
  }
  function handleSetGiaBan(e) {
    setSanPham({
      ...sanPham,
      giaBan: e,
    });
  }
  function handleSetGiaNhap(e) {
    setSanPham({
      ...sanPham,
      giaNhap: e,
    });
  }
  function handleSetMoTa(e) {
    setSanPham({
      ...sanPham,
      moTa: e.target.value,
    });
  }
  function handleSetTrangThai(selectedOption) {
    setSanPham(prevSanPham => ({
      ...prevSanPham,
      trangThai: selectedOption.value
    }));
  }
  // function handleSetThietKe(selectedOption) {
  //   setThietKeSelected(selectedOption);
  //   setSanPham((prevSanPham) => ({
  //     ...prevSanPham,
  //     thietKeId: selectedOption.value, // Chỉ cần dùng thietKeId
  //   }));
  // }

  function handleSetThietKe(e) {
    setSanPham({
      ...sanPham,
      thietKeId: e.value,
    });
  }
  
  
  
  function handleSetNhom(e) {
    setSanPham({
      ...sanPham,
      nhomSanPhamId: e.value,
    });
  }
  function handleSetChatLieu(e) {
    setSanPham({
      ...sanPham,
      chatLieuId: e.value,
    });
  }
  const [fileList, setFileList] = useState([]);
  const [hinhAnh, setHinhAnh] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const props = {
    beforeUpload: (file) => {
      return false;
    },
    onChange: (file) => {
      setFileList(file.fileList);
      if (file.fileList.length == 0) {
        return;
      }
      const isPNG =
        file.file.type === "image/png" ||
        file.file.type === "image/jpg" ||
        file.file.type === "image/jpeg";
      if (!isPNG) {
        message.error(`${file.file.name} không phải file hình ảnh`);
        return;
      }
      if (hinhAnh.length > 1) {
        message.error(`Đã đủ 2 hình ảnh`);
      }
      if (file.fileList[1]) {
        setHinhAnh([
          file.fileList[0].originFileObj,
          file.fileList[1].originFileObj,
        ]);
      } else {
        setHinhAnh([file.fileList[0].originFileObj]);
      }
    },
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const [api, contextHolder] = notification.useNotification();

  const openNotification = (type, title, des, placement) => {
    if (type === "error") {
      api.error({
        message: title,
        description: des,
        placement,
      });
    } else {
      api.success({
        message: title,
        description: des,
        placement,
      });
    }
  };
  async function handleSuaSanPham() {
    if (sanPham.tenSanPham == "") {
      openNotification(
        "error",
        "Hệ thống",
        "Vui lòng nhập tên sản phẩm",
        "bottomRight"
      );
      return;
    }
    if (sanPham.giaNhap <= 0) {
      openNotification(
        "error",
        "Hệ thống",
        "Vui lòng nhập giá nhập",
        "bottomRight"
      );
      return;
    }
    if (sanPham.giaBan <= 0) {
      openNotification(
        "error",
        "Hệ thống",
        "Vui lòng nhập giá bán",
        "bottomRight"
      );
      return;
    }
    if (!sanPham.thietKeId) {
      openNotification("error", "Hệ thống", "Thiếu thiết kế", "bottomRight");
      return;
    }
    if (!sanPham.chatLieuId) {
      openNotification("error", "Hệ thống", "Thiếu chất liệu", "bottomRight");
      return;
    }
    if (!sanPham.nhomSanPhamId) {
      openNotification(
        "error",
        "Hệ thống",
        "Thiếu nhóm sản phẩm",
        "bottomRight"
      );
      return;
    }
    if (!hinhAnh) {
      openNotification(
        "error",
        "Hệ thống",
        "Cần 2 hình ảnh",
        "bottomRight"
      );
      return;
    }
    if (hinhAnh && hinhAnh.length< 2) {
      openNotification(
        "error",
        "Hệ thống",
        "Cần 2 hình ảnh",
        "bottomRight"
      );
      return;
    }

    setIsLoading(true);
    var form2 = new FormData();
    form2.append("file1", hinhAnh[0]);
    form2.append("file2", hinhAnh[1]);
    form2.append("data", JSON.stringify(sanPham));
    

    const data = await useSanPhamStore.actions.suaSanPham(form2);
    console.log("Toàn bộ dữ liệu sản phẩm sẽ được gửi đi:", form2);
    if (data.data.status == "THANHCONG") {
      form.resetFields();
      openNotification(
        "success",
        "Hệ thống",
        "Sửa sản phẩm thành công",
        "bottomRight"
      );
      setSanPham({
       ...sanPham
      });
    } else {
      openNotification(
        "error",
        "Hệ thống",
        "Sửa sản phẩm thất bại",
        "bottomRight"
      );
      setSanPham({
        tenSanPham: "",
        giaBan: 0,
        giaNhap: 0,
        soLuong: 0,
      });
    }
    setData(data.data.data);
    setFileList([]);
    setIsModalOpen(false);
    setIsLoading(false);
  }
  useEffect(() => {
    async function layDuLieu() {
      const data = await useSanPhamStore.actions.laySanPhamById(id);
      console.log("Lấy dữ liệu: ", data);
      setSanPham({
        ...data.data,
        thietKeId: data.data.thietKe.id,
        nhomSanPhamId: data.data.nhomSanPham.id,
        chatLieuId: data.data.chatLieu.id,
        id: data.data.id,
      });
      setFileList([
        { uid: '1', url: data.data.hinhAnh1, name: 'Hình ảnh 1' },
        { uid: '2', url: data.data.hinhAnh2, name: 'Hình ảnh 2' }
      ]);
      // if(data.data.thietKe && data.data.thietKe.id) {
      //   setThietKeSelected({ value: data.data.thietKe.id, label: data.data.thietKe.tenThietKe });
      // }
    }
  
    if (isModalOpen) {
      layDuLieu();
    }
  }, [id, isModalOpen]);
  

  return (
    <>
      {contextHolder}
      <Tooltip title="Cập nhật" onClick={showModal}>
        <Button
          style={{
            color: "green",
          }}
          shape="circle"
          icon={<FaRegPenToSquare />}
        />
      </Tooltip>
      <Modal
        okButtonProps={{ style: { display: "none" } }}
        cancelButtonProps={{ style: { display: "none" } }}
        title="Sửa sản phẩm"
        visible={isModalOpen}
        onCancel={handleCancel}
        centered
        footer={[
          <Button key="submit" type="primary" onClick={handleSuaSanPham}>
            Sửa
          </Button>,
        ]}
      >
         {sanPham ? (
        <Form
          form={form}
          name="wrap"
          labelCol={{
            flex: "110px",
          }}
          labelAlign="left"
          labelWrap
          wrapperCol={{
            flex: 1,
          }}
          colon={false}
          style={{
            maxWidth: 600,
          }}
        >

          <Form.Item label="Tên sản phẩm">
            <Input value={sanPham.tenSanPham} onChange={handleSetTenSP} />
          </Form.Item>
          <Form.Item label="Giá nhập">
            <InputNumber
              formatter={(value) => ` ${value}đ`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
              parser={(value) => value.replace(/\đ\s?|(,*)/g, '')}
              style={{
                width: "100%",
              }}
              min={0}
              value={sanPham.giaNhap}
              rules={[
                {
                  required: true,
                },
              ]}
              onChange={handleSetGiaNhap}
            />
          </Form.Item>
          <Form.Item label="Giá bán">
            <InputNumber
              formatter={(value) => ` ${value}đ`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
              parser={(value) => value.replace(/\đ\s?|(,*)/g, '')}
              style={{
                width: "100%",
              }}
              value={sanPham.giaBan}
              min={0}
              rules={[
                {
                  required: true,
                },
              ]}
              onChange={handleSetGiaBan}
            />
          </Form.Item>
          <Form.Item label="Thông tin chi tiết">
            <Input value={sanPham.moTa} onChange={handleSetMoTa} />
          </Form.Item>
          <Form.Item label="Trạng thái sản phẩm">
          <Select
          labelInValue
          value={sanPham.trangThai ? { value: sanPham.trangThai } : undefined} 
          style={{ width: "100%" }}
          onChange={(selectedOption) => handleSetTrangThai(selectedOption)}
        >
          {trangThaiOptions.map((option) => (
            <Select.Option key={option.value} value={option.value}>
              {option.label}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
          <Form.Item label="Thiết kế">
            <Select
              labelInValue
              optionLabelProp="children"
              style={{ width: "100%" }}
              rules={[{ required: true }]}
              defaultValue={sanPham.thietKe.tenThietKe}
              onChange={(e) => {
                setSanPham({
                  ...sanPham,
                  thietKeId: e.value,
                });
              }}
            >
              {thuocTinh?.thietKeList.map((option) => (
                <Select.Option key={option.id} value={option.id}>
                  {option.tenThietKe}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item label="Chất liệu">
            <Select
              labelInValue
              optionLabelProp="children"
              defaultValue={sanPham.chatLieu.tenChatLieu}
              style={{
                width: "100%",
              }}
              rules={[
                {
                  required: true,
                },
              ]}
              onChange={handleSetChatLieu}
            >
              {thuocTinh
                ? thuocTinh.chatLieuList.map((option) => (
                  <Select.Option key={option.id} value={option.id}>
                    {option.tenChatLieu}
                  </Select.Option>
                ))
                : ""}
            </Select>
          </Form.Item>
          <Form.Item label="Nhóm sản phẩm">
            <Select
              labelInValue
              optionLabelProp="children"
              defaultValue={sanPham.nhomSanPham.tenNhom}
              style={{
                width: "100%",
              }}
              rules={[
                {
                  required: true,
                },
              ]}
              onChange={handleSetNhom}
            >
              {thuocTinh
                ? thuocTinh.nhomSanPhamList.map((option) => (
                  <Select.Option key={option.id} value={option.id}>
                    {option.tenNhom}
                  </Select.Option>
                ))
                : ""}
            </Select>
          </Form.Item>
          <Form.Item label="Upload">
            <Upload
              listType="picture-card"
              multiple
              customRequest={() => { }}
              {...props}
              maxCount={2}
              fileList={fileList}
            >
              <div>
                <PlusOutlined />
                <div
                  style={{
                    marginTop: 8,
                  }}
                >
                  Upload
                </div>
              </div>
            </Upload>
          </Form.Item>

        </Form>
        ) : (
          ""
        )}
      </Modal>
    </>
  );
}

export default ModalSua;