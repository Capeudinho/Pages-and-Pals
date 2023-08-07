import React, {useState, useEffect, useContext} from "react";
import {useParams, useNavigate, Link} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";
import BookshelfCard from "../../common/bookshelf/card.js";

import "./view.css";

function AccountView()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [account, setAccount] = useState (null);
    const [bookshelves, setBookshelves] = useState(null);
    const [mode, setMode] = useState("bookshelves");
    const navigate = useNavigate();
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
                    if (loggedAccount?.id !== undefined && loggedAccount?.id === Number(id))
                    {
                        response = await api.get
                        (
                            "/account/findownbyid/"+id,
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
                        response = await api.get("/account/findbyid/"+id);
                    }
                    setOverlay(false);
                    setAccount(response?.data);
                    if (response?.data?.bookshelfCount > 0)
                    {
                        loadBookshelves(true);
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
        [id]
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (confirm === "logout")
                {
                    setConfirm(null);
                    handleLogOut();
                }
                else if (confirm === "delete")
                {
                    setConfirm(null);
                    await handleDelete();
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [confirm]
    );

    async function loadBookshelves(overwrite)
    {
        if (overwrite || bookshelves?.length < account?.bookshelfCount)
        {
            var offset;
            if (overwrite)
            {
                offset = 0;
            }
            else
            {
                offset = bookshelves?.length;
            }
            try
            {
                var response;
                setOverlay(true);
                if (loggedAccount?.id !== undefined && loggedAccount?.id === Number(id))
                {
                    response = await api.get
                    (
                        "/bookshelf/findownbyowneridpaginate/"+id,
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
                        "/bookshelf/findbyowneridpaginate/"+id,
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
                    setBookshelves(response?.data);
                }
                else
                {
                    setBookshelves([...bookshelves, ...response?.data]);
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

    function handleChangeMode(newMode)
    {
        setMode(newMode);
    }

    function handleConfirmLogOut()
    {
        setConfirm([{identifier: "logout", text: "Log out from account?", options: [{type: "logout", text: "Log out"}, {type: "cancel", text: "Cancel"}]}]);
    }

    function handleConfirmDelete()
    {
        setConfirm([{identifier: "delete", text: "Delete account?", options: [{type: "delete", text: "Delete"}, {type: "cancel", text: "Cancel"}]}]);
    }

    function handleEdit()
    {
        navigate("/account/edit");
    }

    function handleLogOut()
    {
        localStorage.clear();
        setAlert([{type: "success", text: "Logged out from account."}]);
        setLoggedAccount(null);
        navigate("/");
    }

    async function handleDelete()
    {
        try
        {
            setOverlay(true);
            await api.delete
            (
                "/account/deletebyid/"+loggedAccount?.id,
                {
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            localStorage.clear();
            setAlert([{type: "success", text: "Account deleted."}]);
            setLoggedAccount(null);
            navigate("/");
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
        <div className = "area accountViewArea">
            <div className = "topBox">
                <div
                className = "picture"
                style = {{backgroundImage: "url("+account?.picture+")"}}
                />
                <div className = "headBox">
                    <div className = "name">
                        {account?.name}
                    </div>
                    <div className = "email">
                        {account?.email}
                    </div>
                </div>
                {
                    loggedAccount instanceof Object && loggedAccount?.id === Number(id) ?
                    <ButtonGroup options = {[{text: "Edit", operation: handleEdit}, {text: "Log out", operation: handleConfirmLogOut}, {text: "Delete", operation: handleConfirmDelete}]}/> :
                    <></>
                }
            </div>
            {
                (loggedAccount instanceof Object && loggedAccount?.id === Number(id)) || account?.privacy ?
                <>
                    <ButtonMode
                    modes = {[{text: "Bookshelves", type: "bookshelves"}, {text: "Reviews", type: "reviews"}, {text: "Biography", type: "biography"}]}
                    currentMode = {mode}
                    setMode = {handleChangeMode}
                    />
                    <div className = "infoBox">
                        {
                            mode === "biography" ?
                            <div className = "innerInfoBox biographyBox">
                                <div className = "biography">{account?.biography}</div>
                            </div> :
                            mode === "bookshelves" ?
                            <div className = "innerInfoBox bookshelvesBox">
                                {
                                    loggedAccount instanceof Object && loggedAccount?.id === Number(id) ?
                                    <Link to = {"/bookshelf/create"}>
                                        <button className = "createBookshelfButton">Create</button>
                                    </Link> :
                                    <></>
                                }
                                {
                                    bookshelves?.map
                                    (
                                        (bookshelf, index) =>
                                        {
                                            return (
                                                <BookshelfCard
                                                bookshelf = {bookshelf}
                                                key = {index}
                                                />
                                            );
                                        }
                                    )
                                }
                            </div> :
                            <div className = "innerInfoBox reviewsBox">
                                
                            </div>
                        }
                    </div>
                </> :
                <div className = "bottomBox">This account is private.</div>
            }
        </div>
    );
}

export default AccountView;