import React, { useState, useEffect } from "react";
import { Row, Container } from "reactstrap";
import Counter from "./Counter";

let server_url = "localhost:8080/server";

const App = () => {
    const [result, setResult] = useState({'yes':0,'no':0});

    useEffect(() => {
        console.log("Server set to '%s'", server_url);

        const socket = new WebSocket('ws://' + server_url + "/ws/result")
        socket.onopen = () => {
          console.log('WebSocket connected successfully')
        }
        socket.onmessage = async (event) => {
          console.log("message received: '%s'", event.data)
          const result = await JSON.parse(event.data);
          setResult(result);
        }
        socket.onclose = (event) => {
          console.warn(event)
        }
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
