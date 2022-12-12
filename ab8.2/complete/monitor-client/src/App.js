import { useState, useEffect } from "react";
import { Row, Container } from "reactstrap";
import Counter from "./Counter";

const App = () => {
    const [result, setResult] = useState({ yes: 0, no: 0 });

    useEffect(() => {
        const timer = setInterval(() => {
            setResult(prevState => {
                const no = prevState.no < 9 ? prevState.no + 1 : 0;
                return({ yes: prevState.yes, no: no });
            });
        }, 2000);
        return () => clearInterval(timer);
    }, []);

    return (
        <Container fluid>
            <Row>
                <Counter
                    background="#c7e5ae"
                    counter={result.yes}
                    header="yes"
                />
                <Counter 
                    background="#e5aeae" 
                    counter={result.no} 
                    header="no" />
            </Row>
        </Container>
    );
};

export default App;
