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
    const [bookshelf, setBookshelf] = useState(null);
    const [books, setBooks] = useState(null);
    const [mode, setMode] = useState("books");
    const navigate = useNavigate();
    const {ownerId} = useParams();
    const {id} = useParams();

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                try
                {
                    var response;
                    setOverlay(true);
                    if (loggedAccount?.id !== undefined && loggedAccount?.id === Number(ownerId))
                    {
                        response = await api.get
                        (
                            "/bookshelf/findownbyid/"+id,
                            {
                                headers:
                                {
                                    email: loggedAccount?.email,
                                    password: loggedAccount?.password
                                }
                            }
                        );
                    }
                    else
                    {
                        response = await api.get("/bookshelf/findbyid/"+id);
                    }
                    setOverlay(false);
                    setBookshelf(response?.data);
                    if (response?.data?.bookApiIds?.length > 0)
                    {
                        loadBookCards(true);
                    }
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
            runEffect();
            return (() => {mounted = false;});
        },
        [id, loggedAccount]
    );

    async function loadBookCards(overwrite)
    {
        if (overwrite || books?.length < bookshelf?.bookApiIds?.length)
        {
            var offset;
            if (overwrite)
            {
                offset = 0;
            }
            else
            {
                offset = books?.length;
            }
            try
            {
                var response;
                setOverlay(true);
                if (loggedAccount?.id !== undefined && loggedAccount?.id === Number(ownerId))
                {
                    response = await api.get
                    (
                        "/bookshelf/findownbooksbyidpaginate/"+id,
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
                else
                {
                    response = await api.get
                    (
                        "/bookshelf/findbooksbyidpaginate/"+id,
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
                if (overwrite)
                {
                    setBooks(response?.data);
                }
                else
                {
                    setBooks([...books, ...response?.data]);
                }
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
    }

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
        if (date !== null && date !== undefined)
        {
            var dateArray = date?.split("-");
            var newDate = dateArray[2]+"/"+dateArray[1]+"/"+dateArray[0];
            return newDate;
        }
        else
        {
            return undefined;
        }
    }

    function handleEdit()
    {
        navigate("/bookshelf/edit/"+id);
    }

    async function handleDelete()
    {
        try
        {
            setOverlay(true);
            await api.delete
            (
                "/bookshelf/deletebyid/"+id,
                {
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            setAlert([{text: "Bookshelf deleted.", type: "success", key: Math.random()}]);
            navigate("/account/view/"+bookshelf?.owner?.id);
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

    async function handleRemoveBook(index)
    {
        try
        {
            setOverlay(true);
            await api.patch
            (
                "/bookshelf/removebookapiidbyid/"+id,
                {},
                {
                    params:
                    {
                        apiId: books?.[index]?.apiId
                    },
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            var newBooks = [...books];
            newBooks.splice(index, 1);
            setBooks(newBooks);
            setAlert([{text: "Book removed.", type: "success", key: Math.random()}]);
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed")
            {
                localStorage.clear();
                setLoggedAccount(null);
            }
            // navigate("/");
        }
    }

    return(
        <div className = "bookshelfViewArea">
            <div className = "mainBox">
                <div className = "topBox">
                    <div className = "covers">
                        <div
                        className = "cover coverOne"
                        style = {{backgroundImage: "url("+books?.[0]?.cover+")"}}
                        />
                        <div
                        className = "cover coverTwo"
                        style = {{backgroundImage: "url("+books?.[1]?.cover+")"}}
                        />
                        <div
                        className = "cover coverThree"
                        style = {{backgroundImage: "url("+books?.[2]?.cover+")"}}
                        />
                    </div>
                    <div className = "headBox">
                        <div className = "name">{bookshelf?.name}</div>
                        <Link
                        className = "ownerLink"
                        to = {"/account/view/"+bookshelf?.owner?.id}
                        >
                            <div
                            className = "ownerPicture"
                            style = {{backgroundImage: "url("+bookshelf?.owner?.picture+")"}}
                            />
                            <div className = "ownerName">{bookshelf?.owner?.name}</div>
                        </Link>
                        <div className = "creationDate">Created in {handleFormatDate(bookshelf?.creationDate)}</div>
                    </div>
                    {
                        loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id ?
                        <div className = "buttonBox">
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
                        </div> :
                        <></>
                    }
                </div>
                {
                    (loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id) || bookshelf?.privacy ?
                    <>
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
                            {
                                mode === "books" ?
                                <div className = "books">
                                    {
                                        books?.map
                                        (
                                            (book, bookIndex) =>
                                            {
                                                return (
                                                    <div
                                                    className = "bookBox"
                                                    key = {bookIndex}
                                                    >
                                                        <Link to = {"/books/view/"+book?.apiId}>
                                                            <div className = "book">
                                                                <div
                                                                className = "cover"
                                                                style = {{backgroundImage: "url("+book?.cover+")"}}
                                                                />
                                                                <div className = "bookInfoBox">
                                                                    <div className = "title">{book?.title}</div>
                                                                    <div className = "authors">
                                                                        {
                                                                            book?.authors?.map
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
                                                                    <div className = "categories">
                                                                        {
                                                                            book?.categories?.map
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
                                                                    <div className = "score">{book?.score}</div>
                                                                </div>
                                                            </div>
                                                        </Link>
                                                        {
                                                            loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id ?
                                                            <button
                                                            className = "removeButton"
                                                            onClick = {() => {handleRemoveBook(bookIndex)}}
                                                            >
                                                                X
                                                            </button> :
                                                            <></>
                                                        }
                                                    </div>
                                                );
                                            }
                                        )
                                    }
                                </div> :
                                <div className = "description">{bookshelf?.description}</div>
                            }
                        </div>
                    </> :
                    <div className = "bottomBox">This bookshelf is private.</div>
                }
            </div>
        </div>
    );
}

export default BookshelfView;