import { Container, Row, Col } from "reactstrap";
import Question from "./Question";
import VoteButton from "./VoteButton";

const VoteBox = ({ message, canVote, vote }) => {
    return (
        <Container>
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

export default VoteBox;
