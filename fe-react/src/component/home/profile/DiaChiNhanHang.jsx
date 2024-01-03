import { Button, Col, Divider, Radio, Row, notification } from "antd";
import "./style.css";
import { useEffect, useState } from "react";
import { useNguoiDungStore } from "./useNguoiDungStore";
import { useParams } from "react-router-dom";
import { selectLanguage } from "../../../language/selectLanguage";
import { useSelector } from "react-redux";

function DiaChiNhanHang() {
    const param = useParams();
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

    return (
        <>
            {contextHolder}
            <h4
                style={{
                    fontStyle: "normal",
                    fontWeight: 700,
                    fontSize: "20px",
                    marginBottom: "4px",
                }}
            >
                Địa chỉ nhận hàng
            </h4>
            <Divider />
            <Row>
                <Col span={13}>
                    <p>
                        Mật khẩu cũ
                        <span
                            style={{
                                color: "red",
                            }}
                        >
                            *
                        </span>
                    </p>
                </Col>
            </Row>

        </>
    );
}

export default DiaChiNhanHang;
