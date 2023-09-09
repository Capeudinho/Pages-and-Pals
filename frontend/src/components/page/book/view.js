import React, { useState, useEffect, useContext } from "react";
import { useParams, Link } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import overlayContext from "../../context/overlay.js";

import BookInfo from "../../common/book/info.js";
import BookshelfManage from "../../common/bookshelf/manage";

import "./view.css";

function BookView() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const { overlay, setOverlay } = useContext(overlayContext);
    const [showDetails, setShowDetails] = useState(false);
    const [book, setBook] = useState(null);
    const { apiId } = useParams();

    async function fetchBookByApiId(apiId) {
        try {
            setOverlay(true);
            var response = await api.get
                (
                    "book/findbyapiid/" + apiId
                )

            setOverlay(false);
            setBook(response?.data);

        } catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
            }
            //navigate("/");
        }
    }
    useEffect
        (
            () => {
                fetchBookByApiId(apiId);
            }, [apiId]
        );

    const toggleDetails = () => {
        setShowDetails(!showDetails);
    };

    return (
        <div className="page bookViewArea">

            {book && <BookInfo book={book} />}

                {
                    loggedAccount?.id !== undefined ?
                        <BookshelfManage book = {book}/> :
                        <></>
                }
            
        </div>
    );
}

export default BookView;