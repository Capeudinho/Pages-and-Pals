import React, {useState, useEffect, useContext, useRef} from "react";
import {useNavigate} from "react-router-dom";
import api from "../../services/api.js";

import loggedAccountContext from "../context/loggedAccount.js";
import alertContext from "../context/alert.js";
import overlayContext from "../context/overlay.js";

import "./edit.css";

function AccountEdit()
{
    const {loggedAccount, setLoggedAccount} = useContext(loggedAccountContext);
    const {alert, setAlert} = useContext(alertContext);
    const {overlay, setOverlay} = useContext(overlayContext);
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [biography, setBiography] = useState("");
    const [privacy, setPrivacy] = useState(false);
    const [picture, setPicture] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const biographyInput = useRef();
    const navigate = useNavigate();

    useEffect
    (
        () =>
        {
            if (loggedAccount !== null)
            {
                setName(loggedAccount.name);
                setEmail(loggedAccount.email);
                setPassword(loggedAccount.password);
                setBiography(loggedAccount.biography);
                setPrivacy(loggedAccount.privacy);
                setPicture(loggedAccount.picture);
                biographyInput.current.innerText = loggedAccount.biography;
            }
            else
            {
                navigate("/");
            }
        },
        [loggedAccount]
    );

    function handleChangeName(event)
    {
        setName(event.target.value);
    }

    function handleChangeEmail(event)
    {
        setEmail(event.target.value);
    }

    function handleChangePassword(event)
    {
        setPassword(event.target.value);
    }

    function handleChangeBiography(event)
    {
        setBiography(event.target.innerText);
    }

    function handleChangePrivacyPublic()
    {
        setPrivacy(true);
    }

    function handleChangePrivacyPrivate()
    {
        setPrivacy(false);
    }

    function handleChangePicture(event)
    {
        setPicture(event.target.value);
    }

    function handleChangeShowPassword()
    {
        setShowPassword(!showPassword);
    }

    function handleInvalidPicture()
    {
        setPicture("");
    }

    async function handleSave()
    {
        setOverlay(true);
        try
        {
            const response = await api.patch
            (
                "/account/update",
                {
                    id: loggedAccount.id,
                    name: name,
                    email: email,
                    password: password,
                    biography: biography,
                    privacy: privacy,
                    picture: picture
                }
            );
            localStorage.setItem("account", JSON.stringify(response.data));
            setLoggedAccount(response.data);
            setAlert([{text: "Account saved.", type: "success", key: Math.random()}]);
            navigate("/account/view/"+loggedAccount.id);
        }
        catch (exception)
        {
            if (exception.response.hasOwnProperty("data"))
            {
                if (exception.response.data === "incorrect id")
                {
                    localStorage.clear();
                    setLoggedAccount(null);
                    navigate("/");
                }
                else if (exception.response.data === "email taken")
                {
                    setAlert([{text: "E-mail is taken.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid name")
                {
                    setAlert([{text: "Name is invalid.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid email")
                {
                    setAlert([{text: "E-mail is invalid.", type: "warning", key: Math.random()}]);
                }
                else if (exception.response.data === "invalid password")
                {
                    setAlert([{text: "Password is invalid.", type: "warning", key: Math.random()}]);
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
        <div className = "area accountEditArea">
            <div className = "mainBox">
                <div className = "label">Name</div>
                <input
                className = "normalInput"
                value = {name}
                onChange = {(event) => {handleChangeName(event)}}
                spellCheck = {false}
                />
                <div className = "label">E-mail</div>
                <input
                className = "normalInput"
                value = {email}
                onChange = {(event) => {handleChangeEmail(event)}}
                spellCheck = {false}
                />
                <div className = "label">Password</div>
                <div className = "passwordBox">
                    <input
                    className = "passwordInput"
                    value = {password}
                    onChange = {(event) => {handleChangePassword(event)}}
                    type = {showPassword ? "text" : "password"}
                    spellCheck = {false}
                    />
                    <button
                    className = "showPasswordButton"
                    onClick = {() => {handleChangeShowPassword()}}
                    >
                        {showPassword ? "H" : "S"}
                    </button>
                </div>
                <div className = "label">Biography</div>
                <div
                className = "biographyInput"
                ref = {biographyInput}
                value = {biography}
                onInput = {(event) => {handleChangeBiography(event)}}
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
                <div className = "label">Link to profile picture</div>
                <input
                className = "normalInput"
                value = {picture}
                onChange = {(event) => {handleChangePicture(event)}}
                spellCheck = {false}
                />
                <div className = "pictureBox">
                    <div
                    className = "picture bigPicture"
                    onError = {() => {handleInvalidPicture()}}
                    style = {{backgroundImage: picture === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+picture+")"}}
                    />
                    <div
                    className = "picture smallPicture"
                    style = {{backgroundImage: picture === "" ? "url(https://cdn.discordapp.com/attachments/623206414139260998/1133598045720817724/image.png)" : "url("+picture+")"}}
                    />
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

export default AccountEdit;