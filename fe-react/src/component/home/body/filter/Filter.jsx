import { useSelector } from "react-redux";
import "./style.css";
import { selectLanguage } from "../../../../language/selectLanguage";
import { FiFilter } from "react-icons/fi";
import {
  Accordion,
  AccordionButton,
  AccordionIcon,
  AccordionItem,
  AccordionPanel,
  Box,
  Stack,
} from "@chakra-ui/react";
import { Col, InputNumber, Radio, Row, Slider, Space, Tag } from "antd";
import { Checkbox as ckr } from "antd";
import { useEffect, useState } from "react";
import { fixMoney } from "../../../../extensions/fixMoney";
import Tag1 from "../../../common/tag/Tag1";
import { useFilterStore } from "./useFilter";
function Filter({ handleFilter, page, pageSize }) {
  const language = useSelector(selectLanguage);
  const [value, setValue] = useState(undefined);
  const [thuocTinh, setThuocTinh] = useState(undefined);
  async function layThuocTinh() {
    const data = await useFilterStore.actions.layThuocTinh();
    setThuocTinh(data.data);
  }

  const [checkedList1, setCheckedList1] = useState([]);
  const [checkedList2, setCheckedList2] = useState([]);
  const [checkedList3, setCheckedList3] = useState([]);
  const [checkedList4, setCheckedList4] = useState([]);
  const [filterMauSac, setFilterMauSac] = useState([]);
  const [filterNhomSanPham, setFilterNhomSanPham] = useState([]);
  const [filterKichThuoc, setFilterKichThuoc] = useState([]);
  const [filterChatLieu, setFilterChatLieu] = useState([]);
  useEffect(() => {
    layThuocTinh();
  }, []);

  const onChange = (e) => {
    setValue(e.target.value);
  };
  const formatter = (value) => {
    return fixMoney(value);
  };
  const [inputValue, setInputValue] = useState(1);
  const [inputValueSlider, setInputValueSlider] = useState(1);
  const onChangeSlider = (newValue) => {
    setInputValue(newValue);
  };
  const [option, setOption] = useState(undefined);
  return (
    <>
      <div className="filter-container">
        <div className="title">
          <span>{language.body.filter.title}</span>
          <div>
            <FiFilter />
          </div>
        </div>
        <div
          className="filter-item"
          style={{
            width: "100%",
          }}
        >
          <div
            style={{
              marginBottom: "8px",
              width: "100%",
            }}
          >
            {filterMauSac.map((item) => {
              return (
                <Tag className="tag" color={"pink"}>
                  {item.ten}
                </Tag>
              );
            })}
            {filterChatLieu.map((item) => {
              return (
                <Tag className="tag" color={"blue"}>
                  {item.ten}
                </Tag>
              );
            })}
            {filterKichThuoc.map((item) => {
              return (
                <Tag className="tag" color={"red"}>
                  {item.ten}
                </Tag>
              );
            })}
            {filterNhomSanPham.map((item) => {
              return (
                <Tag className="tag" color={"green"}>
                  {item.ten}
                </Tag>
              );
            })}
            <Tag
              className="tag"
              color="purple"
              onClick={() => {
                setFilterChatLieu([]);
                setFilterMauSac([]);
                setFilterKichThuoc([]);
                setFilterNhomSanPham([]);
                setCheckedList1([]);
                setCheckedList2([]);
                setCheckedList3([]);
                setCheckedList4([]);
                handleFilter({
                  page: page,
                  pageSize: pageSize,
                  filter: {
                    mauSac: [],
                    chatLieu: [],
                    nhomSanPham: [],
                    kichThuoc: []
                  }
                })
              }}
            >
              Xóa tất cả
            </Tag>
          </div>
          {thuocTinh && (
            <Accordion allowMultiple allowToggle>
              <AccordionItem>
                <h2>
                  <AccordionButton>
                    <Box as="span" flex="1" textAlign="left">
                      {language.body.filter.item.type.title}
                    </Box>
                    <AccordionIcon />
                  </AccordionButton>
                </h2>
                <AccordionPanel pb={4}>
                  <div
                    style={{
                      marginTop: "8px",
                      marginLeft: "8px",
                      width: "90%",
                    }}
                  >
                    <ckr.Group
                      value={checkedList1}
                      options={thuocTinh.nhomSanPhamList.map((item) => {
                        return {
                          label: item.tenNhom,
                          value: item.id,
                        };
                      })}
                      onChange={(e) => {
                        setCheckedList1(e);
                        setOption({
                          ...option,
                          nhomSanPham: e,
                        });
                        filterNhomSanPham.splice(0, filterNhomSanPham.length);
                        for (var item2 of e) {
                          filterNhomSanPham.push({
                            id: thuocTinh.nhomSanPhamList.find((item) => {
                              return item.id === item2;
                            })?.id,
                            ten: thuocTinh.nhomSanPhamList.find((item) => {
                              return item.id === item2;
                            })?.tenNhom,
                          });
                        }
                        setFilterNhomSanPham(filterNhomSanPham);
                        handleFilter({
                          page: page,
                          pageSize: pageSize,
                          filter: {
                            mauSac: filterMauSac.map((item) => {
                              return item.id
                            }),
                            chatLieu: filterChatLieu.map((item) => {
                              return item.id
                            }),
                            nhomSanPham: e,
                            kichThuoc: filterKichThuoc.map((item) => {
                              return item.id
                            })
                          }
                        })
                      }}
                    />
                  </div>
                </AccordionPanel>
              </AccordionItem>
              <AccordionItem>
                <h2>
                  <AccordionButton>
                    <Box as="span" flex="1" textAlign="left">
                      Chất liệu
                    </Box>
                    <AccordionIcon />
                  </AccordionButton>
                </h2>
                <AccordionPanel pb={4}>
                  <div
                    style={{
                      marginTop: "8px",
                      marginLeft: "8px",
                      width: "90%",
                    }}
                  >
                    <ckr.Group
                      value={checkedList2}
                      options={thuocTinh.chatLieuList.map((item) => {
                        return {
                          label: item.tenChatLieu,
                          value: item.id,
                        };
                      })}
                      onChange={(e) => {
                        setCheckedList2(e);
                        setOption({
                          ...option,
                          chatLieu: e,
                        });
                        filterChatLieu.splice(0, filterChatLieu.length);
                        for (var item2 of e) {
                          filterChatLieu.push({
                            id: thuocTinh.chatLieuList.find((item) => {
                              return item.id === item2;
                            })?.id,
                            ten: thuocTinh.chatLieuList.find((item) => {
                              return item.id === item2;
                            })?.tenChatLieu,
                          });
                        }
                        setFilterChatLieu(filterChatLieu);
                        handleFilter({
                          page: page,
                          pageSize: pageSize,
                          filter: {
                            mauSac: filterMauSac.map((item) => {
                              return item.id
                            }),
                            chatLieu: e,
                            nhomSanPham: filterNhomSanPham.map((item) => {
                              return item.id
                            }),
                            kichThuoc: filterKichThuoc.map((item) => {
                              return item.id
                            })
                          }
                        })
                      }}
                    />
                  </div>
                </AccordionPanel>
              </AccordionItem>
              <AccordionItem>
                <h2>
                  <AccordionButton>
                    <Box as="span" flex="1" textAlign="left">
                      {language.body.filter.item.size.title}
                    </Box>
                    <AccordionIcon />
                  </AccordionButton>
                </h2>
                <AccordionPanel pb={4}>
                  <div
                    style={{
                      marginTop: "8px",
                      marginLeft: "8px",
                      width: "90%",
                    }}
                  >
                    <ckr.Group
                      value={checkedList3}
                      options={thuocTinh.kichThuocList.map((item) => {
                        return {
                          label: item.tenKichThuoc,
                          value: item.id,
                        };
                      })}
                      onChange={(e) => {
                        setCheckedList3(e);
                        setOption({
                          ...option,
                          kichThuoc: e,
                        });
                        filterKichThuoc.splice(0, filterKichThuoc.length);
                        for (var item2 of e) {
                          filterKichThuoc.push({
                            id: thuocTinh.kichThuocList.find((item) => {
                              return item.id === item2;
                            })?.id,
                            ten: thuocTinh.kichThuocList.find((item) => {
                              return item.id === item2;
                            })?.tenKichThuoc,
                          });
                        }
                        setFilterKichThuoc(filterKichThuoc);
                        handleFilter({
                          page: page,
                          pageSize: pageSize,
                          filter: {
                            mauSac: filterMauSac.map((item) => {
                              return item.id
                            }),
                            chatLieu: filterChatLieu.map((item) => {
                              return item.id
                            }),
                            nhomSanPham: filterNhomSanPham.map((item) => {
                              return item.id
                            }),
                            kichThuoc: e
                          }
                        })
                      }}
                    />
                  </div>
                </AccordionPanel>
              </AccordionItem>
              <AccordionItem>
                <h2>
                  <AccordionButton>
                    <Box as="span" flex="1" textAlign="left">
                      {language.body.filter.item.color.title}
                    </Box>
                    <AccordionIcon />
                  </AccordionButton>
                </h2>
                <AccordionPanel pb={4}>
                  <div
                    style={{
                      marginTop: "8px",
                      marginLeft: "8px",
                      width: "90%",
                    }}
                  >
                    <ckr.Group
                      value={checkedList4}
                      options={thuocTinh.mauSacList.map((item) => {
                        return {
                          label: item.tenMau,
                          value: item.id,
                        };
                      })}
                      onChange={(e) => {
                        setCheckedList4(e);
                        setOption({
                          ...option,
                          mauSac: e,
                        });
                        filterMauSac.splice(0, filterMauSac.length);
                        for (var item2 of e) {
                          filterMauSac.push({
                            id: thuocTinh.mauSacList.find((item) => {
                              return item.id === item2;
                            })?.id,
                            ten: thuocTinh.mauSacList.find((item) => {
                              return item.id === item2;
                            })?.tenMau,
                          });
                        }
                        setFilterMauSac(filterMauSac);
                        handleFilter({
                          page: page,
                          pageSize: pageSize,
                          filter: {
                            mauSac: e,
                            chatLieu: filterChatLieu.map((item) => {
                              return item.id
                            }),
                            nhomSanPham: filterNhomSanPham.map((item) => {
                              return item.id
                            }),
                            kichThuoc: filterKichThuoc.map((item) => {
                              return item.id
                            })
                          }
                        })
                      }}
                    />
                  </div>
                </AccordionPanel>
              </AccordionItem>
              <AccordionItem>
                <h2>
                  <AccordionButton>
                    <Box as="span" flex="1" textAlign="left">
                      {language.body.filter.item.cost.title}
                    </Box>
                    <AccordionIcon />
                  </AccordionButton>
                </h2>
                <AccordionPanel pb={4}>
                  <div
                    style={{
                      marginTop: "8px",
                      marginLeft: "8px",
                      width: "90%",
                    }}
                  >
                    <Radio.Group onChange={onChange} value={value}>
                      <Space direction="vertical">
                        {language.body.filter.item.cost.option.map(
                          (item, index) => {
                            return (
                              <Radio value={item.type} key={index}>
                                {item.name}
                              </Radio>
                            );
                          }
                        )}
                      </Space>
                    </Radio.Group>
                    <Row>
                      <Col span={24}>
                        <Slider
                          range={{
                            draggableTrack: true,
                          }}
                          tooltip={{
                            formatter,
                          }}
                          min={100000}
                          max={10000000}
                          defaultValue={[500000, 2000000]}
                        />
                      </Col>
                    </Row>
                    <Row>
                      <Col span={11}>
                        <InputNumber
                          min={1}
                          max={20}
                          style={{ width: "100%" }}
                          value={onChangeSlider}
                          onChange={onChangeSlider}
                        />
                      </Col>
                      <Col
                        span={2}
                        className="d-flex align-items-center justify-content-center"
                      >
                        -
                      </Col>
                      <Col span={11}>
                        <InputNumber
                          min={1}
                          max={20}
                          style={{ width: "100%" }}
                          value={onChangeSlider}
                          onChange={onChangeSlider}
                        />
                      </Col>
                    </Row>
                  </div>
                </AccordionPanel>
              </AccordionItem>
            </Accordion>
          )}
        </div>
      </div >
    </>
  );
}

export default Filter;
