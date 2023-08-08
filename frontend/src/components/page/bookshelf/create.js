import React, {useState, useEffect, useContext} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";

import "./create.css";

function BookshelfCreate()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {confirm, setConfirm} = useContext(confirmContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [bookshelf, setBookshelf] = useState
    (
        {
            name: "",
            description: "",
            privacy: true
        }
    );
    const navigate = useNavigate();

    useEffect
    (
        () =>
        {
            if (loggedAccount?.id === undefined)
            {
                navigate("/");
            }
        },
        []
    );

    useEffect
    (
        () =>
        {
            let mounted = true;
            const runEffect = async () =>
            {
                if (confirm === "discard")
                {
                    setConfirm(null);
                    handleDiscard();
                }
            }
            runEffect();
            return (() => {mounted = false});
        },
        [confirm]
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

    function handleChangePrivacy(newPrivacy)
    {
        var newBookshelf = {...bookshelf};
        newBookshelf.privacy = newPrivacy;
        setBookshelf(newBookshelf);
    }

    function handleConfirmDiscard()
    {
        setConfirm([{identifier: "discard", text: "Discard bookshelf?", options:[{type: "discard", text: "Discard"}, {type: "cancel", text: "Cancel"}]}]);
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
            setAlert([{type: "success", text: "Bookshelf created."}]);
            navigate("/bookshelf/from/"+loggedAccount.id+"/view/"+response?.data?.id);
        }
        catch (exception)
        {
            setOverlay(false);
            if (exception?.response?.data === "invalid name")
            {
                setAlert([{type: "warning", text: "Name is invalid."}]);
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

    async function handleDiscard()
    {
        navigate("/account/view/"+loggedAccount?.id);
    }

    return (
        <div className = "page bookshelfCreateArea">
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
            <ButtonMode
            modes = {[{text: "Public", type: true}, {text: "Private", type: false}]}
            currentMode = {bookshelf?.privacy}
            setMode = {handleChangePrivacy}
            appearance = "switch"
            />
            <ButtonGroup options = {[{text: "Create", operation: handleCreate}, {text: "Discard", operation: handleConfirmDiscard}]}/>
        </div>
    );
}

export default BookshelfCreate;