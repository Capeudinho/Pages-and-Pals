import React, { useState, useEffect, useContext, useRef } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import overlayContext from "../../context/overlay.js";
import scrollContext from "../../context/scroll.js";
import deletedReviewContext from "../../context/deletedReview.js";
import confirmContext from "../../context/confirm.js";

import BookshelfManage from "../../common/bookshelf/manage";
import ReviewCard from "../../common/review/card.js";



import "./view.css";

function BookView() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const { overlay, setOverlay } = useContext(overlayContext);
    const [showDetails, setShowDetails] = useState(false);
    const [book, setBook] = useState(null);
    const { scroll, setScroll } = useContext(scrollContext);
    const { deletedReview, setDeletedReview } = useContext(deletedReviewContext);
    const { confirm, setConfirm } = useContext(confirmContext);
    const deletedReviewRef = useRef(false);
    const [reviews, setReviews] = useState(null);
    const { apiId } = useParams();
    const navigate = useNavigate();

    useEffect
    (
        () => {
            let mounted = true;
            const runEffect = async () => {
                await loadBook();
            }
            runEffect();
            return (() => { mounted = false });
        }, [apiId]
    );

    useEffect
    (
        () => {
            let mounted = true;
            const runEffect = async () => {
                if (typeof confirm === "string" && confirm?.startsWith("deleteReview"))
                {
                    await loadBook();
                }
            }
            runEffect();
            return (() => { mounted = false });
        }, [confirm]
    );

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if
                        (
                        !overlay &&
                        reviews?.length < book?.reviewCount &&
                        scroll?.target?.scrollHeight - scroll?.target?.scrollTop <= scroll?.target?.offsetHeight + 100
                    ) {
                        if (deletedReviewRef.current === false) {
                            await loadReviews(false);
                        } else {
                            deletedReviewRef.current = false;
                        }
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [scroll, reviews]
        );

    useEffect
        (
            () => {

                var index;
                if (deletedReview !== null) {
                    for (var i = 0; i < reviews.length; i++) {
                        if (reviews[i].id === deletedReview.id) {
                            index = i;
                        }
                    }

                    var newReviews = [...reviews];
                    newReviews.splice(index, 1);
                    deletedReviewRef.current = true;
                    setReviews(newReviews);
                    setDeletedReview(null);
                }
            },
            [deletedReview]
        );

    async function loadBook()
    {
        try {
            setOverlay(true);
            var response = await api.get
                (
                    "book/findbyapiid/" + apiId
                );
            if (response?.data?.reviewCount > 0) {
                await loadReviews(true);
            }
            setOverlay(false);
            setBook(response?.data);
        } catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
            }
            navigate("/");
        }
    }

    async function loadReviews(overwrite) {
        if (overwrite || reviews?.length < book?.reviewCount) {
            var offset;
            if (overwrite) {
                offset = 0;
            }
            else {
                offset = reviews?.length;
            }
            try {
                var response;
                setOverlay(true);
                if (loggedAccount?.id !== undefined) {
                    response = await api.get
                        (
                            "review/findbybookapiidpaginateautenticated/" + apiId,
                            {
                                params:
                                {
                                    offset: offset,
                                    limit: 20
                                },
                                headers:
                                {
                                    email: loggedAccount?.email,
                                    password: loggedAccount?.password
                                }
                            }
                        );
                }
                else {
                    response = await api.get
                        (
                            "/review/findbybookapiidpaginate/" + apiId,
                            {
                                params:
                                {
                                    offset: offset,
                                    limit: 20
                                }
                            }
                        );
                }
                setOverlay(false);
                if (overwrite) {
                    setReviews(response?.data);
                }
                else {
                    setReviews([...reviews, ...response?.data]);
                }
            }
            catch (exception) {
                setOverlay(false);
                if (exception?.response?.data === "authentication failed") {
                    localStorage.clear();
                    setLoggedAccount(null);
                }
                navigate("/");
            }
        }
    }

    function handleFormatDescription(description)
    {
        const cleanedText = description?.replace(/<[^>]*>?/gm, '');
        const formattedText = cleanedText?.replace(/<br\s*\/?>/gm, '\n');
        return formattedText;
    }

    function handleFormatDate(date)
    {
        if (date !== null && date !== undefined)
        {
            var dateArray = date?.split("-");
            var newDate = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
            return newDate;
        }
        else
        {
            return undefined;
        }
    }

    const toggleDetails = () => {
        setShowDetails(!showDetails);
    };

    return (
        <div className="page bookViewArea">
            <div className="bookBox">
                <div className="scoreBox">
                    <div
                        className="cover"
                        style={{ backgroundImage: "url(" + book?.cover + ")" }}
                    />
                    {
                        book?.score !== null ?
                            <div className="score">{book?.score} <b>★</b></div> :
                            <></>
                    }
                </div>
                <div className="book">
                    <div className="topBox">
                        <div className="topRightBox">
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
                        </div>
                        {
                            loggedAccount?.id !== undefined ?
                            <BookshelfManage book={book} />:
                            <></>
                        }
                    </div>
                    {
                        book?.description !== "" ?
                            <div
                                className="description">{handleFormatDescription(book?.description)}
                            </div>
                            :
                            <></>
                    }
                    <button
                    className ="showMoreButton"
                    onClick={toggleDetails}
                    >
                        {showDetails ? "Hide details" : "View details"}
                    </button>
                    {showDetails && (
                        <div className="additionalInfo">
                            <div className="labels">
                                {
                                    book?.publishedDate !== undefined ?
                                    <div className="publishedDate">Published date</div> :
                                    <></>
                                }
                                {
                                    book?.publisher !== undefined ?
                                    <div className="publisher">Publisher</div> :
                                    <></>
                                }
                                {
                                    book?.language !== undefined ?
                                    <div className="language">Language</div> :
                                    <></>
                                }
                                {
                                    book?.pageCount !== undefined ?
                                    <div className="pageCount">Page count</div> :
                                    <></>
                                }
                                {
                                    book?.ISBNs?.length > 0 ? 
                                    <div className="ISBNs"> ISBNs</div> :
                                    <></>
                                }
                            </div>
                            <div className="content">
                                {
                                    book?.publishedDate !== undefined ?
                                    <div className="publishedDate">
                                        {handleFormatDate(book?.publishedDate)}
                                    </div> :
                                    <></>
                                }
                                {
                                    book?.publisher !== undefined ?
                                    <div className="publisher">
                                        {book?.publisher}
                                    </div> :
                                    <></>
                                }
                                {
                                    book?.language !== undefined ?
                                    <div className="language">
                                        {book?.language}
                                    </div> :
                                    <></>
                                }
                                {
                                    book?.pageCount !== undefined ?
                                    <div className="pageCount">
                                        {book?.pageCount}
                                    </div> :
                                    <></>
                                }
                                {
                                    book?.ISBNs?.length > 0 ? 
                                    book?.ISBNs?.map
                                    (
                                        (ISBNs, ISBNsIndex) =>
                                        (
                                            <div className="ISBN" key={ISBNsIndex}>
                                                <div>{ISBNs?.identifier}</div>
                                                <div>{ISBNs?.type}</div>
                                            </div>
                                        )
                                    ) :
                                    <></>
                                }
                            </div>
                        </div>
                    )
                    }
                </div>
            </div>
            <div className="reviewBox">
                <Link to={"/review/create/" + apiId}>
                    <button className="addReviewButton">Create review</button>
                </Link>
                {
                    reviews?.map
                        (
                            (review, index) => {
                                return (
                                    <ReviewCard
                                        review={review}
                                        linkable={true}
                                        key={index}
                                    />
                                );
                            }
                        )
                }
            </div>
        </div>
    );
}

export default BookView;