import React, { useState, useEffect, useContext } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import overlayContext from "../../context/overlay.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import clickContext from "../../context/click.js";

import "./manage.css";

function BookshelfManage({book})
{
    const { loggedAccount, setLoggedAccount } = useContext(loggedAccountContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {click, setClick} = useContext(clickContext);
    const [bookshelves, setBookshelves] = useState(null);
    const [showBookshelves, setShowBookshelves] = useState(false);
    const navigate = useNavigate();

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (showBookshelves && click?.target?.closest(".clickSensitive") === null)
                {
                    setShowBookshelves(false);
                }
                else if (!showBookshelves && click?.target?.closest(".clickResponsiveBookshelf"+book?.apiId) !== undefined && click?.target?.closest(".clickResponsiveBookshelf"+book?.apiId) !== null) {
                    if (bookshelves?.length === undefined) {
                        try {
                            setOverlay(true);
                            var response = await api.get
                                (
                                    "/bookshelf/findownselectbyowneridandapiid/" + loggedAccount?.id + "/" + book?.apiId,
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
                if (confirm?.response === "remove"+book?.apiId) {
                    setConfirm(null);
                    await handleToggleBookApiId(confirm?.index);
                }
            }
            runEffect();
            return (() => { mounted = false });
        },
        [confirm]
    );

    async function handleConfirmToggleBookApiId(index) {
        if (bookshelves?.[index]?.contains) {
            setConfirm
            (
                [
                    {
                        identifier: "remove"+book?.apiId+"at"+index,
                        text: "Remove "+book?.title+" from "+bookshelves?.[index]?.name+"?",
                        options:
                        [
                            {
                                type:
                                {
                                    response: "remove"+book?.apiId,
                                    index: index
                                },
                                text: "Remove"
                            },
                            {
                                type: "cancel",
                                text: "Cancel"
                            }
                        ]
                    }
                ]
            );
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
            text = book?.title + " removed from " + bookshelves?.[index]?.name + ".";
        }
        else {
            url = "/bookshelf/addbookapiidbyid/" + bookshelves?.[index]?.id;
            text = book?.title + " added to " + bookshelves?.[index]?.name + ".";
        }
        try {
            setOverlay(true);
            await api.patch
                (
                    url,
                    {
                        apiId: book?.apiId
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
        <div className="bookshelfManageArea">
            <button className={"manageButton clickResponsiveBookshelf"+book?.apiId}>
                M
            </button>
            {
                showBookshelves ?
                <div className="selects clickSensitive">
                    {
                        bookshelves?.length !== undefined ?
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
                                                    {bookshelf?.contains ? "✘" : "✔"}
                                                </button>
                                                <div className="bookshelfName">{bookshelf?.name}</div>
                                            </div>
                                        );
                                    }
                                ) :
                            <></>
                    }
                </div> :
                <></>
            }
        </div>
    );
}

export default BookshelfManage;