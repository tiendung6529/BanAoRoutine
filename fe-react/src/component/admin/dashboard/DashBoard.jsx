import React, { useEffect, useState, useCallback } from 'react';
import { Button, Col, DatePicker, Row, Space, Table, message } from 'antd';
import axios from 'axios';
import MenuAdmin from '../layout/menu/MenuAdmin';
import Header from '../layout/header/Header';
import ThongKeBar from './chart/ThongKeBar';
import BanhDonut from './chart/BanhDonut';
import BanhDonut2 from './chart/BanhDonut2';
import NgayThang from './chart/NgayThang';
import SLNhomAo from './chart/SLNhomAo';

import './style.css';

function DashBoard() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMonth, setSelectedMonth] = useState(null);
  const [selectedYear, setSelectedYear] = useState(null);
  const [dateRange, setDateRange] = useState([]);
  // const [totalKhoang, setTotalKhoang] = useState(null);
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 6,
  });

  const columns = [
    {
      title: 'ID',
      dataIndex: 'idCTSP',
      key: 'idCTSP',
    },
    {
      title: 'Tên Sản Phẩm',
      dataIndex: 'tenSanPham',
      key: 'tenSanPham',
    },
    {
      title: 'Hình Ảnh',
      dataIndex: 'image',
      render: (image) => (
        <img src={image} style={{ width: '120px', height: '180px' }} alt="Product" />
      ),
      key: 'image',
    },
    {
      title: 'Tổng Số Lượng',
      dataIndex: 'tongSoLuong',
      key: 'tongSoLuong',
    },
    {
      title: 'Giá Nhập',
      dataIndex: 'giaNhap',
      key: 'giaNhap',
    },
    {
      title: 'Giá Bán',
      dataIndex: 'giaBan',
      key: 'giaBan',
    },
  ];

  const fetchData = useCallback(async () => {
    try {
      const response = await axios.get(
        'http://localhost:8089/api/thong-ke/san-pham-ban-chay1',
        {
          params: {
            selectedMonth,
            selectedYear,
            page: pagination.current,
            pageSize: pagination.pageSize,
          },
        }
      );

      setData(
        response.data.map((item) => ({
          idCTSP: item[0],
          tenSanPham: item[1],
          image: item[2],
          tongSoLuong: item[3],
          giaNhap: item[4],
          giaBan: item[5],
        }))
      );

      setLoading(false);
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoading(false);
    }
  }, [pagination, selectedMonth, selectedYear]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleTableChange = (pagination, filters, sorter) => {
    setPagination(pagination);
  };

  const handleDateChange = (dates) => {
    setDateRange(dates);
  };

  const handleCalculateTotalRevenue = () => {
    if (!Array.isArray(dateRange) || dateRange.length < 2) {
      // Date range not selected, show error message or take appropriate action
      message.error('Vui lòng chọn khoảng thời gian');
      return;
    }

    const [selectedDateStart, selectedDateEnd] = dateRange;

    axios
      .get('http://localhost:8089/api/thong-ke/khoang-ban-chay-nhat', {
        params: {
          selectedDateStart: selectedDateStart.format('YYYY-MM-DD'),
          selectedDateEnd: selectedDateEnd.format('YYYY-MM-DD'),
        },
      })
      .then((response) => {
        const revenue = response.data;

        // Update the state used by the Table component
        setData(
          revenue.map((item) => ({
            idCTSP: item[0],
            tenSanPham: item[1],
            image: item[2],
            tongSoLuong: item[3],
            giaNhap: item[4],
            giaBan: item[5],
          }))
        );

        // You may also update other state or perform additional actions if needed

        // Optionally, you can show a success message
        message.success('Data loaded successfully!');
      })
      .catch((error) => {
        console.error('Error fetching total revenue:', error.response?.data || error.message);
        // Optionally, you can show an error message
        message.error('Error loading data. Please try again.');
      });
  };

  return (
    <>
      <div>
        <Header />
        <MenuAdmin />
        <div className="body-container">
          <div className="content">
            <Row style={{ backgroundColor: '#ffffff', padding: '12px 12px' }}>
              <ThongKeBar />
            </Row>
            <Row style={{ marginTop: '12px' }}>
              <div style={{ width: '49%', backgroundColor: '#ffffff' }}>
                <BanhDonut />
              </div>
              <div style={{ width: '49%', marginLeft: '2%', backgroundColor: '#ffffff' }}>
                <BanhDonut2 />
              </div>
            </Row>

            <div style={{ marginTop: '12px', width: '100%', backgroundColor: '#ffffff', padding: '12px 12px' }}>
              <NgayThang />

              <Row style={{ marginBottom: '10px' }}>
                <Col span={12}></Col>
                <Col span={2}></Col>
                <Col span={10}>
                  <Space style={{ width: '100%', display: 'flex', justifyContent: 'flex-end' }} direction="horizontal">
                    <DatePicker
                      picker="month"
                      onChange={(date, dateString) => {
                        const selectedDate = new Date(dateString);
                        setSelectedMonth(selectedDate.getMonth() + 1);
                        setSelectedYear(selectedDate.getFullYear());
                      }}
                    />
                    <Button onClick={fetchData}>Tìm kiếm</Button>
                  </Space>
                </Col>
              </Row>
              <Row style={{ marginBottom: '10px' }}>
                <Col span={12}>
                  <Space>
                    <DatePicker.RangePicker value={dateRange} onChange={handleDateChange} />
                    <Button type="primary" onClick={handleCalculateTotalRevenue}>
                      Khoảng sản phẩm bán chạy
                    </Button>
                  </Space>
                </Col>
                <Col span={2}></Col>
                <Col span={10}>

                </Col>
              </Row>
              <div>
                <div>
                  <div style={{ marginTop: '12px', width: '100%', backgroundColor: '#ffffff', padding: '12px 12px' }}>
                    <Table
                      pagination={{
                        ...pagination,
                        position: ['bottomCenter'],
                        showSizeChanger: true,
                        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`,
                        pageSizeOptions: [6, 12, 18],
                      }}
                      columns={columns}
                      dataSource={data}
                      loading={loading}
                      onChange={handleTableChange}
                    />
                  </div>
                </div>
              </div>

              <SLNhomAo />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default DashBoard;
