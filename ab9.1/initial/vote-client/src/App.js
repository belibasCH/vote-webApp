import React, { useState } from "react";
import { Container, Row, Col, Button } from "reactstrap";
import Header from "./Header";
import Question from "./components/Question";
import VoteButton from "./components/VoteButton";
import RegisterBox from "./components/RegisterBox";

let server_url = "localhost:8080/server";

const App = () => {
    const [poll, setPoll] = useState(undefined);
    const [waiting, setWaiting] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [token, setToken] = useState("");

    const getQuestion = async () => {
        console.log("getQuestion() called");
    };

    const register = async (email) => {
        console.log("register() called");
    };

    const vote = async (answer) => {
        console.log("vote() called");
    };

    const getRegisterBox = (register) => {
        return <RegisterBox handler={register} error={errorMessage} />;
    };

    const getVoteBox = (canVote, vote) => {
        let message = "Waiting for new question...";
        if (!waiting) {
            message = poll.questionText;
        }
        return (
            <Container>
                <Row>
                    <Col>
                        <Button onClick={getQuestion}>Refresh</Button>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Question message={message} />
                    </Col>
                </Row>
                <Row>
                    <Col xs="6">
                        <VoteButton
                            isActive={canVote}
                            color="success"
                            message="Yes"
                            handler={vote}
                        />
                    </Col>
                    <Col xs="6">
                        <VoteButton
                            isActive={canVote}
                            color="danger"
                            message="No"
                            handler={vote}
                        />
                    </Col>
                </Row>
            </Container>
        );
    };

    let actBox;
    if (token === "") {
        actBox = getRegisterBox(register);
    } else {
        actBox = getVoteBox(!waiting, vote);
    }

    return (
        <Container>
            <Header
                title="Interactive Voting System"
                lead="Brought to you by the &copy; kvanc team - v1.1.0"
            />
            {actBox}
        </Container>
    );
};

export default App;
