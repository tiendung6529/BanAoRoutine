import { useDispatch, useSelector } from "react-redux";
import "./style.css";
import React, { useEffect, useState } from "react";
import {
  Button,
  Col,
  Image,
  Input,
  InputNumber,
  Modal,
  Row,
  Select,
  Space,
  Table,
  Tag,
  Tooltip,
  notification,
} from "antd";
import { RiRefundLine } from "react-icons/ri";
import TextArea from "antd/es/input/TextArea";
import { useDoiTra } from "./useDoiTra";
import { fixMoney } from "../../../extensions/fixMoney";
import DoiSanPham from "./DoiSanPham";

function YeuCauDoiTra({ hoaDonId, setData2 }) {
  const [api, contextHolder] = notification.useNotification();
  const [selectedChiTietHoaDon, setSelectedChiTietHoaDon] = useState(undefined);
  const [dataThayDoi, setDataThayDoi] = useState([]);
  const [sanPhamDoi, setSanPhamDoi] = useState([]);
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
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [ghiChu, setGhiChu] = useState([]);
  const [soLuong, setSoLuong] = useState([]);
  const [selectTrangThai, setSelectTrangThai] = useState([]);
  const [selectHinhThuc, setSelectHinhThuc] = useState([]);
  const columns = [
    {
      title: "Sản phẩm",
      dataIndex: "sanPhamChiTiet",
      render: (sanPhamChiTiet) => (
        <>
          <div
            style={{
              display: "flex",
            }}
          >
            <Image
              src={sanPhamChiTiet.hinhAnh}
              style={{
                width: "80px",
                height: "120px",
              }}
            />
            <span
              style={{
                marginLeft: "4px",
              }}
            >
              {sanPhamChiTiet.tenSanPham}
            </span>
          </div>
        </>
      ),
      width: "25%",
    },
    {
      title: "Số lượng",
      dataIndex: "soLuong",
      width: "15%",
      render: (soLuong2, record, number) => (
        <>
          <InputNumber
            min={1}
            max={soLuong2}
            onChange={(e) => {
              soLuong[number] = e;
              setSoLuong([...soLuong]);
            }}
          />
          /{soLuong2}
        </>
      ),
    },
    {
      title: "Đơn giá",
      dataIndex: "donGia",
      width: "10%",
      render: (donGia) => <a>{fixMoney(donGia)}</a>,
    },
    {
      title: "Tổng tiền",
      dataIndex: "donGia",
      width: "10%",
      render: (donGia, record) => <a>{fixMoney(donGia * record.soLuong)}</a>,
    },
    {
      title: "Ghi chú",
      dataIndex: "ghiChu",
      render: (_, record, number) => (
        <TextArea
          placeholder="Ghi chú"
          rows={4}
          onChange={(e) => {
            ghiChu[number] = e.target.value;
            setGhiChu([...ghiChu]);
          }}
        />
      ),
      width: "20%",
    },
    {
      title: "Hiện trạng sản phẩm",
      dataIndex: "hienTrangSanPham",
      render: (ghiChu, record, number) =>
        Array(soLuong[number])
          .fill()
          .map((item, index) => {
            return (
              <>
                <Select
                  defaultValue="Hiện trạng sản phẩm"
                  style={{
                    width: "100%",
                  }}
                  onChange={(e) => {
                    if (!selectTrangThai[number]) {
                      selectTrangThai[number] = [];
                    }
                    if (e === 1) {
                      selectTrangThai[number][index] = true;
                      setSelectTrangThai([...selectTrangThai]);
                    } else {
                      selectTrangThai[number][index] = false;
                      setSelectTrangThai([...selectTrangThai]);
                    }
                  }}
                  options={[
                    { value: 1, label: "Sản phẩm lỗi" },
                    { value: 2, label: "Sản phẩm đổi trả" },
                  ]}
                />
              </>
            );
          }),
      width: "10%",
    },
    {
      title: "Hình thức đổi trả",
      dataIndex: "action",
      render: (ghiChu, record, number) => (
        <>
          <Select
            defaultValue="Hình thức đổi trả"
            style={{
              width: "100%",
            }}
            onChange={(e) => { }}
            options={[
              {
                value: "1",
                label: "Đổi sản phẩm",
              },
              {
                value: "2",
                label: "Hoàn tiền",
              },
            ]}
          />
          <DoiSanPham
            dataDoi={sanPhamDoi}
            setSanPhamDoi={setSanPhamDoi}
            number={number}
          />
        </>
      ),
      width: "10%",
    },
  ];
  const [data, setData] = useState(undefined);
  async function handleLayChiTiet() {
    const data = await useDoiTra.actions.layChiTiet(hoaDonId);
    setData(data.data);
  }
  useEffect(() => {
    handleLayChiTiet();
  }, []);
  const rowSelection = {
    onChange: (selectedRowKeys) => {
      setSelectedChiTietHoaDon(selectedRowKeys);
    },
    selectedChiTietHoaDon,
  };
  async function handleDoiTra() {
    if (!selectedChiTietHoaDon) {
      openNotification(
        "error",
        "Hệ thống",
        "Vui lòng chọn 1 sản phẩm",
        "bottomRight"
      );
      return;
    }
    if (selectedChiTietHoaDon.length === 0) {
      openNotification(
        "error",
        "Hệ thống",
        "Vui lòng chọn 1 sản phẩm",
        "bottomRight"
      );
      return;
    }

    var duLieuDoiTra = [];
    for (var item of selectedChiTietHoaDon) {
      if (!ghiChu[item] || ghiChu[item] === "") {
        openNotification(
          "error",
          "Hệ thống",
          "Vui lòng nhập ghi chú cho lựa chọn thứ " + (Number(item) + 1),
          "bottomRight"
        );
        return;
      }
      if (!soLuong[item]) {
        openNotification(
          "error",
          "Hệ thống",
          "Vui lòng nhập số lượng",
          "bottomRight"
        );
        return;
      }
      if (!selectTrangThai[item]) {
        openNotification(
          "error",
          "Hệ thống",
          "Vui lòng chọn hiện trạng sản phẩm",
          "bottomRight"
        );
        return;
      }

      var loi = 0;
      var tra = 0;

      for (var item2 of selectTrangThai[item]) {
        if (item2) {
          loi++;
        } else {
          tra++;
        }
      }
      duLieuDoiTra[item] = {
        chiTietId: data[item].id,
        duLieuMoi: sanPhamDoi[item],
        ghiChu: ghiChu[item],
        soLuong: soLuong[item],
        soLuongLoi: loi,
        soLuongDoiTra: tra,
      };
    }
    var tienDoi = data.reduce((pre, next) => {
      return pre + (next.soLuong + next.donGia);
    }, 0);

    setDataDoiTra(duLieuDoiTra);
    setConfirmModal(true);
  }
  const [dataDoiTra, setDataDoiTra] = useState(undefined)
  const [confirmModal, setConfirmModal] = useState(false);
  async function handleTaoYeuCau() {
    const data = await useDoiTra.actions.taoYeuCau(dataDoiTra)
    if (data.data) {
      openNotification(
        "success",
        "Hệ thống",
        "Đổi trả thành công",
        "bottomRight"
      );
      setData2()
    } else {
      openNotification(
        "error",
        "Hệ thống",
        "Đổi trả thất bại",
        "bottomRight"
      );
    }
    setIsModalOpen(false)
  }

  return (
    <>
      {contextHolder}
      <Tooltip title="Đổi trả">
        <Button
          style={{
            color: "blue",
            marginLeft: "4px",
          }}
          shape="circle"
          icon={<RiRefundLine />}
          onClick={() => {
            setIsModalOpen(true);
          }}
        />
      </Tooltip>
      <Modal
        width={"100vw"}
        open={isModalOpen}
        onOk={handleDoiTra}
        onCancel={() => {
          setIsModalOpen(false);
        }}
      >
        <Row>
          <h6>Thông tin sản phẩm</h6>
        </Row>
        <Row
          style={{
            marginTop: "24px",
          }}
        >
          <Col span={24}>
            <Table
              rowSelection={{
                type: "checkbox",
                ...rowSelection,
              }}
              columns={columns}
              dataSource={
                data &&
                data.map((item, index) => {
                  return {
                    ...item,
                    key: index,
                  };
                })
              }
            />
          </Col>
        </Row>
      </Modal>
      <Modal
        title="Đổi trả sản phẩm?"
        open={confirmModal}
        onOk={() => {
          console.log(dataDoiTra);
          handleTaoYeuCau();
          setConfirmModal(false);
        }}
        onCancel={() => {
          setConfirmModal(false);
        }}
        centered
      >
        <p>Xác nhận đổi trả</p>
      </Modal>
    </>
  );
}

export default YeuCauDoiTra;
