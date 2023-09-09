import React, { useState, useEffect, useContext } from "react";
import { useParams, Link } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import overlayContext from "../../context/overlay.js";

import BookshelfManage from "../../common/bookshelf/manage";

import "./view.css";

function BookView() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const { overlay, setOverlay } = useContext(overlayContext);
    const [showDetails, setShowDetails] = useState(false);
    const [book, setBook] = useState(null);
    const { apiId } = useParams();

    function cleanDescriptionUtility(description) {
        const cleanedText = description?.replace(/<[^>]*>?/gm, '');

        const formattedText = cleanedText?.replace(/<br\s*\/?>/gm, '\n');

        return formattedText;
    }

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
            <div className="bookArea">
                <div className="scoreArea">
                    <div
                        className="cover"
                        style={{ backgroundImage: "url(" + book?.cover + ")" }}
                    />
                    {
                        book?.score !== null ?
                            <div className="score">{book?.score} ★</div> :
                            <></>
                    }
                </div>
                <div
                    className="bookInfoArea" >
                    {
                        book?.title !== "" ?
                            <div className="title">{book?.title}</div> :
                            <></>
                    }
                    {
                        book?.authors?.length > 0 ?
                            <div className="authors">
                                {
                                    book?.authors?.map
                                        (
                                            (author, authorIndex) => {
                                                return (
                                                    <div
                                                        className="author"
                                                        key={authorIndex}
                                                    >
                                                        {author}
                                                    </div>
                                                );
                                            }
                                        )
                                }
                            </div> :
                            <></>
                    }
                    {
                        book?.categories?.length > 0 ?
                            <div className="categories">
                                {
                                    book?.categories?.map
                                        (
                                            (category, categoryIndex) => {
                                                return (
                                                    <div
                                                        className="category"
                                                        key={categoryIndex}
                                                    >
                                                        {category}
                                                    </div>
                                                );
                                            }
                                        )
                                }
                            </div> :
                            <></>
                    }
                    {
                        book?.description !== "" ?
                            <div
                                className="description">{cleanDescriptionUtility(book?.description)}
                            </div>
                            :
                            <></>
                    }
                    <button onClick={toggleDetails}>
                        {showDetails ? "Hide Details" : "View Details"}
                    </button>

                    {showDetails && (
                        <div className="additionalInfoArea">
                            <div className="labels">
                                {
                                    book?.publishedDate ? (
                                        <div className="publishedDate">Published Date
                                        </div>
                                    ) : (
                                        <></>
                                    )
                                }

                                {book?.publisher ? (
                                    <div className="publisher">Publisher </div>
                                ) : (
                                    <></>
                                )}

                                {book?.language ? (
                                    <div className="language">Language </div>
                                ) : (
                                    <></>
                                )}

                                {book?.pageCount ? (
                                    <div className="pageCount">Page Count </div>
                                ) : (
                                    <></>
                                )}

                                {
                                    book?.ISBNs?.length > 0 ? (
                                        <div className="ISBNs"> ISBNs
                                        </div>
                                    ) : (
                                        <></>
                                    )
                                }
                            </div>

                            <div className="content">

                                {book?.publishedDate ?
                                    <div className="publishedDate">
                                        {book?.publishedDate}
                                    </div> :
                                    <></>
                                }


                                {book?.publisher ?
                                    <div className="publisher">
                                        {book?.publisher}
                                    </div> :
                                    <></>
                                }

                                {book?.language ?
                                    <div className="language">
                                        {book?.language}
                                    </div> :
                                    <></>
                                }

                                {book?.pageCount ?
                                    <div className="pageCount">
                                        {book?.pageCount}
                                    </div> :
                                    <></>
                                }

                                {
                                    book?.ISBNs?.length > 0 ? (
                                        book?.ISBNs?.map((ISBNs, ISBNsIndex) => (
                                            <div className="ISBN" key={ISBNsIndex}>
                                                {ISBNs.identifier} ({ISBNs.type})
                                            </div>
                                        ))
                                    ) : (
                                        <></>
                                    )
                                }


                            </div>
                        </div>
                    )
                    }
                </div >

                {
                    loggedAccount?.id !== undefined ?
                        <BookshelfManage book={book} /> :
                        <></>
                }
            </div>
            <div className="reviewArea">
                <div className="reviews">Reviews</div>
                <Link to="/review/create">
                    <button className="addReviewButton">Add a Review</button>
                </Link>
            </div>

        </div>
    );
}

export default BookView;