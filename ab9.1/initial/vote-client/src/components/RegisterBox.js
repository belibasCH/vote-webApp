import React, { useState } from "react";
import { Form, Button, Input, Row, Col } from "reactstrap";

const errorStyle = {
    color: "red",
};

const RegisterBox = ({handler, error}) => {
    let [email, setEmail] = useState("");

    const handleChange = (e) => {
        setEmail(e.target.value);
    };

    const submit = () => {
        handler(email);
        setEmail("");
    };

    return (
        <Form>
            <Row>
                <Col sm="12" md={{ size: 6, offset: 2 }}>
                    <Input
                        type="email"
                        id="email"
                        placeholder="your email"
                        value={email}
                        onChange={handleChange}
                    />
                </Col>
                <Col xs="1">
                    <Button color="secondary" onClick={submit}>
                        Register
                    </Button>
                </Col>
            </Row>
            <Row>
                <Col style={errorStyle} sm="12" md={{ size: 6, offset: 2 }}>
                    {error}
                </Col>
            </Row>
        </Form>
    );
};
export default RegisterBox;
