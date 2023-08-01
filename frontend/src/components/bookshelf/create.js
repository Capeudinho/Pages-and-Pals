import React, {useState, useContext} from "react";
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
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [privacy, setPrivacy] = useState(true);
    const navigate = useNavigate();

    function handleChangeName(event)
    {
        setName(event.target.value);
    }

    function handleChangeDescription(event)
    {
        setDescription(event.target.innerText);
    }

    function handleChangePrivacyPublic()
    {
        setPrivacy(true);
    }

    function handleChangePrivacyPrivate()
    {
        setPrivacy(false);
    }

    async function handleCreate()
    {
        setOverlay(true);
        try
        {
            await api.post
            (
                "/bookshelf/create",
                {
                    name: name,
                    description: description,
                    privacy: privacy,
                    owner: loggedAccount
                }
            );
            setAlert([{text: "Bookshelf created.", type: "success", key: Math.random()}]);
            navigate("/account/view/"+loggedAccount.id);
        }
        catch (exception)
        {
            if (exception.response.hasOwnProperty("data"))
            {
                if (exception.response.data === "invalid name")
                {
                    setAlert([{text: "Name is invalid.", type: "warning", key: Math.random()}]);
                }
            }
        }
        setOverlay(false);
    }

    async function handleCancel()
    {
        navigate(-1);
    }

    return (
        <div className = "area bookshelfCreateArea">
            <div className = "mainBox">
                <div className = "label">Name</div>
                <input
                className = "normalInput"
                value = {name}
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
                suppressContentEditableWarning = {true}
                >
                    {description}
                </div>
                <div className = "label">Privacy</div>
                <div className = "privacyBox">
                    <button
                    className = "privacyButton privacyPublicButton"
                    onClick = {() => {handleChangePrivacyPublic()}}
                    style = {{backgroundColor: privacy ? "#ffffff" : "#cccccc"}}
                    >
                        Public
                    </button>
                    <button
                    className = "privacyButton privacyPrivateButton"
                    onClick = {() => {handleChangePrivacyPrivate()}}
                    style = {{backgroundColor: !privacy ? "#ffffff" : "#cccccc"}}
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