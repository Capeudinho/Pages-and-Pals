import React, {useState, useEffect, useContext} from "react";
import {useParams, useNavigate, Link} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./view.css";

function BookshelfView()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [creationDate, setCreationDate] = useState("");
    const [privacy, setPrivacy] = useState(false);
    const [ownerId, setOwnerId] = useState("");
    const [bookCards, setBookCards] = useState([]);
    const [bookCardsPage, setBookCardsPage] = useState(-1);
    const [mode, setMode] = useState("books");
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
                    const accountResponse = await api.get("/bookshelf/findbyid/"+id);
                    setName(accountResponse.data.name);
                    setDescription(accountResponse.data.description);
                    setCreationDate(accountResponse.data.creationDate);
                    setPrivacy(accountResponse.data.privacy);
                    setOwnerId(accountResponse.data.owner.id);
                    setBookCardsPage(0);
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
                if (bookCardsPage !== -1)
                {
                    setOverlay(true);
                    try
                    {
                        const bookCardsResponse = await api.get
                        (
                            "/bookshelf/findbookcardsbyid/"+id,
                            {
                                params:
                                {
                                    page: bookCardsPage,
                                    size: 20
                                }
                            }
                        );
                        setBookCards([...bookCards, ...bookCardsResponse.data]);
                    }
                    catch (exception) {}
                    setOverlay(false);
                }
            }
            runEffect();
            return (() => {mounted = false;});
        },
        [bookCardsPage]
    );

    function handleChangeModeBooks()
    {
        setMode("books");
    }

    function handleChangeModeDescription()
    {
        setMode("description");
    }

    function handleFormatDate(date)
    {
        var dateArray = date.split("-");
        var newDate = dateArray[2]+"/"+dateArray[1]+"/"+dateArray[0];
        return newDate;
    }

    function handleInvalidCover(bookCardIndex)
    {
        var newBookCards = [...bookCards];
        newBookCards[bookCardIndex].cover = "";
        setBookCards(newBookCards);
    }

    function handleEdit()
    {
        navigate("/bookshelf/edit/"+id);
    }

    async function handleDelete()
    {
        setOverlay(true);
        try
        {
            await api.delete("/bookshelf/deletebyid/"+id);
            setAlert([{text: "Bookshelf deleted.", type: "success", key: Math.random()}]);
            navigate(-1);
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

    return(
        <div className = "bookshelfViewArea">
            <div className = "mainBox">
                <div className = "topBox">
                    <div className = "covers">
                        <div
                        className = "cover coverOne"
                        onError = {() => {handleInvalidCover(bookCards[0])}}
                        style = {{backgroundImage: bookCards[0] === undefined || bookCards[0].cover === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookCards[0].cover+")"}}
                        />
                        <div
                        className = "cover coverTwo"
                        onError = {() => {handleInvalidCover(bookCards[1])}}
                        style = {{backgroundImage: bookCards[1] === undefined || bookCards[1].cover === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookCards[1].cover+")"}}
                        />
                        <div
                        className = "cover coverThree"
                        onError = {() => {handleInvalidCover(bookCards[2])}}
                        style = {{backgroundImage: bookCards[2] === undefined || bookCards[2].cover === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+bookCards[2].cover+")"}}
                        />
                    </div>
                    <div className = "headBox">
                        <div className = "name">{name}</div>
                        <div className = "creationDate">{handleFormatDate(creationDate)}</div>
                    </div>
                    <div
                    className = "buttonBox"
                    style = {{display: loggedAccount !== null && ownerId === loggedAccount.id ? "flex" : "none"}}
                    >
                        <button
                        className = "normalButton"
                        onClick = {() => {handleEdit()}}
                        >
                            Edit
                        </button>
                        <button
                        className = "normalButton"
                        onClick = {() => {handleDelete()}}
                        >
                            Delete
                        </button>
                    </div>
                </div>
                <div className = "modeBox">
                    <button
                    className = "modeButton modeBooksButton"
                    onClick = {() => {handleChangeModeBooks()}}
                    style = {{backgroundColor: mode === "books" ? "#cccccc" : "#ffffff"}}
                    >
                        Books
                    </button>
                    <button
                    className = "modeButton modeDescriptionButton"
                    onClick = {() => {handleChangeModeDescription()}}
                    style = {{backgroundColor: mode === "description" ? "#cccccc" : "#ffffff"}}
                    >
                        Description
                    </button>
                </div>
                <div className = "infoBox">
                    <div
                    className = "books"
                    style = {{display: mode === "books" ? "flex" : "none"}}
                    >
                        {
                            bookCards.map
                            (
                                (book, bookIndex) =>
                                {
                                    return (
                                        <div className = "bookBox">
                                            <Link
                                            to = {"/books/view/"+book.apiId}
                                            key = {bookIndex}
                                            >
                                                <div className = "book">
                                                    <div
                                                    className = "cover"
                                                    onError = {() => {handleInvalidCover(bookIndex)}}
                                                    style = {{backgroundImage: book.cover === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+book.cover+")"}}
                                                    />
                                                    <div className = "bookInfoBox">
                                                        <div className = "title">{book.title}</div>
                                                        <div
                                                        className = "authors"
                                                        style = {{display: book.authors !== [] ? "flex" : "none"}}
                                                        >
                                                            {
                                                                book.authors.map
                                                                (
                                                                    (author, authorIndex) =>
                                                                    {
                                                                        return (
                                                                            <div
                                                                            className = "author"
                                                                            key = {authorIndex}
                                                                            >
                                                                                {author}
                                                                            </div>
                                                                        );
                                                                    }
                                                                )
                                                            }
                                                        </div>
                                                        <div
                                                        className = "categories"
                                                        style = {{display: book.categories.length !== 0 ? "flex" : "none"}}
                                                        >
                                                            {
                                                                book.categories.map
                                                                (
                                                                    (category, categoryIndex) =>
                                                                    {
                                                                        return (
                                                                            <div
                                                                            className = "category"
                                                                            key = {categoryIndex}
                                                                            >
                                                                                {category}
                                                                            </div>
                                                                        );
                                                                    }
                                                                )
                                                            }
                                                        </div>
                                                        <div
                                                        className = "score"
                                                        style = {{display: book.score !== null ? "block" : "none"}}
                                                        >
                                                            {book.score !== null ? book.score : ""}
                                                        </div>
                                                    </div>
                                                </div>
                                            </Link>
                                            <button
                                            className = "removeButton"
                                            onClick = {() => {}}
                                            >
                                                X
                                            </button>
                                        </div>
                                    );
                                }
                            )
                        }
                    </div>
                    <div
                    className = "description"
                    style = {{display: mode === "description" ? "block" : "none"}}
                    >
                        {description}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default BookshelfView;