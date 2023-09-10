import React, { useContext, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import ButtonGroup from "../../common/button/group.js";
import loggedAccountContext from "../../context/loggedAccount.js";
import AccountLink from "../account/link.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";
import alertContext from "../../context/alert.js";
import deletedReviewContext from "../../context/deletedReview.js";
import api from "../../../services/api.js";
import "./card.css";

function ReviewCard({ review, linkable }) {

    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const navigate = useNavigate();
    const { confirm, setConfirm } = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const {alert, setAlert} = useContext(alertContext);
    const {deletedReview, setDeletedReview} = useContext(deletedReviewContext);

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
               
                if (confirm === "deleteReview"+ review?.id)
                {
                    setConfirm(null);
                    await handleDelete();
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [confirm]
    );

    function handleFormatDate(date) {
        if (date !== null && date !== undefined) {
            var dateArray = date?.split("-");
            var newDate = dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0];
            return newDate;
        }
        else {
            return undefined;
        }
    }
    function handleConfirmDelete() {
        setConfirm([{ identifier: "delete" + review?.id, text: "Delete review of book " + review?.title + "?", options: [{ type: "deleteReview" + review?.id, text: "Delete" }, { type: "cancel", text: "Cancel" }] }]);
    }

    function handleEdit() {
        navigate("/review/edit/" + review?.id);
    }

    async function handleDelete()
    {
        try
        {
            setOverlay(true);
            var response = await api.delete
            (
                "/review/deletebyid/"+review?.id,
                {
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            setAlert([{type: "success", text: "Review deleted."}]);
            setDeletedReview(response.data);
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed")
            {
                localStorage.clear();
                setLoggedAccount(null);
            }
            navigate("/");
        }
    }


    return (
        <div className="reviewCardArea">
            {
                !linkable ?
                    <Link
                        className="cover"
                        style={{ backgroundImage: "url(" + review?.cover + ")" }}
                        to = {"/book/view/" + review.bookApiId}
                    /> : <></>
            }
            <div className="info">
                {
                    !linkable ?
                        <Link className="title"
                        to = {"/book/view/" + review.bookApiId}>
                            {review?.title}
                        </Link> : <></>
                }

                <div className="middleBox">
                    {
                        linkable ?
                            <AccountLink account={review?.owner}/> : <></>
                    }
                    <div className="creationDate">
                        Created in {handleFormatDate(review?.creationDate)}
                    </div>
                    {
                        review?.editionDate !== null ?
                            <div className="editionDate">
                                Edited in {handleFormatDate(review?.editionDate)}
                            </div> : <></>
                    }
                    {
                        review?.bookScore !== null ?
                            <div className="bookScore">
                                {review?.bookScore} ★
                            </div> : <></>
                    }
                </div>
                <div className="text">
                    {review?.text}
                </div>
                {
                    loggedAccount instanceof Object && loggedAccount?.id === review?.owner?.id ?
                        <ButtonGroup options={[{ text: "Edit", operation: handleEdit }, { text: "Delete", operation: handleConfirmDelete }]} /> :
                        <></>
                }
            </div>
        </div>
    );
}
export default ReviewCard;