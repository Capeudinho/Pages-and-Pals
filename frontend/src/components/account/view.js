import React, {useState, useEffect, useContext} from "react";
import {useParams, useNavigate, Link} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./view.css";

function AccountView()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [biography, setBiography] = useState("");
    const [privacy, setPrivacy] = useState(false);
    const [picture, setPicture] = useState("");
    const [bookshelfCards, setBookshelfCards] = useState([]);
    const [mode, setMode] = useState("biography");
    const [bookshelfCardsPage, setBookshelfCardsPage] = useState(-1);
    const navigate = useNavigate();
    const {id} = useParams();

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                setOverlay(true);
                try
                {
                    const accountResponse = await api.get("/account/findbyid/"+id);
                    setName(accountResponse.data.name);
                    setEmail(accountResponse.data.email);
                    setBiography(accountResponse.data.biography);
                    setPrivacy(accountResponse.data.privacy);
                    setPicture(accountResponse.data.picture);
                }
                catch (exception)
                {
                    if (exception.response.hasOwnProperty("data"))
                    {
                        if (exception.response.data === "incorrect id")
                        {
                            navigate("/");
                        }
                    }
                }
                setOverlay(false);
            }
            runEffect();
            return (() => {mounted = false;});
        },
        [id]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (bookshelfCardsPage !== -1)
                {
                    setOverlay(true);
                    try
                    {
                        const bookshelfCardsResponse = await api.get
                        (
                            "/bookshelf/findcardsbyownerid/"+id,
                            {
                                params:
                                {
                                    page: bookshelfCardsPage,
                                    size: 20
                                }
                            }
                        );
                        setBookshelfCards([...bookshelfCards, ...bookshelfCardsResponse.data]);
                    }
                    catch (exception) {}
                    setOverlay(false);
                }
            }
            runEffect();
            return (() => {mounted = false;});
        },
        [bookshelfCardsPage]
    );

    function handleLogOut()
    {
        localStorage.clear();
        setLoggedAccount(null);
        navigate("/");
    }

    function handleChangeModeBiography()
    {
        setMode("biography");
    }

    async function handleChangeModeBookshelves()
    {
        if (bookshelfCardsPage === -1)
        {
            setBookshelfCardsPage(0);
        }
        setMode("bookshelves");
    }

    function handleChangeModeReviews()
    {
        setMode("reviews");
    }

    function handleInvalidPicture()
    {
        setPicture("");
    }

    function handleInvalidCover(bookshelfCardIndex, coverIndex)
    {
        var newBookshelfCards = [...bookshelfCards];
        newBookshelfCards[bookshelfCardIndex].covers[coverIndex] = "";
        setBookshelfCards(newBookshelfCards);
    }

    function handleEdit()
    {
        navigate("/account/edit");
    }

    async function handleDelete()
    {
        setOverlay(true);
        try
        {
            await api.delete("/account/deletebyid/"+loggedAccount.id);
            localStorage.clear();
            setLoggedAccount(null);
            setAlert([{text: "Account deleted.", type: "success", key: Math.random()}]);
            navigate("/");
        }
        catch (exception)
        {
            if (exception.response.hasOwnProperty("data"))
            {
                if (exception.response.data === "incorrect id")
                {
                    localStorage.clear();
                    setLoggedAccount(null);
                    navigate("/");
                }
            }
        }
        setOverlay(false);
    }

    return (
        <div className = "area accountViewArea">
            <div className = "mainBox">
                <div className = "topBox">
                    <div
                    className = "picture"
                    onError = {() => {handleInvalidPicture()}}
                    style = {{backgroundImage: picture === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+picture+")"}}
                    />
                    <div className = "headBox">
                        <div className = "name">{name}</div>
                        <div
                        className = "email"
                        style = {{display: (loggedAccount !== null && id === loggedAccount.id.toString()) || privacy ? "block" : "none"}}
                        >
                            {email}
                        </div>
                    </div>
                    <div
                    className = "buttonBox"
                    style = {{display: loggedAccount !== null && id === loggedAccount.id.toString() ? "flex" : "none"}}
                    >
                        <button
                        className = "normalButton"
                        onClick = {() => {handleEdit()}}
                        >
                            Edit
                        </button>
                        <button
                        className = "normalButton"
                        onClick = {() => {handleLogOut()}}
                        >
                            Log out
                        </button>
                        <button
                        className = "normalButton"
                        onClick = {() => {handleDelete()}}
                        >
                            Delete
                        </button>
                    </div>
                </div>
                <div
                className = "modeBox"
                style = {{display: (loggedAccount !== null && id === loggedAccount.id.toString()) || privacy ? "flex" : "none"}}
                >
                    <button
                    className = "modeButton modeBiographyButton"
                    onClick = {() => {handleChangeModeBiography()}}
                    style = {{backgroundColor: mode === "biography" ? "#ffffff" : "#cccccc"}}
                    >
                        Biography
                    </button>
                    <button
                    className = "modeButton modeBookshelvesButton"
                    onClick = {() => {handleChangeModeBookshelves()}}
                    style = {{backgroundColor: mode === "bookshelves" ? "#ffffff" : "#cccccc"}}
                    >
                        Bookshelves
                    </button>
                    <button
                    className = "modeButton modeReviewsButton"
                    onClick = {() => {handleChangeModeReviews()}}
                    style = {{backgroundColor: mode === "reviews" ? "#ffffff" : "#cccccc"}}
                    >
                        Reviews
                    </button>
                </div>
                <div
                className = "infoBox"
                style = {{display: (loggedAccount !== null && id === loggedAccount.id.toString()) || privacy ? "block" : "none"}}
                >
                    <div
                    className = "innerInfoBox biographyBox"
                    style = {{display: mode === "biography" ? "block" : "none"}}
                    >
                        <div className = "biography">{biography}</div>
                    </div>
                    <div
                    className = "innerInfoBox bookshelvesBox"
                    style = {{display: mode === "bookshelves" ? "block" : "none"}}
                    >
                        <Link
                        to = {"/bookshelf/create"}
                        >
                            <button className = "createBookshelfButton">Create</button>
                            </Link>
                        {
                            bookshelfCards.map
                            (
                                (bookshelfCard, bookshelfCardIndex) =>
                                {
                                    return (
                                        <Link
                                        to = {"/bookshelf/view/"+bookshelfCard.bookshelf.id}
                                        key = {bookshelfCardIndex}
                                        >
                                            <div className = "bookshelf">
                                                <div className = "bookshelfCovers">
                                                    <div
                                                    className = "cover coverOne"
                                                    onError = {() => {handleInvalidCover(bookshelfCardIndex, 0)}}
                                                    style = {{backgroundImage: bookshelfCard.covers[0] === undefined || bookshelfCard.covers[0] === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookshelfCard.covers[0]+")"}}
                                                    />
                                                    <div
                                                    className = "cover coverTwo"
                                                    onError = {() => {handleInvalidCover(bookshelfCardIndex, 1)}}
                                                    style = {{backgroundImage: bookshelfCard.covers[1] === undefined || bookshelfCard.covers[1] === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookshelfCard.covers[1]+")"}}
                                                    />
                                                    <div
                                                    className = "cover coverThree"
                                                    onError = {() => {handleInvalidCover(bookshelfCardIndex, 2)}}
                                                    style = {{backgroundImage: bookshelfCard.covers[2] === undefined || bookshelfCard.covers[2] === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookshelfCard.covers[2]+")"}}
                                                    />
                                                </div>
                                                <div className = "bookshelfInfo">
                                                    <div className = "bookshelfName">
                                                        {bookshelfCard.bookshelf.name}
                                                    </div>
                                                    <div className = "bookshelfDescription">
                                                        {bookshelfCard.bookshelf.description}
                                                    </div>
                                                </div>
                                            </div>
                                        </Link>
                                    );
                                }
                            )
                        }
                    </div>
                    <div
                    className = "innerInfoBox reviewsBox"
                    style = {{display: mode === "reviews" ? "block" : "none"}}
                    >
                        
                    </div>
                </div>
                <div
                className = "bottomBox"
                style = {{display: !((loggedAccount !== null && id === loggedAccount.id.toString()) || privacy) ? "block" : "none"}}
                >
                    This account is private.
                </div>
            </div>
        </div>
    );
}

export default AccountView;