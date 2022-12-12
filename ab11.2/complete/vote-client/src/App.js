import React, { useState, useEffect } from "react";
import { Container } from "reactstrap";
import Header from "./components/Header";
import RegisterBox from "./components/RegisterBox";
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";
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
    // IMPORTANT: Spring STOMP integration supports STOMP over SockJS and NOT STOMP over WebSocket
    const openStompWebSocket = () => {
        var socket = new SockJS("http://" + server_url + "/stomp");
        client = Stomp.over(socket);

        client.connect(
            {},
            () => {
                console.log("StompClient connected successfully");
                client.subscribe("/topic/question", async (message) => {
                    console.log("message received: '%s'", message.body);
                    const poll = await JSON.parse(message.body);
                    setPoll(poll);
                    setWaiting(false);
                });
            },
            (error) => {
                console.log(error);
                setWaiting(true);
                setToken("");
                setErrorMessage(
                    "System-Reset: Register again by Reloading App!"
                );
                client.deactivate();
            }
        );
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

    const vote = async (answer) => {
        if (!waiting) {
            const voteDTO = {
                token: token,
                answer: answer.message === "Yes",
            };
            client.send("/app/answer/" + poll.id, {}, JSON.stringify(voteDTO));
            setWaiting(true);
        }
    };

    const getRegisterBox = (register) => {
        return <RegisterBox handler={register} error={errorMessage} />;
    };

    const getVoteBox = (canVote, vote) => {
        let message = "Waiting for new question...";
        if (canVote) {
            message = poll.questionText;
        }
        return <VoteBox message={message} canVote={canVote} vote={vote} />;
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
