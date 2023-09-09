import React, { useState } from "react";
import { Link } from "react-router-dom";

import "./info.css";

function BookInfo({ book }) {
    const [showDetails, setShowDetails] = useState(false);

    function cleanDescriptionUtility(description) {
        const cleanedText = description.replace(/<[^>]*>?/gm, '');

        const formattedText = cleanedText.replace(/<br\s*\/?>/gm, '\n');

        return formattedText;
    }

    const toggleDetails = () => {
        setShowDetails(!showDetails);
    };

    return (
        <div className="bookInfoArea">
            <div className="rateArea">
                <div
                    className="cover"
                    style={{ backgroundImage: "url(" + book?.cover + ")" }}
                />
                {
                    book?.scoreTotal !== null ?
                        <div className="score">{book?.scoreTotal} ★ </div> :
                        <></>
                }

                <Link to="/review/create">
                    <button className="add-review-button">Add a Review</button>
                </Link>
            </div>
            <div
                className="rest" >
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
                        {
                            book?.publishedDate ? (
                                <div className="publishedDate">
                                    Published Date {book?.publishedDate}
                                </div>
                            ) : (
                                <></>
                            )
                        }

                        {book?.publisher ? (
                            <div className="publisher">Publisher {book?.publisher}</div>
                        ) : (
                            <></>
                        )}

                        {book?.language ? (
                            <div className="language">Language {book?.language}</div>
                        ) : (
                            <></>
                        )}

                        {book?.pageCount ? (
                            <div className="pageCount">Page Count {book?.pageCount}</div>
                        ) : (
                            <></>
                        )}

                        {
                            book?.ISBNs?.length > 0 ? (
                                <div className="ISBNs"> ISBNs
                                    {book?.ISBNs?.map((ISBNs, ISBNsIndex) => (
                                        <div className="ISBN" key={ISBNsIndex}>
                                            {ISBNs.identifier} ({ISBNs.type})
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <></>
                            )
                        }

                    </div>
                )
                }
            </div >
        </div >
    );
}

export default BookInfo;
