import React, {useState, useEffect, useContext, useRef} from "react";
import {useParams, useNavigate} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";
import eventContext from "../../context/event.js";

import "./view.css";

function BookView()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const {event, setEvent} = useContext(eventContext);
    const [book, setBook] = useState(null);
    const [bookshelves, setBookshelves] = useState(null);
    const [showBookshelves, setShowBookshelves] = useState(false);
    const [mode, setMode] = useState("???");
    const navigate = useNavigate();
    const {apiId} = useParams();

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (showBookshelves && event?.target?.closest(".clickSensitive") === null)
                {
                    setShowBookshelves(false);
                }
                else if (!showBookshelves && event?.target?.closest(".clickResponsive") !== undefined && event?.target?.closest(".clickResponsive") !== null)
                {
                    if (bookshelves?.length === undefined)
                    {
                        try
                        {
                            setOverlay(true);
                            var response = await api.get
                            (
                                "/bookshelf/findownselectbyowneridandapiid/"+loggedAccount?.id+"/"+apiId,
                                {
                                    headers:
                                    {
                                        email: loggedAccount?.email,
                                        password: loggedAccount?.password
                                    }
                                }
                            );
                            setOverlay(false);
                            setBookshelves(response?.data);
                        }
                        catch (exception)
                        {
                            setOverlay(false);
                            // ...
                        }
                    }
                    setShowBookshelves(true);
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [event]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (confirm?.response === "remove")
                {
                    setConfirm(null);
                    await handleToggleBookApiId(confirm?.index);
                }
            }
            runEffect();
        },
        [confirm]
    );

    async function handleConfirmToggleBookApiId(index)
    {
        if (bookshelves?.[index]?.contains)
        {
            setConfirm([{identifier: "remove"+bookshelves?.[index]?.id, text: "Remove book from "+bookshelves?.[index]?.name+"?", options: [{type: {response: "remove", index: index}, text: "Remove"}, {type: "cancel", text: "Cancel"}]}]);
        }
        else
        {
            await handleToggleBookApiId(index);
        }
    }

    async function handleToggleBookApiId(index)
    {
        var url;
        var text;
        if (bookshelves?.[index]?.contains)
        {
            url = "/bookshelf/removebookapiidbyid/"+bookshelves?.[index]?.id;
            text = book?.name+" removed from "+bookshelves?.[index]?.name+".";
        }
        else
        {
            url = "/bookshelf/addbookapiidbyid/"+bookshelves?.[index]?.id;
            text = book?.name+" added to "+bookshelves?.[index]?.name+".";
        }
        try
        {
            setOverlay(true);
            await api.patch
            (
                url,
                {
                    apiId: apiId
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
            var newBookshelves = [...bookshelves];
            newBookshelves[index].contains = !newBookshelves?.[index]?.contains;
            setBookshelves(newBookshelves);
            setAlert
            (
                [
                    {
                        text: text,
                        type: "success"
                    }
                ]
            );
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
        <div className = "area bookViewArea">
            {
                loggedAccount?.id !== undefined ?
                <div className = "manageBox" style = {{padding: "20px"}}>
                    <button className = "manageButton clickResponsive">
                        Manage in bookshelves
                    </button>
                    {
                        showBookshelves ?
                        <>
                            <div className = "selects clickSensitive">
                                {
                                    bookshelves?.map !== undefined ?
                                    bookshelves.map
                                    (
                                        (bookshelf, bookshelfIndex) =>
                                        {
                                            return (
                                                <div
                                                className = "select"
                                                key = {bookshelfIndex}
                                                >
                                                    <button
                                                    className = "selectButton"
                                                    onClick = {() => {handleConfirmToggleBookApiId(bookshelfIndex)}}
                                                    >
                                                        {bookshelf?.contains ? "R" : "A"}
                                                    </button>
                                                    <div className = "bookshelfName">{bookshelf?.name}</div>
                                                </div>
                                            );
                                        }
                                    ):
                                    <></>
                                }
                            </div>
                        </> :
                        <></>
                    }
                </div> :
                <></>
            }
        </div>
    );
}

export default BookView;