import React, {useState, useEffect, useContext, useRef} from "react";
import {useParams, useNavigate} from "react-router-dom";
import api from "../../../services/api.js";

import loggedAccountContext from "../../context/loggedAccount.js";
import alertContext from "../../context/alert.js";
import confirmContext from "../../context/confirm.js";
import overlayContext from "../../context/overlay.js";

import ButtonGroup from "../../common/button/group.js";
import ButtonMode from "../../common/button/mode.js";

import "./edit.css";

function BookshelfEdit()
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
            privacy: false
        }
    );
    const descriptionInput = useRef();
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
                    setOverlay(true);
                    var accountResponse = await api.get
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
                    setOverlay(false);
                    setBookshelf(accountResponse?.data);
                    descriptionInput.current.innerText = accountResponse?.data?.description;
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
        setConfirm([{identifier: "discard", text: "Discard alterations to bookshelf?", options:[{type: "discard", text: "Discard"}, {type: "cancel", text: "Cancel"}]}]);
    }

    async function handleSave()
    {
        try
        {
            setOverlay(true);
            await api.patch
            (
                "/bookshelf/update",
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
            setAlert([{type: "success", text: "Bookshelf saved."}]);
            navigate("/bookshelf/from/"+loggedAccount?.id+"/view/"+id);
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
        navigate("/bookshelf/from/"+loggedAccount?.id+"/view/"+id);
    }

    return (
        <div className = "area bookshelfEditArea">
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
            ref = {descriptionInput}
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
            <ButtonGroup options = {[{text: "Save", operation: handleSave}, {text: "Discard", operation: handleConfirmDiscard}]}/>
        </div>
    );
}

export default BookshelfEdit;