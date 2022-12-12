import React, { useState, useEffect } from "react";
import { Container } from "reactstrap";
import Header from "./components/Header";
import RegisterBox from "./components/RegisterBox";
import SockJS from 'sockjs-client';
import Stomp from 'stomp-websocket';
import VoteBox from "./components/VoteBox";

let server_url = "localhost:8080/server";
let client = undefined;

const App = () => {
    const [poll, setPoll] = useState(undefined);
    const [waiting, setWaiting] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [token, setToken] = useState("");

    useEffect(() => {
        // executed just after component is mounted
        console.log("Server set to '%s'", server_url);
    }, []);


    // [STOMP] Stomp over SockJS
    const openStompWebSocket = () => {
        console.log("openStompWebSocket called");
    };

    const register = async (email) => {
        const tokenDto = {
            email: email,
        };
        try {
            const response = await fetch(
                "http://" + server_url + "/registrations",
                {
                    method: "POST",
                    headers: new Headers({
                        "Content-Type": "application/json",
                    }),
                    body: JSON.stringify(tokenDto),
                }
            );
            if (!response.ok) {
                setErrorMessage("Error! Check your E-Mail!");
            } else {
                const dto = await response.json();
                openStompWebSocket();
                setToken(dto.token);
            }
        } catch (error) {
            setErrorMessage("Error! Server is not available");
        }
    };

    // [STOMP]
    const vote = async (answer) => {
        console.log("vote called");
    };

    const getRegisterBox = (register) => {
        return <RegisterBox handler={register} error={errorMessage} />;
    };

    const getVoteBox = (canVote, vote) => {
        let message = "Waiting for new question...";
        if (canVote) {
            message = poll.questionText;
        }
        return <VoteBox message={message} canVote={canVote} vote={vote}/>;
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
