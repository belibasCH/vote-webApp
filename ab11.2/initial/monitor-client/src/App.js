import React, { useState, useEffect } from "react";
import { Row, Container } from "reactstrap";
import Counter from "./Counter";
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";

let server_url = "localhost:8080/server";

const App = () => {
    const [result, setResult] = useState({'yes':0,'no':0});

    useEffect(() => {
        console.log("Server set to '%s'", server_url);
    }, []);

    return (
        <Container fluid>
            <Row>
                <Counter background="#c7e5ae" counter={result.yes} header="yes" />
                <Counter background="#e5aeae" counter={result.no} header="no" />
            </Row>
        </Container>
    );
};

export default App;
