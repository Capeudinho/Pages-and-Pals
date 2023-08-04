import React, {useState, useEffect, useContext} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./create.css";

function BookshelfCreate()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [bookshelf, setBookshelf] = useState
    (
        {
            name: "",
            description: "",
            privacy: false
        }
    );
    const navigate = useNavigate();

    useEffect
    (
        () =>
        {
            if (loggedAccount?.id !== undefined)
            {
                navigate("/");
            }
        },
        []
    );

    function handleChangeName(event)
    {
        var newBookshelf = {...bookshelf};
        newBookshelf.name = event.target.value;
        setBookshelf(newBookshelf);
    }

    function handleChangeDescription(event)
    {
        var newBookshelf = {...bookshelf};
        newBookshelf.description = event.target.innerText;
        setBookshelf(newBookshelf);
    }

    function handleChangePrivacyPublic()
    {
        var newBookshelf = {...bookshelf};
        newBookshelf.privacy = true;
        setBookshelf(newBookshelf);
    }

    function handleChangePrivacyPrivate()
    {
        var newBookshelf = {...bookshelf};
        newBookshelf.privacy = false;
        setBookshelf(newBookshelf);
    }

    async function handleCreate()
    {
        try
        {
            setOverlay(true);
            var response = await api.post
            (
                "/bookshelf/create",
                bookshelf,
                {
                    headers:
                    {
                        email: loggedAccount?.email,
                        password: loggedAccount?.password
                    }
                }
            );
            setOverlay(false);
            setAlert([{text: "Bookshelf created.", type: "success", key: Math.random()}]);
            navigate("/bookshelf/from/"+loggedAccount.id+"/view/"+response?.data?.id);
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "invalid name")
            {
                setAlert([{text: "Name is invalid.", type: "warning", key: Math.random()}]);
            }
            else if (exception?.response?.data === "authentication failed")
            {
                localStorage.clear();
                setLoggedAccount(null);
                navigate("/");
            }
            else
            {
                navigate("/");
            }
        }
    }

    async function handleCancel()
    {
        // confirm
        navigate("/account/view/"+loggedAccount?.id);
    }

    return (
        <div className = "area bookshelfCreateArea">
            <div className = "mainBox">
                <div className = "label">Name</div>
                <input
                className = "normalInput"
                value = {bookshelf?.name}
                onChange = {(event) => {handleChangeName(event)}}
                spellCheck = {false}
                />
                <div className = "label">Description</div>
                <div
                className = "descriptionInput"
                onInput = {(event) => {handleChangeDescription(event)}}
                spellCheck = {false}
                contentEditable = {true}
                placeholder = "Optional"
                />
                <div className = "label">Privacy</div>
                <div className = "privacyBox">
                    <button
                    className = "privacyButton privacyPublicButton"
                    onClick = {() => {handleChangePrivacyPublic()}}
                    style = {{backgroundColor: bookshelf?.privacy ? "#ffffff" : "#cccccc"}}
                    >
                        Public
                    </button>
                    <button
                    className = "privacyButton privacyPrivateButton"
                    onClick = {() => {handleChangePrivacyPrivate()}}
                    style = {{backgroundColor: !bookshelf?.privacy ? "#ffffff" : "#cccccc"}}
                    >
                        Private
                    </button>
                </div>
                <div className = "buttonBox">
                    <button
                    className = "completeButton"
                    onClick = {() => {handleCreate()}}
                    >
                        Create
                    </button>
                    <button
                    className = "completeButton"
                    onClick = {() => {handleCancel()}}
                    >
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
}

export default BookshelfCreate;