import React, {useState, useEffect, useContext, useRef} from "react";
import {useParams, useNavigate, Link} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./edit.css";

function BookshelfEdit()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [privacy, setPrivacy] = useState(true);
    const [creationDate, setCreationDate] = useState("");
    const [bookApiIds, setBookApiIds] = useState([]);
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
                setOverlay(true);
                try
                {
                    const accountResponse = await api.get("/bookshelf/findbyid/"+id);
                    setName(accountResponse.data.name);
                    setDescription(accountResponse.data.description);
                    setPrivacy(accountResponse.data.privacy);
                    setCreationDate(accountResponse.data.creationDate);
                    setBookApiIds(accountResponse.data.bookApiIds);
                    descriptionInput.current.innerText = accountResponse.data.description;
                }
                catch (exception)
                {
                    if (exception.response.hasOwnProperty("data"))
                    {
                        if (exception.response.data === "incorrect id")
                        {
                            navigate("/");
                        }
                    }
                }
                setOverlay(false);
            }
            runEffect();
            return (() => {mounted = false;});
        },
        [id]
    );

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

    async function handleSave()
    {
        setOverlay(true);
        try
        {
            await api.patch
            (
                "/bookshelf/update",
                {
                    id: id,
                    name: name,
                    description: description,
                    privacy: privacy,
                    creationDate: creationDate,
                    bookApiIds: bookApiIds,
                    owner: loggedAccount
                }
            );
            setAlert([{text: "Bookshelf saved.", type: "success", key: Math.random()}]);
            navigate("/bookshelf/view/"+id);
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
        <div className = "area bookshelfEditArea">
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
                ref = {descriptionInput}
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
                    onClick = {() => {handleSave()}}
                    >
                        Save
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

export default BookshelfEdit;