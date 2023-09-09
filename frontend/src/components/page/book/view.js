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
        console.log('Fetching book for apiId:', apiId);
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
            <div className="First Column">
                {book && <BookInfo book={book} />}

                <Link to="/review/create">
                    <button className="add-review-button">Add a Review</button>
                </Link>
            </div>

            <div className="Second Column">
                <button onClick={toggleDetails}>
                    {showDetails ? "Hide Details" : "View Details"}
                </button>

                {showDetails ?
                    <div>
                        <div>Published in {book?.publishedDate}</div>
                        <div>Published by {book?.publisher}</div>
                        <div>Language: {book?.language}</div>
                        <div>Pages: {book?.pageCount}</div>
                        <div>
                            ISBNs:
                            <div>
                                {book?.ISBNs?.map((isbn, index) => (
                                    <div> {index}
                                        {isbn?.identifier} ({isbn?.type})
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div> : <></>
                }
                {
                    loggedAccount?.id !== undefined ?
                        <BookshelfManage book = {book}/> :
                        <></>
                }
            </div>
        </div>
    );
}

export default BookView;