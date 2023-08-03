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
    const [account, setAccount] = useState (null);
    const [bookshelves, setBookshelves] = useState(null);
    const [mode, setMode] = useState("biography");
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
                    var response;
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
                    setAccount(response?.data);
                    if (response?.data?.bookshelfCount > 0)
                    {
                        loadBookshelfCards(true);
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

    async function loadBookshelfCards(overwrite)
    {
        if (bookshelves?.length < account?.bookshelfCount)
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
            setOverlay(true);
            try
            {
                var response;
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

    function handleLogOut()
    {
        localStorage.clear();
        setLoggedAccount("none");
        navigate("/");
    }

    function handleChangeModeBookshelves()
    {
        setMode("bookshelves");
    }

    function handleChangeModeReviews()
    {
        setMode("reviews");
    }

    function handleChangeModeBiography()
    {
        setMode("biography");
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
            localStorage.clear();
            setLoggedAccount("none");
            setAlert([{text: "Account deleted.", type: "success", key: Math.random()}]);
            navigate("/");
        }
        catch (exception)
        {
            if (exception?.response?.data === "incorrect id")
            {
                localStorage.clear();
                setLoggedAccount("none");
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

    return (
        <div className = "area accountViewArea">
            <div className = "mainBox">
                <div className = "topBox">
                    <div
                    className = "picture"
                    style = {{backgroundImage: "url("+account?.picture+")"}}
                    />
                    <div className = "headBox">
                        <div className = "name">
                            {account?.name}
                        </div>
                        {
                            (loggedAccount?.id !== undefined && loggedAccount?.id === Number(id)) || account?.privacy ?
                            <div className = "email">
                                {account?.email}
                            </div> :
                            <></>
                        }
                    </div>
                    {
                        loggedAccount?.id !== undefined && loggedAccount?.id === Number(id) ?
                        <div className = "buttonBox">
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
                        </div> :
                        <></>
                    }
                </div>
                {
                    (loggedAccount?.id !== undefined && loggedAccount?.id === Number(id)) || account?.privacy ?
                    <>
                        <div className = "modeBox">
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
                        <div className = "infoBox">
                            {
                                mode === "biography" ?
                                <div className = "innerInfoBox biographyBox">
                                    <div className = "biography">{account?.biography}</div>
                                </div> :
                                mode === "bookshelves" ?
                                <div className = "innerInfoBox bookshelvesBox">
                                    <Link to = {"/bookshelf/create"}>
                                        <button className = "createBookshelfButton">Create</button>
                                    </Link>
                                    {
                                        bookshelves !== null ? 
                                        bookshelves.map
                                        (
                                            (bookshelf, index) =>
                                            {
                                                return (
                                                    <Link
                                                    to = {"/bookshelf/from/"+bookshelf?.owner?.id+"/view/"+bookshelf?.id}
                                                    key = {index}
                                                    >
                                                        <div className = "bookshelf">
                                                            <div className = "bookshelfCovers">
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
                                                            <div className = "bookshelfInfo">
                                                                <div className = "bookshelfName">
                                                                    {bookshelf?.name}
                                                                </div>
                                                                <div className = "bookshelfDescription">
                                                                    {bookshelf?.description}
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </Link>
                                                );
                                            }
                                        ) :
                                        <></>
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
        </div>
    );
}

export default AccountView;