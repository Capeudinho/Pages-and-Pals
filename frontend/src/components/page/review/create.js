import React, { useState, useEffect, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";

import "./create.css";

function ReviewCreate() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const { alert, setAlert } = useContext(alertContext);
    const { confirm, setConfirm } = useContext(confirmContext);
    const { overlay, setOverlay } = useContext(overlayContext);
    const { bookApiId } = useParams();
    const [review, setReview] = useState
        (
            {
                text: "",
                bookScore: null,
                privacy: true,
                bookApiId: ""
            }
        );
    const navigate = useNavigate();

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if (confirm === "discard") {
                        setConfirm(null);
                        handleDiscard();
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [confirm]
        );

    function handleChangeText(event) {
        var newReview = { ...review };
        newReview.text = event.target.innerText;
        setReview(newReview);
    }

    function handlePlusBookScore() {
        if (review.bookScore === null)
        {
            var newReview = { ...review };
            newReview.bookScore = 0.5;
            setReview(newReview);
        }
        else if (review.bookScore < 5)
        {
            var newReview = { ...review };
            newReview.bookScore = newReview.bookScore+0.5;
            setReview(newReview);
        }
    }

    function handleMinusBookScore() {
        if (review.bookScore === null)
        {
            var newReview = { ...review };
            newReview.bookScore = 0.5;
            setReview(newReview);
        }
        else if (review.bookScore > 0.5)
        {
            var newReview = { ...review };
            newReview.bookScore = newReview.bookScore-0.5;
            setReview(newReview);
        }
    }

    function handleClearBookScore() {
        var newReview = { ...review };
        newReview.bookScore = null;
        setReview(newReview);
    }

    function handleChangePrivacy(newPrivacy) {
        var newReview = { ...review };
        newReview.privacy = newPrivacy;
        setReview(newReview);
    }

    function handleConfirmDiscard() {
        setConfirm([{ identifier: "discard", text: "Discard review?", options: [{ type: "discard", text: "Discard" }, { type: "cancel", text: "Cancel" }] }]);
    }

    async function handleCreate() {
        try {
            setOverlay(true);
            var newReview = { ...review };
            newReview.bookApiId = bookApiId;
            await api.post
                (
                    "/review/create",
                    newReview,
                    {
                        headers:
                        {
                            email: loggedAccount?.email,
                            password: loggedAccount?.password
                        }
                    }
                );
            setOverlay(false);
            setAlert([{ type: "success", text: "Review created." }]);
            navigate("/book/view/" + bookApiId);
        }
        catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "invalid book score") {
                setAlert([{ type: "warning", text: "Book score is invalid." }]);
            }
            if (exception?.response?.data === "invalid text") {
                setAlert([{ type: "warning", text: "Review text is invalid." }]);
            }
            if (exception?.response?.data === "invalid review") {
                setAlert([{ type: "warning", text: "Review is invalid." }]);
            }
            if (exception?.response?.data === "duplicate review") {
                setAlert([{ type: "warning", text: "Duplicate review." }]);
                navigate("/book/view/" + bookApiId);
            }
            else if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
                navigate("/");
            }
            else {
                navigate("/");
            }
        }
    }

    async function handleDiscard() {
        navigate("/book/view/" + bookApiId);
    }

    return (
        <div className="page reviewCreateArea">
            <div className="label">Text</div>
            <div
                className="textInput"
                value={review?.text}
                onInput={(event) => { handleChangeText(event) }}
                spellCheck={false}
                contentEditable={true}
                placeholder="Type your review"
            />
            <div className="label">Score</div>
            <div className="bookScoreBox">
                <div className="numberBox">
                    <div className="bookScore">
                        {review?.bookScore !== null ? <> {review?.bookScore} <b>★</b></> : "No score"}
                    </div>
                    <button className="minusButton" onClick={() => {handleMinusBookScore()}}>
                        −
                    </button>
                    <button className="plusButton" onClick={() => {handlePlusBookScore()}}>
                        +
                    </button>
                </div>
                <button className="clearButton" onClick={() => {handleClearBookScore()}}>Clear</button>
            </div>
            <div className="label">Privacy</div>
            <ButtonMode
                modes={[{ text: "Public", type: true }, { text: "Private", type: false }]}
                currentMode={review?.privacy}
                setMode={handleChangePrivacy}
                appearance="switch"
            />
            <ButtonGroup options={[{ text: "Create", operation: handleCreate }, { text: "Discard", operation: handleConfirmDiscard }]} />
        </div>
    );
}

export default ReviewCreate;