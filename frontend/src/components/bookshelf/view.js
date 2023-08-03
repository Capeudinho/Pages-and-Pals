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
                setOverlay(true);
                try
                {
                    var response;
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
                    setBookshelf(response?.data);
                    if (response?.data?.bookApiIds?.length > 0)
                    {
                        loadBookCards(true);
                    }
                }
                catch (exception)
                {
                    if (exception?.response?.data === "incorrect id")
                    {
                        navigate("/");
                    }
                    else if (exception?.response?.data === "authentication failed")
                    {
                        localStorage.clear();
                        setLoggedAccount(null);
                        navigate("/");
                    }
                    else if (exception?.response?.data === "access denied")
                    {
                        navigate("/");
                    }
                }
                setOverlay(false);
            }
            runEffect();
            return (() => {mounted = false;});
        },
        [id]
    );

    async function loadBookCards(overwrite)
    {
        if (books?.length < bookshelf?.bookApiIds?.length)
        {
            var offset = null;
            if (overwrite)
            {
                offset = 0;
            }
            else
            {
                offset = books?.length;
            }
            setOverlay(true);
            try
            {
                var response;
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
                if (exception?.response?.data === "incorrect id")
                {
                    navigate("/");
                }
                else if (exception?.response?.data === "authentication failed")
                {
                    localStorage.clear();
                    setLoggedAccount(null);
                    navigate("/");
                }
                else if (exception?.response?.data === "access denied")
                {
                    navigate("/");
                }
            }
            setOverlay(false);
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
        setOverlay(true);
        try
        {
            await api.delete("/bookshelf/deletebyid/"+id);
            setAlert([{text: "Bookshelf deleted.", type: "success", key: Math.random()}]);
            if (bookshelf?.owner?.id !== undefined)
            {
                navigate("/account/view/"+bookshelf?.owner?.id);
            }
            else
            {
                navigate("/");
            }
        }
        catch (exception)
        {
            if (exception?.response?.data === "incorrect id")
            {
                navigate("/");
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
                        style = {{backgroundImage: "url("+bookshelf?.covers?.[0]+")"}}
                        />
                        <div
                        className = "cover coverTwo"
                        style = {{backgroundImage: "url("+bookshelf?.covers?.[1]+")"}}
                        />
                        <div
                        className = "cover coverThree"
                        style = {{backgroundImage: "url("+bookshelf?.covers?.[2]+")"}}
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
                        {
                            loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id ?
                            <div className = "creationDate">{handleFormatDate(bookshelf?.creationDate)}</div> :
                            <></>
                        }
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
                                        books !== null ?
                                        books.map
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
                                                            onClick = {() => {}}
                                                            >
                                                                X
                                                            </button> :
                                                            <></>
                                                        }
                                                    </div>
                                                );
                                            }
                                        ) :
                                        <></>
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