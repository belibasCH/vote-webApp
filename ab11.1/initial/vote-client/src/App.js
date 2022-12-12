import React, { useState, useEffect } from "react";
import { Container } from "reactstrap";
import Header from "./components/Header";
import RegisterBox from "./components/RegisterBox";
import VoteBox from "./components/VoteBox";

let server_url = "localhost:8080/server";

const App = () => {
    const [poll, setPoll] = useState(undefined);
    const [waiting, setWaiting] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [token, setToken] = useState("");

    useEffect(() => {
        console.log("Server set to '%s'", server_url);
    }, []);

    const subscribeForSse = (token) => {
        console.log("subscribeForSse() called");
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
                subscribeForSse(dto.token);
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
            const url =
                "http://" + server_url + "/polls/" + poll.id + "/answers";
            const request = new Request(url, {
                method: "POST",
                headers: new Headers({
                    "Content-Type": "application/json",
                }),
                body: JSON.stringify(voteDTO),
            });
            try {
                const response = await fetch(request);
                if (!response.ok) {
                    throw Error(
                        "HTTP Status Code received: " + response.status
                    );
                }
                setWaiting(true);
            } catch (error) {
                console.error(error);
                setWaiting(true);
            }
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
