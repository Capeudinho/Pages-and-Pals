import React, { useState, useEffect, useContext, useRef } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";
import clickContext from "../../context/click.js";
import BookInfo from "../../common/book/info.js";

import "./view.css";

function BookView() {
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const { alert, setAlert } = useContext(alertContext);
    const { confirm, setConfirm } = useContext(confirmContext);
    const { overlay, setOverlay } = useContext(overlayContext);
    const { click, setClick } = useContext(clickContext);
    const [book, setBook] = useState(null);
    const [bookshelves, setBookshelves] = useState(null);
    const [showBookshelves, setShowBookshelves] = useState(false);
    const navigate = useNavigate();
    const { apiId } = useParams();
    const { id } = useParams();

    async function fetchBookByApiId(apiId) {
        try {
            setOverlay(true);
            var response = await api.get
                (
                    "book/findbookbyapiid/" + apiId
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
            }, [apiId]);

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if (showBookshelves && click?.target?.closest(".clickSensitive") === null) {
                        setShowBookshelves(false);
                    }
                    else if (!showBookshelves && click?.target?.closest(".clickResponsive") !== undefined && click?.target?.closest(".clickResponsive") !== null) {
                        if (bookshelves?.length === undefined) {
                            try {
                                setOverlay(true);
                                var response = await api.get
                                    (
                                        "/bookshelf/findownselectbyowneridandapiid/" + loggedAccount?.id + "/" + apiId,
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
                            catch (exception) {
                                setOverlay(false);
                                if (exception?.response?.data === "authentication failed") {
                                    localStorage.clear();
                                    setLoggedAccount(null);
                                }
                                navigate("/");
                            }
                        }
                        setShowBookshelves(true);
                    }
                }
                runEffect();
                return (() => { mounted = false });
            },
            [click]
        );

    useEffect
        (
            () => {
                let mounted = true;
                const runEffect = async () => {
                    if (confirm?.response === "remove") {
                        setConfirm(null);
                        await handleToggleBookApiId(confirm?.index);
                    }
                }
                runEffect();
            },
            [confirm]
        );

    async function handleConfirmToggleBookApiId(index) {
        if (bookshelves?.[index]?.contains) {
            setConfirm([{ identifier: "remove" + bookshelves?.[index]?.id, text: "Remove book from " + bookshelves?.[index]?.name + "?", options: [{ type: { response: "remove", index: index }, text: "Remove" }, { type: "cancel", text: "Cancel" }] }]);
        }
        else {
            await handleToggleBookApiId(index);
        }
    }

    async function handleToggleBookApiId(index) {
        var url;
        var text;
        if (bookshelves?.[index]?.contains) {
            url = "/bookshelf/removebookapiidbyid/" + bookshelves?.[index]?.id;
            text = book?.name + " removed from " + bookshelves?.[index]?.name + ".";
        }
        else {
            url = "/bookshelf/addbookapiidbyid/" + bookshelves?.[index]?.id;
            text = book?.name + " added to " + bookshelves?.[index]?.name + ".";
        }
        try {
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
        catch (exception) {
            setOverlay(false);
            if (exception?.response?.data === "authentication failed") {
                localStorage.clear();
                setLoggedAccount(null);
            }
            navigate("/");
        }
    }
    return (
        <div className="page bookViewArea">

            {book && <BookInfo book={book} />}

                {
                    loggedAccount?.id !== undefined ?
                        <div className="manageBox" style={{ padding: "20px" }}>
                            <button className="manageButton clickResponsive">
                                Manage in bookshelves
                            </button>
                            {
                                showBookshelves ?
                                    <>
                                        <div className="selects clickSensitive">
                                            {
                                                bookshelves?.map !== undefined ?
                                                    bookshelves.map
                                                        (
                                                            (bookshelf, bookshelfIndex) => {
                                                                return (
                                                                    <div
                                                                        className="select"
                                                                        key={bookshelfIndex}
                                                                    >
                                                                        <button
                                                                            className="selectButton"
                                                                            onClick={() => { handleConfirmToggleBookApiId(bookshelfIndex) }}
                                                                            disabled={overlay}
                                                                        >
                                                                            {bookshelf?.contains ? "R" : "A"}
                                                                        </button>
                                                                        <div className="bookshelfName">{bookshelf?.name}</div>
                                                                    </div>
                                                                );
                                                            }
                                                        ) :
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