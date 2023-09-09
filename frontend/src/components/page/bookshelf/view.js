import React, {useState, useEffect, useContext} from "react";
import {useParams, useNavigate, Link} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";
import scrollContext from "../../context/scroll.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";
import BookshelfCovers from "../../common/bookshelf/covers.js";
import BookCard from "../../common/book/card.js";

import "./view.css";

function BookshelfView()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const {scroll, setScroll} = useContext(scrollContext);
    const [bookshelf, setBookshelf] = useState(null);
    const [books, setBooks] = useState(null);
    const [mode, setMode] = useState("books");
    const navigate = useNavigate();
    const {id, ownerId} = useParams();

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
                        await loadBooks(true);
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
            return (() => {mounted = false});
        },
        [id, ownerId, loggedAccount]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if
                (
                    !overlay &&
                    books?.length < bookshelf?.bookApiIds?.length &&
                    scroll?.target?.scrollHeight-scroll?.target?.scrollTop <= scroll?.target?.offsetHeight+100
                )
                {
                    await loadBooks(false);
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [scroll, books]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (confirm === "delete")
                {
                    setConfirm(null);
                    await handleDelete();
                }
                else if (confirm?.response === "remove")
                {
                    setConfirm(null);
                    await handleRemoveBook(confirm?.index);
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [confirm]
    );

    async function loadBooks(overwrite)
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
                //navigate("/");
            }
        }
    }

    function handleChangeMode(newMode)
    {
        setMode(newMode);
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

    function handleConfirmDelete()
    {
        setConfirm([{identifier: "delete", text: "Delete bookshelf?", options: [{type: "delete", text: "Delete"}, {type: "cancel", text: "Cancel"}]}]);
    }

    function handleConfirmRemoveBook(index)
    {
        setConfirm([{identifier: "remove"+books?.[index]?.apiId, text: "Remove "+books?.[index]?.title+" from bookshelf?", options: [{type: {response: "remove", index: index}, text: "Remove"}, {type: "cancel", text: "Cancel"}]}]);
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
            setAlert([{type: "success", text: "Bookshelf deleted."}]);
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
                {
                    apiId: books?.[index]?.apiId
                },
                {
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
            setAlert([{type: "success", text: books?.[index]?.title+" removed from bookshelf."}]);
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
        <div className = "page bookshelfViewArea">
            <div className = "topBox">
                <BookshelfCovers covers = {[books?.[0]?.cover, books?.[1]?.cover, books?.[2]?.cover]}/>
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
                    <ButtonGroup options = {[{text: "Edit", operation: handleEdit}, {text: "Delete", operation: handleConfirmDelete}]}/> :
                    <></>
                }
            </div>
            {
                (loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id) || bookshelf?.privacy ?
                <>
                    <ButtonMode
                    modes = {[{text: "Books", type: "books"}, {text: "Description", type: "description"}]}
                    currentMode = {mode}
                    setMode = {handleChangeMode}
                    />
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
                                                <BookCard
                                                key = {bookIndex}
                                                book = {book}
                                                bookIndex = {bookIndex}
                                                remove = {handleConfirmRemoveBook}
                                                removeable = {loggedAccount?.id !== undefined && loggedAccount?.id === bookshelf?.owner?.id}
                                                />
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
    );
}

export default BookshelfView;